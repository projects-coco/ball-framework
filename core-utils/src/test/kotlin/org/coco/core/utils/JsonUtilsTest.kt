package org.coco.core.utils

import arrow.core.Option
import arrow.core.toOption
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.coco.core.extension.toOption

class JsonUtilsTest :
    FunSpec({
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

            data class Sample(
                val name: String,
            )

            JsonUtils.deserialize(sample, Sample::class.java) shouldBe Sample("coco")
        }

        test("JsonUtils#deserialize(String)") {
            val sample = """{"name":"coco"}"""

            JsonUtils.deserialize(sample) shouldBe
                mapOf(
                    "name" to "coco",
                )
        }

        test("JsonUtils#deserialize() with arrow.core.option #1") {
            data class Sample(
                val name: Option<String> = null.toOption(),
            )

            val sample = """{"name":"coco"}"""

            JsonUtils.deserialize(sample, Sample::class) shouldBe Sample(name = "coco".toOption())
        }

        test("JsonUtils#deserialize() with arrow.core.option #2") {
            data class Sample(
                val name: Option<String> = null.toOption(),
            )

            val sample = """{}"""

            JsonUtils.deserialize(sample, Sample::class) shouldBe Sample(name = null.toOption())
        }
    })
