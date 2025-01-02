package org.coco.core.utils

fun <T> List<T>.lowerBound(
    target: T,
    comparator: Comparator<in T>,
): Int {
    var low = 0
    var high = this.size

    while (low < high) {
        val mid = (low + high) / 2
        if (comparator.compare(this[mid], target) < 0) {
            low = mid + 1
        } else {
            high = mid
        }
    }

    return low
}

fun <T : Comparable<T>> List<T>.lowerBound(target: T): Int = lowerBound(target) { a, b -> a.compareTo(b) }

fun <T> List<T>.upperBound(
    target: T,
    comparator: Comparator<in T>,
): Int {
    var low = 0
    var high = this.size

    while (low < high) {
        val mid = (low + high) / 2
        if (comparator.compare(this[mid], target) <= 0) {
            low = mid + 1
        } else {
            high = mid
        }
    }

    return high
}

fun <T : Comparable<T>> List<T>.upperBound(target: T): Int = upperBound(target) { a, b -> a.compareTo(b) }
