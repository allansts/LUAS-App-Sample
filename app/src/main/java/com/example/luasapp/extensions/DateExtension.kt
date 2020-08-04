package com.example.luasapp.extensions

import androidx.core.os.LocaleListCompat
import com.example.luasapp.utils.DatePatterns
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Formats a Date into a date/time string on the current locale
 * @param dtPattern the <code>Enum</code> time value to be formatted into a time string.
 * @return the formatted time string.
 */
fun Date?.toString(dtPattern: DatePatterns) : String {
    this ?: return ""
    val locale = LocaleListCompat.getDefault().get(0)
    val formatter = SimpleDateFormat(dtPattern.pattern, locale)
    return formatter.format(this)
}

/**
 * Formats a Date into a date/time string.
 * @param dtPattern the <code>Enum</code> time value to be formatted into a time string.
 * @param locale the locale which will be use to format the date
 * @return the formatted time string.
 */
fun Date?.toString(dtPattern: DatePatterns, locale: Locale) : String {
    this ?: return ""
    val formatter = SimpleDateFormat(dtPattern.pattern, locale)
    return formatter.format(this)
}