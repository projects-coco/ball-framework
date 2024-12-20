package org.coco.core.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class JsonUtilsTest : FunSpec({
    test("JsonUtils#serialize(Any)") {
        val sample =
            object {
                @Suppress("unused")
                val name = "coco"
            }
        JsonUtils.serialize(sample) shouldBe """{"name":"coco"}"""
    }

    test("JsonUtils#deserialize(String, Class<T>)") {
        val sample = """{"name":"coco"}"""

        data class Sample(val name: String)

        JsonUtils.deserialize(sample, Sample::class.java) shouldBe Sample("coco")
    }

    test("JsonUtils#deserialize(String)") {
        val sample = """{"name":"coco"}"""

        JsonUtils.deserialize(sample) shouldBe
            mapOf(
                "name" to "coco",
            )
    }
})
