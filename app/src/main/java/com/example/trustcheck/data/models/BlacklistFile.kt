package com.example.trustcheck.data.models

import android.app.Activity
import java.io.*
import java.util.*


class BlacklistFile(parent: File?, child: String?) :
    File(parent, child) {
    fun load(): List<Number> {
        val numbers: MutableList<Number> = LinkedList()
        val fin: FileInputStream
        val reader: BufferedReader
        var line: String
        var n: Number
        var sep: Int = 0
        return try {
            fin = FileInputStream(this)
            reader = BufferedReader(InputStreamReader(fin))
            while (reader.readLine().also { line = it } != null &&
                line.indexOf(END_NUMBER_DELIMETER).also { sep = it } != -1) {
                n = Number()
                n.number = line.substring(0, sep)
                n.name = line.substring(sep + END_NUMBER_DELIMETER.length)
                numbers.add(n)
            }
            fin.close()
            numbers
        } catch (exception: IOException) {
            numbers
        }
    }

    fun store(numbers: List<Number>, activity: Activity?): Boolean {
        return try {
            val fout = FileOutputStream(this)
            for (n in numbers) {
                fout.write(n.number?.toByteArray())
                fout.write(END_NUMBER_DELIMETER.toByteArray())
                fout.write(n.name?.toByteArray())
                fout.write("\n".toByteArray())
            }
            fout.close()
            true

            // if we don't have permission, bail immediately; failure message is already displayed
        } catch (exception: IOException) {
            false
        }
    }

    companion object {
        const val END_NUMBER_DELIMETER = ": "
        const val DEFAULT_FILENAME = "NoPhoneSpam_blacklist.txt"
    }
}