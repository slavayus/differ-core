package com.differ.differcore.service.transformer

import com.differ.differcore.service.MapTransformerServiceImpl
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.isEmptyString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.powermock.reflect.Whitebox
import java.util.stream.Collectors
import java.util.stream.Stream

class MapTransformerServiceFlattenTest5 {

    private lateinit var mapTransformerService: MapTransformerServiceImpl

    @BeforeEach
    fun init() {
        mapTransformerService = MapTransformerServiceImpl()
    }


    @Test
    fun secondKey_whenEmptyList_shouldReturnEmptyString() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, SECOND_KEY_METHOD, listOf<String>())
        assertThat(actual, isEmptyString())
    }

    @Test
    fun secondKey_whenListWithOneElement_shouldReturnEmptyString() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, SECOND_KEY_METHOD, listOf("1"))
        assertThat(actual, isEmptyString())
    }

    @Test
    fun secondKey_whenListWithMultipleElement_shouldSecondElement() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, SECOND_KEY_METHOD, listOf("1", "2"))
        assertThat(actual, equalTo("2"))
    }

    @Test
    fun joinKeyList_whenEmptyListAndCorrectStartIndex_shouldReturnDot() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, JOIN_KEY_LIST_METHOD, listOf<String>(), 0)
        assertThat(actual, equalTo("."))
    }

    @Test
    fun joinKeyList_whenEmptyListAndIncorrectStartIndex_shouldReturnEmptyString() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, JOIN_KEY_LIST_METHOD, listOf<String>(), 1)
        assertThat(actual, isEmptyString())
    }

    @Test
    fun joinKeyList_whenListWithOneElementAndCorrectStartIndex_shouldReturnJoinedList() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, JOIN_KEY_LIST_METHOD, listOf("1"), 0)
        assertThat(actual, equalTo(".1"))
    }

    @Test
    fun joinKeyList_whenListWithOneElementAndIndexAsListSize_shouldReturnJustDot() {
        val actual = Whitebox.invokeMethod<String>(mapTransformerService, JOIN_KEY_LIST_METHOD, listOf("1"), 1)
        assertThat(actual, equalTo("."))
    }

    @Test
    fun populateList_whenListIsEmpty_shouldAddElement() {
        val inputValue = mutableListOf<String>()
        val actual = Whitebox.invokeMethod<Boolean>(mapTransformerService, POPULATE_LIST_METHOD, inputValue, "1")
        assertAll(
            { assertThat(actual, `is`(true)) },
            { assertThat(inputValue, hasSize(1)) },
            { assertThat(inputValue[0], equalTo("1")) }
        )
    }

    @Test
    fun populateList_whenListWithIncorrectType_shouldAddElement() {
        val inputValue = mutableListOf(1)
        val actual = Whitebox.invokeMethod<Boolean>(mapTransformerService, POPULATE_LIST_METHOD, inputValue, "1")
        assertAll(
            { assertThat(actual, `is`(true)) },
            { assertThat(inputValue, hasSize(2)) },
            { assertThat(inputValue[0], equalTo(1)) }
        )
    }

    @Test
    fun flatten_keyAndValue() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to "1",
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1")) },
            { assertThat(first.second, equalTo("1")) }
        )
    }

    @Test
    fun flatten_keyAndValueMapWithOneElement() {//по два елемента
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to mapOf("2" to "4"),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.2")) },
            { assertThat(first.second, equalTo("4")) }
        )
    }

    @Test
    fun flatten_keyAndValueMapWithTwoElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to mapOf("2" to "4", "3" to "5"),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(2))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.2")) },
            { assertThat(first.second, equalTo("4")) }
        )

        val second = actualList[1]
        assertAll(
            { assertThat(second.first, equalTo("1.3")) },
            { assertThat(second.second, equalTo("5")) }
        )
    }

    @Test
    fun flatten_keyAndValueMapWithOneListWithOneElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to mapOf("2" to listOf("3")),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.2.-0")) },
            { assertThat(first.second, equalTo("3")) }
        )
    }

    @Test
    fun flatten_keyAndValueMapWithTwoListWithOneElement() {//map list with two element, map with two lists
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to mapOf("2" to listOf("3"), "4" to listOf("5")),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(2))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.2.-0")) },
            { assertThat(first.second, equalTo("3")) }
        )

        val second = actualList[1]
        assertAll(
            { assertThat(second.first, equalTo("1.4.-0")) },
            { assertThat(second.second, equalTo("5")) }
        )
    }

    @Test
    fun flatten_keyAndValueMapWithOneListWithTwoElements() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to mapOf("2" to listOf("3", "6"), "4" to listOf("5", "7")),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(4))
        var element = actualList[0]
        assertAll(
            { assertThat(element.first, equalTo("1.2.-0")) },
            { assertThat(element.second, equalTo("3")) }
        )

        element = actualList[1]
        assertAll(
            { assertThat(element.first, equalTo("1.2.-1")) },
            { assertThat(element.second, equalTo("6")) }
        )

        element = actualList[2]
        assertAll(
            { assertThat(element.first, equalTo("1.4.-0")) },
            { assertThat(element.second, equalTo("5")) }
        )

        element = actualList[3]
        assertAll(
            { assertThat(element.first, equalTo("1.4.-1")) },
            { assertThat(element.second, equalTo("7")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithOneElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf("1"),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0")) },
            { assertThat(first.second, equalTo("1")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithTwoElements() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf("1", "2"),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(2))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0")) },
            { assertThat(first.second, equalTo("1")) }
        )

        val second = actualList[1]
        assertAll(
            { assertThat(second.first, equalTo("1.-1")) },
            { assertThat(second.second, equalTo("2")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithMapWithOneElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf(mapOf("1" to "2")),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0.1")) },
            { assertThat(first.second, equalTo("2")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithListWithMapWithOneElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf(listOf(mapOf("1" to "2"))),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0.-0.1")) },
            { assertThat(first.second, equalTo("2")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithListWithMapWithTwoElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf(listOf(mapOf("1" to "2", "3" to "4"))),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(2))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0.-0.1")) },
            { assertThat(first.second, equalTo("2")) }
        )

        val second = actualList[1]
        assertAll(
            { assertThat(second.first, equalTo("1.-0.-0.3")) },
            { assertThat(second.second, equalTo("4")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithListOneElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf(listOf("1")),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(1))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0.-0")) },
            { assertThat(first.second, equalTo("1")) }
        )
    }

    @Test
    fun flatten_keyAndValueListWithListTwoElement() {
        val actual = Whitebox.invokeMethod<Stream<Pair<String, String>>>(
            mapTransformerService,
            FLATTEN_METHOD,
            "1" to listOf(listOf("1", "2")),
            "."
        )

        val actualList = actual.collect(Collectors.toList())

        assertThat(actualList, hasSize(2))
        val first = actualList[0]
        assertAll(
            { assertThat(first.first, equalTo("1.-0.-0")) },
            { assertThat(first.second, equalTo("1")) }
        )

        val second = actualList[1]
        assertAll(
            { assertThat(second.first, equalTo("1.-0.-1")) },
            { assertThat(second.second, equalTo("2")) }
        )
    }

    companion object {
        private const val FLATTEN_METHOD = "flatten"
        private const val SECOND_KEY_METHOD = "secondKey"
        private const val JOIN_KEY_LIST_METHOD = "joinKeyList"
        private const val POPULATE_LIST_METHOD = "populateList"
    }
}