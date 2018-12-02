package com.no1.taiwan.newsbasket.features.main

import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.devrapid.dialogbuilder.support.QuickDialogFragment
import com.devrapid.kotlinknifer.loge
import com.devrapid.kotlinshaver.cast
import com.devrapid.kotlinshaver.isNull
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.no1.taiwan.newsbasket.R
import com.no1.taiwan.newsbasket.bases.AdvFragment
import com.no1.taiwan.newsbasket.components.recyclerview.NewsAdapter
import com.no1.taiwan.newsbasket.components.recyclerview.NewsMultiVisitable
import com.no1.taiwan.newsbasket.components.recyclerview.helpers.NewsItemTouchHelper
import com.no1.taiwan.newsbasket.components.recyclerview.helpers.ViewItemTouchCallback
import com.no1.taiwan.newsbasket.entities.KeywordEntity
import com.no1.taiwan.newsbasket.ext.const.DEFAULT_STR
import com.no1.taiwan.newsbasket.ext.happenError
import com.no1.taiwan.newsbasket.ext.muteErrorDoWith
import com.no1.taiwan.newsbasket.ext.observeNonNull
import com.no1.taiwan.newsbasket.ext.peel
import com.no1.taiwan.newsbasket.ext.peelSkipLoading
import com.no1.taiwan.newsbasket.features.main.viewmodels.KeywordViewModel
import com.no1.taiwan.newsbasket.internal.di.tags.ObjectLabel.KEYOWRD_ADAPTER
import com.no1.taiwan.newsbasket.internal.di.tags.ObjectLabel.LINEAR_LAYOUT_VERTICAL
import kotlinx.android.synthetic.main.dialog_input_keyword.view.btn_send
import kotlinx.android.synthetic.main.dialog_input_keyword.view.et_keyword
import kotlinx.android.synthetic.main.fragment_keyword.fab_add
import kotlinx.android.synthetic.main.fragment_keyword.rv_keywords
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onKey
import org.kodein.di.generic.instance

class KeywordFragment : AdvFragment<MainActivity, KeywordViewModel>() {
    private val linearLayout by instance<LinearLayoutManager>(LINEAR_LAYOUT_VERTICAL)
    private val keywordAdapter by instance<NewsAdapter>(KEYOWRD_ADAPTER)
    private val dupKeywords = mutableListOf<String>()
    private val helper = object : ViewItemTouchCallback {
        override fun onItemSwiped(position: Int) {
            removedPosition = position
            currentDeleted = dupKeywords[position]
            vm.removeKeyword(currentDeleted)
        }

        override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        }
    }
    private var currentInput = DEFAULT_STR
    private var currentDeleted = DEFAULT_STR
    private var removedPosition = -1

    //region Base build-in functions
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        observeNonNull(vm.keywordsLiveData) {
            peelSkipLoading {
                dupKeywords.addAll(it)
                // Only first time into here.
                keywordAdapter.appendList(it.map { cast<NewsMultiVisitable>(KeywordEntity(it)) }.toMutableList())
            } happenError { loge(it) } muteErrorDoWith this@KeywordFragment
        }
        observeNonNull(vm.storeKeywordLiveData) {
            peelSkipLoading {
                vm.updateRemoteSubscribing(currentInput)
            } happenError { Snackbar.make(fab_add, it, LENGTH_SHORT).show() } muteErrorDoWith this@KeywordFragment
        }
        observeNonNull(vm.updateKeywordsLiveData) {
            peel {
                keywordAdapter.appendList(mutableListOf(KeywordEntity(currentInput)))
                dupKeywords.add(currentInput)
                Snackbar.make(fab_add, "success", LENGTH_SHORT).show()
            } happenError { Snackbar.make(fab_add, it, LENGTH_SHORT).show() } muteErrorDoWith this@KeywordFragment
        }
        observeNonNull(vm.removeKeywordLiveData) {
            peel {
                if (it) dupKeywords.removeAt(removedPosition)
            } happenError {
                Snackbar.make(fab_add, it, LENGTH_SHORT).show()
                // Rollback the data we deleted.
                vm.storeLocalKeyword(currentDeleted)
            } muteErrorDoWith this@KeywordFragment
        }
    }

    /**
     * Initialize method.
     *
     * @param savedInstanceState before status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
        componentSetting()
        eventSetting()

        vm.fetchLocalKeywords()
    }

    /**
     * Set the parentView for inflating.
     *
     * @return [LayoutRes] layout xml.
     */
    override fun provideInflateView() = R.layout.fragment_keyword

    /**
     * Set fragment title into action bar.
     *
     * @return [String] action bar title.
     */
    override fun actionBarTitle() = getString(R.string.title_keyword)
    //endregion

    private fun componentSetting() {
        ItemTouchHelper(NewsItemTouchHelper(cast(keywordAdapter), helper)).attachToRecyclerView(rv_keywords)

        rv_keywords.apply {
            if (layoutManager.isNull())
                layoutManager = linearLayout
            if (adapter.isNull())
                adapter = keywordAdapter
        }
    }

    private fun eventSetting() {
        fab_add.onClick {
            createKeywordDialog()
        }
    }

    private fun createKeywordDialog() {
        QuickDialogFragment.Builder(this) {
            viewResCustom = R.layout.dialog_input_keyword
            fetchComponents = { v, df ->
                v.apply {
                    btn_send.onClick {
                        currentInput = v.et_keyword.text.toString().replace("\n", DEFAULT_STR)
                        vm.storeLocalKeyword(currentInput)
                        df.dismiss()
                    }
                    et_keyword.onKey { v, keyCode, event ->
                        if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                            btn_send.performClick()
                        }
                    }
                }
            }
        }.build().show()
    }
}
