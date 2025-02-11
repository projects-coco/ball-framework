package org.coco.presentation.mvc.middleware

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.coco.core.utils.HidingToResponse
import org.coco.domain.model.EntityBase
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

abstract class EntityJsonSerializer : JsonSerializer<EntityBase>() {
    override fun serialize(
        value: EntityBase,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        gen.writeStartObject()

        val visibleFields =
            getVisibleFields(value)
                .filter { it.findAnnotation<HidingToResponse>() == null }

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
