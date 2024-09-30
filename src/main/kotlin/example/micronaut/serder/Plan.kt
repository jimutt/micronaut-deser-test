package example.micronaut.serder

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Plan(
  val id: String,
  val name: String
)