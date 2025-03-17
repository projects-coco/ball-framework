package org.coco.core.utils

import arrow.integrations.jackson.module.registerArrowModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import com.fasterxml.jackson.module.kotlin.addSerializer
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.coco.core.type.BinaryId
import java.io.Reader
import kotlin.reflect.KClass

object JsonUtils {
    private val simpleObjectMapper: ObjectMapper =
        ObjectMapper().apply {
            registerModule(Jdk8Module())
            registerModule(JavaTimeModule())
            registerKotlinModule()
            registerArrowModule()
            registerModule(
                SimpleModule().apply {
                    addSerializer(BinaryId::class, BinaryIdSerializer())
                    addDeserializer(BinaryId::class, BinaryIdDeserializer())
                },
            )
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

    private val objectMapper: ObjectMapper =
        simpleObjectMapper.copy().apply {
            activateDefaultTyping(
                BasicPolymorphicTypeValidator
                    .builder()
                    .allowIfBaseType(Map::class.java)
                    .allowIfSubType(Any::class.java)
                    .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY,
            )
        }

    fun serialize(`object`: Any): String = objectMapper.writeValueAsString(`object`)

    fun deserialize(jsonString: String): Map<String, Any> = simpleObjectMapper.readValue(jsonString)

    fun <T : Any> deserialize(
        reader: Reader,
        typeReference: TypeReference<T>,
    ): T = objectMapper.readValue(reader, typeReference)

    fun <T : Any> deserialize(
        reader: Reader,
        clazz: Class<T>,
    ): T = objectMapper.readValue(reader, clazz)

    fun <T : Any> deserialize(
        jsonString: String,
        clazz: KClass<T>,
    ): T = objectMapper.readValue(jsonString, clazz.java)

    fun <T : Any> deserialize(
        jsonString: String,
        clazz: Class<T>,
    ): T = objectMapper.readValue(jsonString, clazz)

    fun <T : Any> deserialize(
        jsonString: String,
        typeReference: TypeReference<T>,
    ): T = objectMapper.readValue(jsonString, typeReference)
}
