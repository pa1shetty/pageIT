package com.example.pageit.utils


import com.example.pageit.utils.DateMethods.DateConstants.date_format_fb
import com.example.pageit.utils.DateMethods.DateConstants.date_format_ui
import com.example.pageit.utils.DateMethods.DateConstants.yyyymmddhhmmss
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateMethods {

    object DateConstants {
        const val yyyymmddhhmmss = "yyyymmddhhmmss"
        const val date_format_ui = "dd MMM yyyy HH:mm"
        const val date_format_fb = "yyyy-MM-dd'T'HH:mm:ssZ"

    }


    private fun stringToDate(
        dateInString: String,
        dateInStringFormat: String = yyyymmddhhmmss
    ): Date {
        val sdf = SimpleDateFormat(dateInStringFormat, Locale.getDefault())
        return try {
            sdf.parse(dateInString)!!
        } catch (e: ParseException) {
            throw e.message?.let { GeneralException(it) }!!
        }
    }


    private fun convertDateFormat(date: Date = Date(), requiredDateFormat: String): String {
        return SimpleDateFormat(requiredDateFormat, Locale.getDefault()).format(date)
    }

    private fun convertDateFormat(
        originalDate: String,
        originalDateFormat: String,
        requiredDateFormat: String
    ): String {
        return convertDateFormat(stringToDate(originalDate, originalDateFormat), requiredDateFormat)
    }


    fun convertFbDate(date: String, requiredFormat: String = date_format_ui) =
        convertDateFormat(date, date_format_fb, requiredFormat)
}