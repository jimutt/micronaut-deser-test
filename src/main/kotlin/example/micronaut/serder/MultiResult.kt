package example.micronaut.serder

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class MultiResult<T>(val result: List<T>)