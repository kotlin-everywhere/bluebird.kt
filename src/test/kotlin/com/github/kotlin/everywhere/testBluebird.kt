package com.github.kotlin.everywhere

import com.github.kotlin.everywhere.bluebird.Bluebird
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


external private interface QUnitAssert {
    fun async(): () -> Unit
}


external private interface QUnitTest {
    val assert: QUnitAssert
    fun pushFailure(message: String, stackTrace: dynamic)
}

external private interface QUnitConfig {
    val current: QUnitTest
}

external private object QUnit {
    val config: QUnitConfig = definedExternally
}

fun <T> Bluebird<T>.assertAsync(): Bluebird<T> {
    return catch { e ->
        @Suppress("UnsafeCastFromDynamic")
        QUnit.config.current.pushFailure(e.message, e.stack)
        @Suppress("UnsafeCastFromDynamic")
        throw e
    }
            .finally(QUnit.config.current.assert.async())
}


class TestBluebird {
    @Test
    fun testPromise() {
        Bluebird<Int> { resolve, _ ->
            resolve(42)
        }
                .then {
                    assertEquals(42, it)
                }
                .assertAsync()
    }

    @Test
    fun testPromiseAsync() {
        var isCalled = false
        Bluebird<Unit> { resolve, _ ->
            isCalled = true
            resolve(Unit)
        }
                .then {
                    assertTrue(isCalled)
                }
                .assertAsync()
    }

    @Test
    fun testThen() {
        Bluebird<Int> { resolve, _ ->
            resolve(42)
        }
                .then { "$it" }
                .then {
                    assertEquals("42", it)
                }
                .assertAsync()
    }

    @Test
    fun testAndThen() {
        Bluebird<Int> { resolve, _ ->
            resolve(42)
        }
                .andThen { int ->
                    Bluebird<String> { resolve, _ ->
                        resolve("${int * 2}")
                    }
                }
                .then {
                    assertEquals("84", it)
                }
                .assertAsync()
    }
}