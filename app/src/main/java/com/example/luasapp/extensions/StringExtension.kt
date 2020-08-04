package com.example.luasapp.extensions

import androidx.core.os.LocaleListCompat
import com.example.luasapp.utils.DatePatterns
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Parses text from the beginning of the given string to produce a date.
 * The method may not use the entire text of the given string.
 * @param dtPattern the <code>Enum</code> whose beginning should be parsed.
 * @return A <code>Date</code> parsed from the string. it will return null
 * if the beginning of the specified string cannot be parsed
 */
fun String?.toDate(dtPattern: DatePatterns): Date? {
    if (this.isNullOrBlank()) return null

    val locale = LocaleListCompat.getDefault().get(0)
    val parsedDate: Date?
    val formatter = SimpleDateFormat(dtPattern.pattern, locale)

    try {
        parsedDate = formatter.parse(this)
    } catch (e: ParseException) {
        return null
    }

    return parsedDate
}


/**
 * Parses text from the beginning of the given string to produce a date.
 * The method may not use the entire text of the given string.
 * @param dtPattern the <code>Enum</code> whose beginning should be parsed.
 * @param locale the locale to be used to parse the string to date
 * @return A <code>Date</code> parsed from the string. it will return null
 * if the beginning of the specified string cannot be parsed
 */
fun String?.toDate(dtPattern: DatePatterns, locale: Locale): Date? {
    if (this.isNullOrBlank()) return null

    val parsedDate: Date?
    val formatter = SimpleDateFormat(dtPattern.pattern, locale)

    try {
        parsedDate = formatter.parse(this)
    } catch (e: ParseException) {
        return null
    }

    return parsedDate
}