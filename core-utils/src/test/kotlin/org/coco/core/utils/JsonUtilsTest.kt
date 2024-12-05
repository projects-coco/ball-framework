package org.coco.core.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class JsonUtilsTest : FunSpec({
    test("JsonUtils::serialize") {
        val sample = object {
            @Suppress("unused")
            val name = "coco"
        }
        JsonUtils.serialize(sample) shouldBe """{"name":"coco"}"""
    }

    test("JsonUtils::deserialize") {
        val sample = """{"name":"coco"}"""

        data class Sample(val name: String)

        JsonUtils.deserialize(sample, Sample::class.java) shouldBe Sample("coco")
    }
})
