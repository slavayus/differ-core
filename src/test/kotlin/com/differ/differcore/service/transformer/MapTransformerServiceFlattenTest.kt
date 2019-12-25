package com.differ.differcore.service.transformer

import com.differ.differcore.service.MapTransformerServiceImpl
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.util.stream.Collectors
import java.util.stream.Stream


@RunWith(PowerMockRunner::class)
class MapTransformerServiceFlattenTest {

    @Spy
    private lateinit var mapTransformerService: MapTransformerServiceImpl

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

//    @Test
//    @PrepareForTest(MapTransformerServiceImpl::class)
//    fun flattenTest() {
//        mapTransformerService = MapTransformerServiceImpl()
//
//        val spy = PowerMockito.spy(mapTransformerService)
//        PowerMockito.doReturn("Test").`when`(spy, FLATTEN_METHOD)
//        val value = spy.flattenMap(mapOf())
//
//        assertThat(value, equalTo("Mock private method example: Test"))
//        PowerMockito.verifyPrivate(spy, Mockito.times(1)).invoke(FLATTEN_METHOD)
//    }

    @Test
    fun flatten_keyAndValue() {
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
        mapTransformerService = MapTransformerServiceImpl()

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
    }
}