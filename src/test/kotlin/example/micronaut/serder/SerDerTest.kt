package example.micronaut.serder

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.core.type.Argument
import io.micronaut.core.type.GenericArgument
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@MicronautTest
class SerDerTest {

	@Inject
	lateinit var objectMapper: ObjectMapper

	@ParameterizedTest
	@ValueSource(strings = ["serde-naive", "serde-generic", "jackson-naive", "jackson-typereference"])
	fun clientGetReferenceLink_Deserialize(deserializeMethod: String) {
		val json = "{\"urlStylised\":\"urlStylised\",\"urlUnstylised\":\"urlUnstylised\",\"textLong\":\"textLong\",\"textShort\":\"textShort\",\"splashScreenTypeId\":1,\"version\":{\"major\":1,\"minor\":0}}"
		val referenceLink = deserialize<ClientGetReferenceLink>(json, deserializeMethod)
		val expected = ClientGetReferenceLink("urlStylised", "urlUnstylised", "textLong", "textShort", 1, Version(1, 0))
		Assertions.assertEquals(expected, referenceLink)
	}

	@ParameterizedTest
	@ValueSource(strings = ["serde-naive", "serde-generic", "jackson-naive", "jackson-typereference"])
	fun clientGetReferenceLink_List_Deserialize(deserializeMethod: String) {
		val json = "[{\"urlStylised\":\"urlStylised\",\"urlUnstylised\":\"urlUnstylised\",\"textLong\":\"textLong\",\"textShort\":\"textShort\",\"splashScreenTypeId\":1,\"version\":{\"major\":1,\"minor\":0}}]"
		val referenceLinks = deserialize<List<ClientGetReferenceLink>>(json, deserializeMethod)
		val expectedFirstItem = ClientGetReferenceLink("urlStylised", "urlUnstylised", "textLong", "textShort", 1, Version(1, 0))
		Assertions.assertEquals(expectedFirstItem, referenceLinks?.get(0))
	}

	@ParameterizedTest
	@ValueSource(strings = ["serde-naive", "serde-generic", "jackson-naive", "jackson-typereference"])
	fun clientGetReferenceLink_Map_Deserialize(deserializeMethod: String) {
		val json = "{\"key\":{\"urlStylised\":\"urlStylised\",\"urlUnstylised\":\"urlUnstylised\",\"textLong\":\"textLong\",\"textShort\":\"textShort\",\"splashScreenTypeId\":1,\"version\":{\"major\":1,\"minor\":0}}}"
		val referenceLinks = deserialize<Map<String, ClientGetReferenceLink>>(json, deserializeMethod)
		val expectedFirstItem = ClientGetReferenceLink("urlStylised", "urlUnstylised", "textLong", "textShort", 1, Version(1, 0))
		Assertions.assertEquals(expectedFirstItem, referenceLinks?.get("key"))
	}

	@ParameterizedTest
	@ValueSource(strings = ["serde-naive", "serde-generic", "jackson-naive", "jackson-typereference"])
	fun clientGetReferenceLink_MapToList_Deserialize(deserializeMethod: String) {
		val json = "{\"key\":[{\"urlStylised\":\"urlStylised\",\"urlUnstylised\":\"urlUnstylised\",\"textLong\":\"textLong\",\"textShort\":\"textShort\",\"splashScreenTypeId\":1,\"version\":{\"major\":1,\"minor\":0}}]}"
		val referenceLinks = deserialize<Map<String, List<ClientGetReferenceLink>>>(json, deserializeMethod)
		val expectedFirstItem = ClientGetReferenceLink("urlStylised", "urlUnstylised", "textLong", "textShort", 1, Version(1, 0))
		Assertions.assertEquals(expectedFirstItem, referenceLinks?.get("key")?.get(0))
	}

	private inline fun <reified T> deserialize(value: String, method: String): T? {
		return when (method) {
			"serde-naive" -> objectMapper.readValue(value, T::class.java)

			"serde-generic" -> {
				val argument: Argument<T> = object: GenericArgument<T>() {}
				objectMapper.readValue(value, argument)
			}

			"jackson-naive" -> {
				val jacksonMapper = jacksonObjectMapper()
				jacksonMapper.readValue(value, T::class.java)
			}

      "jackson-typereference" -> {
				val jacksonMapper = jacksonObjectMapper()
				val typeRef = object : TypeReference<T>() {}
				jacksonMapper.readValue(value, typeRef)
			}

			else -> throw IllegalArgumentException("Unknown deserialization method: $method")
		}
	}
}