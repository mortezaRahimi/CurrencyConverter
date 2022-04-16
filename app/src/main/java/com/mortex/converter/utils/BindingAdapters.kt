package com.mortex.converter.utils

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("unreadCount")
fun TextView.checkUnread(count: Int) {
    if (count > 0) {
        text = count.toString()
        (this.parent as FrameLayout).visibility = VISIBLE
        visibility = VISIBLE
    } else {
        (this.parent as FrameLayout).visibility = GONE
        visibility = GONE
    }
}
