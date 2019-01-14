package com.no1.taiwan.newsbasket.features.main

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.google.firebase.iid.FirebaseInstanceId
import com.no1.taiwan.newsbasket.App
import com.no1.taiwan.newsbasket.R
import com.no1.taiwan.newsbasket.bases.AdvFragment
import com.no1.taiwan.newsbasket.ext.observeUnboxNonNull
import com.no1.taiwan.newsbasket.features.main.viewmodels.IndexViewModel

class IndexFragment : AdvFragment<MainActivity, IndexViewModel>() {
    //region Base build-in functions
    /** The block of binding to [androidx.lifecycle.ViewModel]'s [androidx.lifecycle.LiveData]. */
    override fun bindLiveData() {
        observeUnboxNonNull(vm.tokenLiveData) {
            App.isFirstTimeOpen = true
        }
    }

    /**
     * Initialize method.
     *
     * @param savedInstanceState before status.
     */
    override fun rendered(savedInstanceState: Bundle?) {
        if (!App.isFirstTimeOpen) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                vm.addFirstSubscriber(it.token)
            }
        }
    }

    /**
     * For separating the huge function code in [rendered]. Initialize all component listeners here.
     */
    override fun componentListenersBinding() {
//        btn_next.onClick {
//            findNavController().navigate(R.id.action_nav_index_to_keyword)
//        }
//        btn_archive.onClick {
//            findNavController().navigate(R.id.action_nav_index_to_archive)
//        }
    }

    /**
     * Set the parentView for inflating.
     *
     * @return [LayoutRes] layout xml.
     */
    override fun provideInflateView() = R.layout.fragment_news

    /**
     * Set fragment title into action bar.
     *
     * @return [String] action bar title.
     */
    override fun actionBarTitle() = getString(R.string.app_name)
    //endregion
}
