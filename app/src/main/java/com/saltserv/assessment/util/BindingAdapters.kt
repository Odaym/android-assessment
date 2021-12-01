package com.saltserv.assessment.util

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun bindVisibleGone(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}