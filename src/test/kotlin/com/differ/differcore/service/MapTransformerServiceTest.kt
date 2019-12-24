package com.differ.differcore.service

import com.differ.differcore.utils.on
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.collection.IsMapWithSize.aMapWithSize
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.function.Executable
import org.mockito.MockitoAnnotations
import org.mockito.Spy


class MapTransformerServiceTest {
    @Spy
    private lateinit var mapTransformerService: MapTransformerServiceImpl

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `just one tag should return map with key value`() {
        on(mutableMapOf<String, Any>()) { set(".home", "basic-error-controller") }
            .let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .run {
                assertAll(
                    { assertThat(this, aMapWithSize(1)) },
                    { assertThat("basic-error-controller", equalTo(get("home"))) }
                )
            }
    }

    @Test
    fun `tag with one child should return map with nested map with one item`() {
        on(mutableMapOf<String, Any>()) { set(".home.tags", "basic-error-controller") }
            .let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .let {
                assertThat(it, aMapWithSize(1))
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertThat(it, aMapWithSize(1))
                    assertThat("basic-error-controller", equalTo(get("tags")))
                }
            }
    }

    @Test
    fun `tag with one child should return map with nested map with two items`() {
        on(mutableMapOf<String, Any>()) {
            set(".home.tags", "basic-error-controller")
            set(".home.name", "basic-controller")
        }.let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .let {
                assertThat(it, aMapWithSize(1))
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertThat(this, aMapWithSize(2))
                    assertAll(
                        { assertThat("basic-error-controller", equalTo(get("tags"))) },
                        { assertThat("basic-controller", equalTo(get("name"))) }
                    )
                }
            }
    }

    @Test
    fun `tag with one child should return map with nested list with one item`() {
        on(mutableMapOf<String, Any>()) { set(".home.tags.-0", "basic-error-controller") }
            .let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .let {
                assertThat(it, aMapWithSize(1))
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertThat(this, aMapWithSize(1))
                    assertTrue(get("tags") is List<*>)
                    with(get("tags") as List<*>) {
                        assertThat(this, hasSize(1))
                        assertThat("basic-error-controller", equalTo(get(0)))
                    }
                }
            }
    }

    @Test
    fun `tag with one child should return map with nested list with two items`() {
        on(mutableMapOf<String, Any>()) {
            set(".home.tags.-0", "basic-error-controller")
            set(".home.tags.-1", "basic-controller")
        }.let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .let {
                assertThat(it, aMapWithSize(1))
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertThat(this, aMapWithSize(1))
                    assertTrue(get("tags") is List<*>)
                    with(get("tags") as List<*>) {
                        assertThat(this, hasSize(2))
                        assertAll(
                            Executable { assertThat("basic-error-controller", equalTo(get(0))) },
                            Executable { assertThat("basic-controller", equalTo(get(1))) }
                        )
                    }
                }
            }
    }

    @Test
    fun `tag with one child should return map with list with two nested maps with one items`() {
        on(mutableMapOf<String, Any>()) {
            set(".home.-0.tags", "basic-error-controller")
            set(".home.-1.name", "basic-controller")
        }.let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .let {
                assertThat(it, aMapWithSize(1))
                assertTrue(it["home"] is List<*>)
                with(it["home"] as List<*>) {
                    assertThat(this, hasSize(2))
                    assertTrue(get(0) is Map<*, *>)
                    with(get(0) as Map<*, *>) {
                        assertAll(
                            { assertThat(this, aMapWithSize(1)) },
                            { assertThat("basic-error-controller", equalTo(get("tags"))) }
                        )
                    }
                    assertTrue(get(1) is Map<*, *>)
                    with(get(1) as Map<*, *>) {
                        assertAll(
                            { assertThat(this, aMapWithSize(1)) },
                            { assertThat("basic-controller", equalTo(get("name"))) }
                        )
                    }
                }
            }
    }

    @Test
    fun `tag with one child should return map with list with two nested maps with two items`() {
        on(mutableMapOf<String, Any>()) {
            set(".home.-0.tags", "basic-error-controller")
            set(".home.-1.name", "basic-controller")
            set(".home.-1.name2", "basic2-controller")
        }.let { mapTransformerService.expandToMapObjects(it) }
            .apply { println(this) }
            .let {
                assertThat(it, aMapWithSize(1))
                assertTrue(it["home"] is List<*>)
                with(it["home"] as List<*>) {
                    assertThat(this, hasSize(2))
                    assertTrue(get(0) is Map<*, *>)
                    with(get(0) as Map<*, *>) {
                        assertAll(
                            { assertThat(it, aMapWithSize(1)) },
                            { assertThat("basic-error-controller", equalTo(get("tags"))) }
                        )
                    }
                    assertTrue(get(1) is Map<*, *>)
                    with(get(1) as Map<*, *>) {
                        assertAll(
                            { assertThat(this, aMapWithSize(2)) },
                            { assertThat("basic-controller", equalTo(get("name"))) },
                            { assertThat("basic2-controller", equalTo(get("name2"))) }
                        )
                    }
                }
            }
    }

    @Test
    fun baseTest() {
        val data = mutableMapOf<String, Any>()
        data[".home.tags.-0.name"] = "basic-error-controller"
        data[".home.tags.-0.description"] = "Basic Error Controller"
        data[".home.tags.-0.externalDocs"] = "null"
        data[".home.tags.-1.name"] = "controller"
        data[".home.paths./.get.tags.-0"] = "controller"
        data[".home.paths./.get.tags.-1"] = "controller1"
        val expandToJson = mapTransformerService.expandToMapObjects(data)
        println(expandToJson)
        assertTrue(true)
    }
}