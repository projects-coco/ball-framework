package org.coco.core.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.coco.core.type.BinaryId

class BinaryIdSerializer : JsonSerializer<BinaryId>() {
    override fun serialize(
        value: BinaryId?,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        gen.writeString(value.toString())
    }
}
