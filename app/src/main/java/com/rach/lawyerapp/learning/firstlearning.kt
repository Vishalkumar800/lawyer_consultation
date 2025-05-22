package com.rach.lawyerapp.learning

fun main(){
    println("Enter your name")
   val a = readlnOrNull()?.toInt()

    when(a){
        1 -> println("Tom")
        2 -> println("SomeBody")
        else -> null
    }
}