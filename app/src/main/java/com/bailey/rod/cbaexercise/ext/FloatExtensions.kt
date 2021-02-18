package com.bailey.rod.cbaexercise.ext

fun Float.getDollarString(): String {
    return String.format("$%,.2f", this)
}