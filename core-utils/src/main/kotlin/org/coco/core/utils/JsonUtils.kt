package org.coco.core.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.Reader

object JsonUtils {
    private val objectMapper =
        ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(Jdk8Module())
            .registerModule(JavaTimeModule())
            .registerKotlinModule()

    fun serialize(`object`: Any): String {
        return objectMapper.writeValueAsString(`object`)
    }

    fun <T> deserialize(
        reader: Reader,
        typeReference: TypeReference<T>,
    ): T {
        return objectMapper.readValue(reader, typeReference)
    }

    fun <T> deserialize(
        reader: Reader,
        clazz: Class<T>,
    ): T {
        return objectMapper.readValue(reader, clazz)
    }

    fun deserialize(jsonString: String): Map<String, Any> {
        return objectMapper.readValue(jsonString)
    }

    fun <T> deserialize(
        jsonString: String,
        clazz: Class<T>,
    ): T {
        return objectMapper.readValue(jsonString, clazz)
    }

    fun <T> deserialize(
        jsonString: String,
        typeReference: TypeReference<T>,
    ): T {
        return objectMapper.readValue(jsonString, typeReference)
    }
}
