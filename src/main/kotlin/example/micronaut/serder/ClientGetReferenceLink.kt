package example.micronaut.serder

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ClientGetReferenceLink(
  val urlStylised: String,
  val urlUnstylised: String,
  val textLong: String?,
  val textShort: String?,
  val splashScreenTypeId: Int,
  val version: Version?
)

@Serdeable
data class Version(val major: Int, val minor: Int)
