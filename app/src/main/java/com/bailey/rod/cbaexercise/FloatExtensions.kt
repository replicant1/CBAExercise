package com.bailey.rod.cbaexercise

fun Float.getDollarString(): String {
    return String.format("$%,.2f", this)
}