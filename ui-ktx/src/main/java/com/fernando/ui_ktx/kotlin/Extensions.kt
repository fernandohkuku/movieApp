package com.fernando.ui_ktx.kotlin

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun Any?.isNull(): Boolean = this == null

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }

    return this != null
}

fun <T> castTo(anything: Any?): T {
    return anything as T
}

infix fun <T> Boolean.then(param: () -> T): T? = if (this) param() else null
