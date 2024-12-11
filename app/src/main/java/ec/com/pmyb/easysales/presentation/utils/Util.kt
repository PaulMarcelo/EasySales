package ec.com.pmyb.easysales.presentation.utils

import com.google.gson.Gson
import kotlin.reflect.KProperty1

object Util {
    fun objectToJsonString(obj: Any): String {
        val gson = Gson()
        return gson.toJson(obj)
    }

    inline fun <reified T> jsonStringToObject(json: String?): T? {
        val gson = Gson()
        return try {
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null // Devuelve null si hay algún error en la conversión
        }
    }

    fun <T, R> getPropertyName(property: KProperty1<T, R>): String {
        return property.name
    }
}