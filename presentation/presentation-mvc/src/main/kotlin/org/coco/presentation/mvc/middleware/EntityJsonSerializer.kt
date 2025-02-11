package org.coco.presentation.mvc.middleware

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.coco.core.utils.HidingToResponse
import org.coco.domain.model.EntityBase
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

abstract class EntityJsonSerializer : JsonSerializer<EntityBase>() {
    override fun serialize(
        value: EntityBase,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        gen.writeStartObject()

        val visibleFields =
            getVisibleFields(value)
                .filter {
                    it.javaField?.getAnnotation(HidingToResponse::class.java) == null
                }

        writeVisibleFields(gen, value, visibleFields)

        gen.writeEndObject()
    }

    private fun writeVisibleFields(
        gen: JsonGenerator,
        entity: EntityBase,
        visibleFields: List<KProperty1<Any, *>>,
    ) {
        visibleFields.forEach { field ->
            field.isAccessible = true
            gen.writeObjectField(field.name, field.get(entity))
        }
    }

    protected abstract fun getVisibleFields(entity: EntityBase): List<KProperty1<Any, *>>
}
