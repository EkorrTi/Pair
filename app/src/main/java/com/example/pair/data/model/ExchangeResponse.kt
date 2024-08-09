package com.example.pair.data.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class ExchangeResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: List<ConversionRate>,
)

data class ConversionRate(
    val code: String,
    val rate: Double,
)

class ExchangeResponseDeserializer : JsonDeserializer<ExchangeResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ExchangeResponse {
        if (json == null || context == null) {
            throw Exception("Error")
        }
        val obj = json.asJsonObject

        // let Gson handle the other 3 properties
        val result = context.deserialize<String?>(obj.get("result"), String::class.java)
        val base = context.deserialize<String?>(obj.get("base_code"), String::class.java)

        // create List<Rate> from the rates JsonObject
        val ratesSet = obj.get("conversion_rates").asJsonObject.entrySet()
        val ratesList = ratesSet.map {
            val code = it.key
            val value = it.value.asDouble
            ConversionRate(code, value)
        }

        return ExchangeResponse(result, base, ratesList)
    }
}