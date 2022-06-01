package com.example.yike

import java.util.regex.Pattern

// Consider an email valid if there's some text before and after a "@"
private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState : TextFieldState(
    validator = ::isEmailValid,
    errorFor = ::emailValidationError
)

private fun emailValidationError(email: String): String {
    return if (email.isBlank()) {
        "请输入邮箱地址"
    } else {
        "无效邮箱地址: $email."
    }
}

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}