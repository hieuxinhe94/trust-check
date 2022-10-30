package com.example.trustcheck.data.models

import android.content.ContentValues
import android.text.TextUtils
import android.util.SparseIntArray
import com.example.trustcheck.ui.helper.DBHelper

data class PhoneData(
    var addressId: String?,
    var typeId: String?,
    var predictType: String?,
    var predictvar: String?,
    var lastActive: String?,
    var lastReport: String?,
    var freqIndex: String?,
    var lastSync: String?
)

data class MessageData(
    var addressId: String?,
    var typeId: String?,
    var predictvar: String?,
    var content: String?,
    var lastActive: String?,
    var lasReport: String?,
    var freqIndex: String?,
    var lastSync: String?
)

data class ReportData(
    var addressId: String?,
    var typeId: String?,
    var title: String?,
    var predictType: String?,
    var predictvar: String?,
    var dateReport: String?,
    var authorName: String?,
    var authorAddress: String?
)

data class Category(
    var typeId: String?, var name: String?, var description: String?
)

class Number {
    var number: String? = null
    var name: String? = null

    constructor() {}

    @JvmOverloads
    constructor(number: String?, name: String? = null) {
        this.number = number
        this.name = name
    }

    companion object {
        @JvmStatic
        fun fromValues(values: ContentValues): Number {
            val number = Number()
            number.number = values.getAsString(DBHelper.ADDRESS_ID)
            return number
        }

        @JvmStatic
        fun wildcardsDbToView(number: String): String {
            return number
                .replace('%', '*')
                .replace('_', '#')
        }

        @JvmStatic
        fun wildcardsViewToDb(number: String): String {
            return number
                .replace("[^+#*%_0-9]".toRegex(), "")
                .replace('*', '%')
                .replace('#', '_')
        }
    }
}

object PhoneNumber {
    private val KEYPAD_MAP = SparseIntArray()

    init {
        KEYPAD_MAP.put('a'.code, '2'.code)
        KEYPAD_MAP.put('b'.code, '2'.code)
        KEYPAD_MAP.put('c'.code, '2'.code)
        KEYPAD_MAP.put('A'.code, '2'.code)
        KEYPAD_MAP.put('B'.code, '2'.code)
        KEYPAD_MAP.put('C'.code, '2'.code)
        KEYPAD_MAP.put('d'.code, '3'.code)
        KEYPAD_MAP.put('e'.code, '3'.code)
        KEYPAD_MAP.put('f'.code, '3'.code)
        KEYPAD_MAP.put('D'.code, '3'.code)
        KEYPAD_MAP.put('E'.code, '3'.code)
        KEYPAD_MAP.put('F'.code, '3'.code)
        KEYPAD_MAP.put('g'.code, '4'.code)
        KEYPAD_MAP.put('h'.code, '4'.code)
        KEYPAD_MAP.put('i'.code, '4'.code)
        KEYPAD_MAP.put('G'.code, '4'.code)
        KEYPAD_MAP.put('H'.code, '4'.code)
        KEYPAD_MAP.put('I'.code, '4'.code)
        KEYPAD_MAP.put('j'.code, '5'.code)
        KEYPAD_MAP.put('k'.code, '5'.code)
        KEYPAD_MAP.put('l'.code, '5'.code)
        KEYPAD_MAP.put('J'.code, '5'.code)
        KEYPAD_MAP.put('K'.code, '5'.code)
        KEYPAD_MAP.put('L'.code, '5'.code)
        KEYPAD_MAP.put('m'.code, '6'.code)
        KEYPAD_MAP.put('n'.code, '6'.code)
        KEYPAD_MAP.put('o'.code, '6'.code)
        KEYPAD_MAP.put('M'.code, '6'.code)
        KEYPAD_MAP.put('N'.code, '6'.code)
        KEYPAD_MAP.put('O'.code, '6'.code)
        KEYPAD_MAP.put('p'.code, '7'.code)
        KEYPAD_MAP.put('q'.code, '7'.code)
        KEYPAD_MAP.put('r'.code, '7'.code)
        KEYPAD_MAP.put('s'.code, '7'.code)
        KEYPAD_MAP.put('P'.code, '7'.code)
        KEYPAD_MAP.put('Q'.code, '7'.code)
        KEYPAD_MAP.put('R'.code, '7'.code)
        KEYPAD_MAP.put('S'.code, '7'.code)
        KEYPAD_MAP.put('t'.code, '8'.code)
        KEYPAD_MAP.put('u'.code, '8'.code)
        KEYPAD_MAP.put('v'.code, '8'.code)
        KEYPAD_MAP.put('T'.code, '8'.code)
        KEYPAD_MAP.put('U'.code, '8'.code)
        KEYPAD_MAP.put('V'.code, '8'.code)
        KEYPAD_MAP.put('w'.code, '9'.code)
        KEYPAD_MAP.put('x'.code, '9'.code)
        KEYPAD_MAP.put('y'.code, '9'.code)
        KEYPAD_MAP.put('z'.code, '9'.code)
        KEYPAD_MAP.put('W'.code, '9'.code)
        KEYPAD_MAP.put('X'.code, '9'.code)
        KEYPAD_MAP.put('Y'.code, '9'.code)
        KEYPAD_MAP.put('Z'.code, '9'.code)
    }

    fun normalizeNumber(phoneNumber: String?): String {
        if (TextUtils.isEmpty(phoneNumber)) {
            return ""
        }
        val sb = StringBuilder()
        val len = phoneNumber!!.length
        for (i in 0 until len) {
            val c = phoneNumber[i]
            // Character.digit() supports ASCII and Unicode digits (fullwidth, Arabic-Indic, etc.)
            val digit = c.digitToIntOrNull() ?: -1
            if (digit != -1) {
                sb.append(digit)
            } else if (sb.isEmpty() && c == '+') {
                sb.append(c)
            } else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                return normalizeNumber(convertKeypadLettersToDigits(phoneNumber))
            }
        }
        return sb.toString().replace("+84", "0")
    }

    fun convertKeypadLettersToDigits(input: String?): String? {
        if (input == null) {
            return input
        }
        val len = input.length
        if (len == 0) {
            return input
        }
        val out = input.toCharArray()
        for (i in 0 until len) {
            val c = out[i]
            // If this char isn't in KEYPAD_MAP at all, just leave it alone.
            out[i] = KEYPAD_MAP[c.code, c.code].toChar()
        }
        return String(out)
    }
}
