@file:Suppress("NOTHING_TO_INLINE")

package com.no1.taiwan.newsbasket.ext

import android.app.Activity
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import com.devrapid.kotlinknifer.gone
import com.devrapid.kotlinknifer.visible
import com.no1.taiwan.newsbasket.ext.const.DEFAULT_STR
import org.jetbrains.anko.find
import org.jetbrains.anko.findOptional

fun Activity.showViewStub(@IdRes stub: Int, @IdRes realView: Int, options: (View.() -> Unit)? = null) {
    (findOptional<ViewStub>(stub)?.inflate() ?: find<View>(realView).apply { visible() }).apply {
        bringToFront()
        invalidate()
        options?.let(this::apply)
    }
}

inline fun Activity.showLoadingView() = showViewStub(R.id.vs_loading, R.id.v_loading)

inline fun Activity.showErrorView(errorMsg: String = DEFAULT_STR) =
    showViewStub(R.id.vs_error, R.id.v_error) { find<TextView>(R.id.tv_error_msg).text = errorMsg }

inline fun Activity.showRetryView(noinline retryListener: ((View) -> Unit)? = null) {
    showViewStub(R.id.vs_retry, R.id.v_retry) RetryView@{ retryListener?.let(this@RetryView::setOnClickListener) }
}

inline fun Activity.hideLoadingView() = findOptional<View>(R.id.v_loading)?.gone() ?: Unit

inline fun Activity.hideErrorView() =
    find<View>(R.id.v_error).apply { find<TextView>(R.id.tv_error_msg).text = DEFAULT_STR }.gone()

inline fun Activity.hideRetryView() = find<View>(R.id.v_retry).apply {
    find<Button>(R.id.btn_retry).takeIf { hasOnClickListeners() }?.let { setOnClickListener(null) }
}.gone()
