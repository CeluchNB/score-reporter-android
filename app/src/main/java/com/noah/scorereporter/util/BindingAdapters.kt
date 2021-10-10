package com.noah.scorereporter.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("yearText")
fun yearText(textView: TextView, date: Date?) {
    if (date == null) return

    val simpleDateFormat = SimpleDateFormat("yyyy", Locale.US)
    val yearText = simpleDateFormat.format(date)
    textView.text = yearText
}
