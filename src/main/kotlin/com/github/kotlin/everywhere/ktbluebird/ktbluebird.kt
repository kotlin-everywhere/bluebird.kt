package com.github.kotlin.everywhere.ktbluebird


@JsModule("bluebird")
external class Bluebird<T>(body: (resolve: (T) -> Unit, reject: (e: Exception) -> Unit) -> Unit) {
    fun <U : Any> then(body: (T) -> U): Bluebird<U>
    @JsName("then")
    fun <U : Bluebird<V>, V : Any> andThen(body: (T) -> U): Bluebird<V>

    fun catch(body: (e: dynamic) -> Unit): Bluebird<T>
    fun finally(body: () -> Unit): Bluebird<T>
}
