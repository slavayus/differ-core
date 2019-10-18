package com.differ.differcore.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.mockito.InjectMocks

class DiffServiceTest {
    @InjectMocks
    private val diffService = DiffService()

    @Test
    fun `just one tag should return map with key value`() {
        mutableMapOf<String, Any>().apply { set(".home", "basic-error-controller") }
            .let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertAll(
                    Executable { assertEquals(1, it.size) },
                    Executable { assertEquals("basic-error-controller", it["home"]) }
                )
            }
    }

    @Test
    fun `tag with one child should return map with nested map with one item`() {
        mutableMapOf<String, Any>().apply { set(".home.tags", "basic-error-controller") }
            .let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertEquals(1, it.size)
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertEquals(1, size)
                    assertEquals("basic-error-controller", get("tags"))
                }
            }
    }

    @Test
    fun `tag with one child should return map with nested map with two items`() {
        mutableMapOf<String, Any>().apply {
            set(".home.tags", "basic-error-controller")
            set(".home.name", "basic-controller")
        }.let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertEquals(1, it.size)
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertEquals(2, size)
                    assertAll(
                        Executable { assertEquals("basic-error-controller", get("tags")) },
                        Executable { assertEquals("basic-controller", get("name")) }
                    )
                }
            }
    }

    @Test
    fun `tag with one child should return map with nested list with one item`() {
        mutableMapOf<String, Any>().apply { set(".home.tags.-0", "basic-error-controller") }
            .let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertEquals(1, it.size)
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertEquals(1, size)
                    assertTrue(get("tags") is List<*>)
                    (get("tags") as List<*>).let { tagList ->
                        assertEquals(1, tagList.size)
                        assertEquals("basic-error-controller", tagList[0])
                    }
                }
            }
    }

    @Test
    fun `tag with one child should return map with nested list with two items`() {
        mutableMapOf<String, Any>().apply {
            set(".home.tags.-0", "basic-error-controller")
            set(".home.tags.-1", "basic-controller")
        }.let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertEquals(1, it.size)
                assertTrue(it["home"] is Map<*, *>)
                with(it["home"] as Map<*, *>) {
                    assertEquals(1, size)
                    assertTrue(get("tags") is List<*>)
                    with(get("tags") as List<*>) {
                        assertEquals(2, size)
                        assertAll(
                            Executable { assertEquals("basic-error-controller", get(0)) },
                            Executable { assertEquals("basic-controller", get(1)) }
                        )
                    }
                }
            }

//        data[".home.tags.-0.name"] = "basic-error-controller"
//        data[".home.tags.-0.description"] = "Basic Error Controller"
//        data[".home.tags.-0.externalDocs"] = "null"
//        data[".home.tags.-1.name"] = "controller"
//        data[".home.paths./.get.tags.-0"] = "controller"
//        data[".home.paths./.get.tags.-1"] = "controller1"
    }

    @Test
    fun `tag with one child should return map with list with two nested maps with one items`() {
        mutableMapOf<String, Any>().apply {
            set(".home.-0.tags", "basic-error-controller")
            set(".home.-1.name", "basic-controller")
        }.let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertEquals(1, it.size)
                assertTrue(it["home"] is List<*>)
                with(it["home"] as List<*>) {
                    assertEquals(2, size)
                    assertTrue(get(0) is Map<*, *>)
                    with(get(0) as Map<*, *>) {
                        assertAll(
                            Executable { assertEquals(1, size) },
                            Executable { assertEquals("basic-error-controller", this["tags"]) }
                        )
                    }
                    assertTrue(get(1) is Map<*, *>)
                    with(get(1) as Map<*, *>) {
                        assertAll(
                            Executable { assertEquals(1, size) },
                            Executable { assertEquals("basic-controller", this["name"]) }
                        )
                    }
                }
            }
    }

    @Test
    fun `tag with one child should return map with list with two nested maps with two items`() {
        mutableMapOf<String, Any>().apply {
            set(".home.-0.tags", "basic-error-controller")
            set(".home.-1.name", "basic-controller")
            set(".home.-1.name2", "basic2-controller")
        }.let { diffService.expandToJson(it) }
            .apply { println(this) }
            .let {
                assertEquals(1, it.size)
                assertTrue(it["home"] is List<*>)
                with(it["home"] as List<*>) {
                    assertEquals(2, size)
                    assertTrue(get(0) is Map<*, *>)
                    with(get(0) as Map<*, *>) {
                        assertAll(
                            Executable { assertEquals(1, size) },
                            Executable { assertEquals("basic-error-controller", this["tags"]) }
                        )
                    }
                    assertTrue(get(1) is Map<*, *>)
                    with(get(1) as Map<*, *>) {
                        assertAll(
                            Executable { assertEquals(2, size) },
                            Executable { assertEquals("basic-controller", this["name"]) },
                            Executable { assertEquals("basic2-controller", this["name2"]) }
                        )
                    }
                }
            }
    }


}