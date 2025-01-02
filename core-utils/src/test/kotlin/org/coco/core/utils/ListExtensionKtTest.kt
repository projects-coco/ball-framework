package org.coco.core.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ListExtensionKtTest :
    FunSpec({
        test("List<T>::lowerBound() - 1") {
            val list = listOf(1, 2, 3, 4, 5)
            val result = list.lowerBound(target = 3)
            result shouldBe 2
        }

        test("List<T>::lowerBound() - 2") {
            val list = listOf(1, 2, 3, 3, 4, 5)
            val result = list.lowerBound(target = 3)
            result shouldBe 2
        }

        test("List<T>::lowerBound() - 3") {
            val list = listOf(1, 2, 3, 4, 5)
            val result = list.lowerBound(6)
            result shouldBe 5
        }

        test("List<T>::upperBound() - 1") {
            val list = listOf(1, 2, 3, 4, 5)
            val result = list.upperBound(target = 3)
            result shouldBe 3
        }

        test("List<T>::upperBound() - 2") {
            val list = listOf(1, 2, 3, 3, 4, 5)
            val result = list.upperBound(target = 3)
            result shouldBe 4
        }

        test("List<T>::upperBound() - 3") {
            val list = listOf(1, 2, 3, 4, 5)
            val result = list.upperBound(6)
            result shouldBe 5
        }
    })
