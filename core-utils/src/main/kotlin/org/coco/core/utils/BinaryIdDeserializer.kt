package org.coco.core.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.coco.core.type.BinaryId

class BinaryIdDeserializer : StdDeserializer<BinaryId>(BinaryId::class.java) {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
    ): BinaryId {
        val value = p.valueAsString
        return BinaryId.fromString(value)
    }
}
