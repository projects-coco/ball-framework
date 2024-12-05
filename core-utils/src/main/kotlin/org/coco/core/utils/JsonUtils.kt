package org.coco.core.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.Reader

object JsonUtils {
    private val objectMapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(JavaTimeModule())
        .registerKotlinModule()

    fun serialize(`object`: Any): String {
        return objectMapper.writeValueAsString(`object`)
    }

    fun <T> deserialize(reader: Reader, typeReference: TypeReference<T>): T {
        return objectMapper.readValue(reader, typeReference)
    }

    fun <T> deserialize(reader: Reader, clazz: Class<T>): T {
        return objectMapper.readValue(reader, clazz)
    }

    fun <T> deserialize(jsonString: String, clazz: Class<T>): T {
        return objectMapper.readValue(jsonString, clazz)
    }

    fun <T> deserialize(jsonString: String, typeReference: TypeReference<T>): T {
        return objectMapper.readValue(jsonString, typeReference)
    }
}