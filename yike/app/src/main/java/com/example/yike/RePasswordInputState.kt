package com.example.yike

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.example.yike.viewModel.GlobalViewModel

class RePasswordInputState : TextFieldState(
    validator = ::isValidPassword,
    errorFor = ::passwordValidationError
) {
    var shouldHidePassword: Boolean by mutableStateOf(true)
}

private fun passwordValidationError(password: String): String {
    return "输入密码与上次不一致."
}

private fun isValidPassword(password: String): Boolean {
    return password == GlobalViewModel.getPassWord()
}