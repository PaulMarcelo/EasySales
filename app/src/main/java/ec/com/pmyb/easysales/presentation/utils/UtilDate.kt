package ec.com.pmyb.easysales.presentation.utils

import java.util.Calendar

object UtilDate {

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Los meses son 0-indexados
        val year = calendar.get(Calendar.YEAR)
        return "$day/$month/$year"
    }

}