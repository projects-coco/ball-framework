package org.coco.presentation.mvc.middleware

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.coco.core.type.BinaryId
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class BinaryIdSerializerConfig : JsonSerializer<BinaryId>() {
    override fun serialize(
        value: BinaryId?,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        gen.writeString(value.toString())
    }
}
