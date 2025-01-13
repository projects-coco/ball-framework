package org.coco.infra.jpa.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Duration
import java.time.temporal.ChronoUnit

@Converter(autoApply = true)
class DurationToLongJpaConverter : AttributeConverter<Duration, Long> {
    override fun convertToDatabaseColumn(attribute: Duration?): Long = attribute?.toNanos() ?: 0L

    override fun convertToEntityAttribute(dbData: Long): Duration = Duration.of(dbData, ChronoUnit.NANOS)
}
