package org.coco.core.utils

import arrow.core.Option
import arrow.core.toOption
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.coco.core.extension.toOption
import org.coco.core.type.BinaryId

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

        test("JsonUtils#deserialize() with nested arrow.core.option #1") {
            data class Sample(
                val name: Option<String> = null.toOption(),
            )

            data class SampleWrapper(
                val sample: Sample = Sample(),
                val t: Option<String> = null.toOption(),
                val f: Option<String> = null.toOption(),
            )

            val sample = """{"sample": {"name":"coco"}, "t":"t"}"""

            JsonUtils.deserialize(sample, SampleWrapper::class) shouldBe
                SampleWrapper(
                    sample = Sample(name = "coco".toOption()),
                    t = "t".toOption(),
                    f = null.toOption(),
                )
        }

        test("JsonUtils#deserialize() with nested arrow.core.option #2") {
            data class Sample(
                val name: Option<String> = null.toOption(),
            )

            data class SampleWrapper(
                val sample: Sample = Sample(),
                val t: Option<String> = null.toOption(),
            )

            val sample = """{"t":"t"}"""

            JsonUtils.deserialize(sample, SampleWrapper::class) shouldBe
                SampleWrapper(
                    sample = Sample(),
                    t = "t".toOption(),
                )
        }

        test("JsonUtils#deserialize() with nested arrow.core.option #3") {
            data class Sample(
                val id: Option<BinaryId> = null.toOption(),
                val name: Option<String> = null.toOption(),
            )

            data class SampleWrapper(
                val sample: Sample = Sample(),
                val t: Option<String> = null.toOption(),
            )

            val id = BinaryId.new()
            val sample = """{"sample":{"id": "$id"}, "t":"t"}"""

            val deserialized = JsonUtils.deserialize(sample, SampleWrapper::class)

            deserialized.sample.id.onSome {
                it.contentEquals(id) shouldBe true
            }
            deserialized.t shouldBe "t".toOption()
        }
    })
