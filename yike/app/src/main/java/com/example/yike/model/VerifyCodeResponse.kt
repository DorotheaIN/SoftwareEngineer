package com.example.yike.model

data class VerifyCodeResponse(
    val code:Int,
    val msg:String,
    val result:String,
    val dataCount:Int,
)