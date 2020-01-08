package com.differ.differcore.utils

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class ExtensionsKtTest {

    @Test
    fun isInt() =
        assertAll(
            { assertThat("123".isInt(), `is`(true)) },
            { assertThat("-123".isInt(), `is`(true)) },
            { assertThat("-123.0".isInt(), `is`(false)) },
            { assertThat("0".isInt(), `is`(true)) },
            { assertThat("".isInt(), `is`(false)) },
            { assertThat("a".isInt(), `is`(false)) },
            { assertThat("1a".isInt(), `is`(false)) },
            { assertThat("a2".isInt(), `is`(false)) }
        )

    @Test
    fun isNegativeNumber() =
        assertAll(
            { assertThat("123".isNegativeNumber(), `is`(false)) },
            { assertThat("-123".isNegativeNumber(), `is`(true)) },
            { assertThat("-123.0".isNegativeNumber(), `is`(false)) },
            { assertThat("0".isNegativeNumber(), `is`(true)) },
            { assertThat("".isNegativeNumber(), `is`(false)) },
            { assertThat("-".isNegativeNumber(), `is`(false)) },
            { assertThat("-a".isNegativeNumber(), `is`(false)) },
            { assertThat("-1a".isNegativeNumber(), `is`(false)) },
            { assertThat("-a2".isNegativeNumber(), `is`(false)) }
        )

    @Test
    fun on() {
        var actual = on(StringBuilder()) {
            append("4")
        }
        assertThat("4", `is`(actual.toString()))

        actual = on(StringBuilder()) {
            append("4")
            append("45")
        }
        assertThat("445", `is`(actual.toString()))
    }
}