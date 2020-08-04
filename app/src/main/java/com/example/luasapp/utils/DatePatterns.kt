package com.example.luasapp.utils

enum class DatePatterns(val pattern: String) {
    HH_mm("HH:mm"),
    dd_MM_yyyy("dd/MM/yyyy"),
    dd_MM_yyyy_HH_mm("dd/MM/yyyy HH:mm"),
    yyyy_MM_ddTHH_mm_ss("yyyy-MM-dd'T'HH:mm:ss")
}