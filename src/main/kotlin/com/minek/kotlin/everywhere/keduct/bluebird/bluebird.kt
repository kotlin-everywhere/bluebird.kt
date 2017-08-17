package com.minek.kotlin.everywhere.keduct.bluebird

import kotlin.js.Promise


@JsModule("bluebird")
external class Bluebird<out T>(body: (resolve: (T) -> Unit, reject: (e: Exception) -> Unit) -> Unit) : Promise<T> {
    fun <U : Any> then(body: (T) -> U): Bluebird<U>
    @JsName("then")
    fun <U : Bluebird<V>, V : Any> andThen(body: (T) -> U): Bluebird<V>


    fun catch(body: (e: dynamic) -> Unit): Bluebird<T>
    fun finally(body: () -> Unit): Bluebird<T>

    companion object {
        fun <U : Any> resolve(value: U): Bluebird<U>
        fun <U : Any> resolve(promise: Promise<U>): Bluebird<U>
    }
}
