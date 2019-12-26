package com.differ.differcore.service.transformer

import com.differ.differcore.service.MapTransformerServiceImpl
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Spy
import org.powermock.api.mockito.PowerMockito.doAnswer
import org.powermock.api.mockito.PowerMockito.verifyPrivate
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.stream.Stream

@RunWith(PowerMockRunner::class)
@PrepareForTest(MapTransformerServiceImpl::class)
class MapTransformerServiceFlattenTest4 {

    @Spy
    private var spy = MapTransformerServiceImpl()

    @Test
    fun flattenMap_keyAndValue() {
        doAnswer { Stream.of("1" to "2") }
            .`when`(spy, FLATTEN_METHOD, "1" to "2", ".")

        val actualResult = spy.flattenMap(mapOf("1" to "2"))

        val expectedResult: Map<String, Any?> = mapOf(".1" to "2")
        assertThat(actualResult, equalTo(expectedResult))

        verifyPrivate(spy, times(1)).invoke(FLATTEN_METHOD, "1" to "2", ".")
    }

    @Test
    fun flattenMap_keyAndValueMapWithOneElement() {
        doAnswer { Stream.of("1.2" to "4") }
            .`when`(spy, FLATTEN_METHOD, "1" to mapOf("2" to "4"), ".")

        val actualResult = spy.flattenMap(mapOf("1" to mapOf("2" to "4")))

        val expectedResult: Map<String, Any?> = mapOf(".1.2" to "4")
        assertThat(actualResult, equalTo(expectedResult))

        verifyPrivate(spy, times(1)).invoke(FLATTEN_METHOD, "1" to mapOf("2" to "4"), ".")
    }

    companion object {
        private const val FLATTEN_METHOD = "flatten"
    }
}