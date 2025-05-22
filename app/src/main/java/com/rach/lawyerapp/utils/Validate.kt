package com.rach.lawyerapp.utils

import android.util.Patterns

fun validEmail(email:String):Boolean{
    if (email.isEmpty()) return false
    val emailPattern = Patterns.EMAIL_ADDRESS
    return emailPattern.matcher(email).matches()
}