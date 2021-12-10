package com.example.yike.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.yike.EmailState
import com.example.yike.PasswordInputState
import com.example.yike.component.PrimaryButton
import com.example.yike.viewModel.GlobalViewModel
import com.example.yike.viewModel.LoginViewModel
import com.example.yike.viewModel.UserInfo


@Composable
fun LoginScreen(viewModel: LoginViewModel, routeEvent: () -> Unit = {}) {
    val userInfo = viewModel.userInfo.observeAsState()
    LoginContent(userInfo = userInfo.value, routeEvent) {
        viewModel.checkLoginStatus("13", "ssg")
    }
}

@Composable
private fun LoginContent(userInfo: UserInfo?, routeEvent: () -> Unit = {}, clickEvent: () -> Unit = {}) {
    val loginStatus = userInfo?.status
    if(loginStatus == 1) {
        if (userInfo != null) {
            println(userInfo)
            GlobalViewModel.updateUserInfo(userId = userInfo.id, userName =  userInfo.user_NAME,
                userStatus = userInfo.status, avatar = userInfo.avator, introduction = userInfo.introduction)
            run(routeEvent)
        }
    } else {
        println(loginStatus)
    }
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {

//            MyExample()

            LogInHeader()

            EmailInput()

            Spacer(Modifier.height(8.dp))

            PasswordInput()

            TermsOfServiceLabel()

            Spacer(Modifier.height(16.dp))

            LoginButton( onClick =
                clickEvent
            )
        }
    }
}

@Composable
private fun LoginButton( onClick: () -> Unit = {}) {
    PrimaryButton(
        buttonText = "Log in",
        onClick = onClick
    )
}

@Composable
private fun TermsOfServiceLabel() {
    Text(
        text = "By clicking below you agree to our Terms of Use and consent to our Privacy Policy.",
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .paddingFromBaseline(top = 24.dp),
    )
}

@Composable
private fun PasswordInput() {
    val textState = remember {
        PasswordInputState()
    }

    OutlinedTextField(
        value = textState.text,
        onValueChange = { newText ->
            textState.text = newText
        },
        label = {
            Text(text = "Password (8+ characters)")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = if (textState.shouldHidePassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                val isFocused = it.isFocused
                textState.onFocusChange(isFocused)

                if (!isFocused) {
                    textState.enableShowErrors()
                }
            },
        isError = textState.showErrors,
        trailingIcon = {
            Crossfade(targetState = textState.shouldHidePassword) { hidePassword ->
                if (hidePassword) {
                    PasswordVisabilityIcon(
                        iconToUse = Icons.Default.VisibilityOff,
                        textState = textState
                    )
                } else {
                    PasswordVisabilityIcon(
                        iconToUse = Icons.Default.Visibility,
                        textState = textState
                    )
                }
            }
        }
    )

    textState.getError()?.let { errorMessage ->
        TextFieldError(textError = errorMessage)
    }
}

@Composable
private fun PasswordVisabilityIcon(
    iconToUse: ImageVector,
    textState: PasswordInputState
) {
    Icon(
        iconToUse,
        contentDescription = "Toggle Password Visibility",
        modifier = Modifier
            .clickable {
                textState.shouldHidePassword = !textState.shouldHidePassword
            },
    )
}

@Composable
private fun EmailInput() {
    val textState = remember {
        EmailState()
    }

    OutlinedTextField(
        value = textState.text,
        onValueChange = { newString ->
            textState.text = newString
        },
        label = {
            Text(text = "Email address")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { it ->
                val isFocused = it.isFocused
                textState.onFocusChange(isFocused)

                if (!isFocused) {
                    textState.enableShowErrors()
                }
            },
        isError = textState.showErrors,
    )

    textState.getError()?.let { errorMessage ->
        TextFieldError(textError = errorMessage)
    }
}

@Composable
private fun TextFieldError(textError: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}

@Composable
private fun LogInHeader() {
    Text(
        text = "Log in",
        style = MaterialTheme.typography.h1,
        modifier = Modifier
            .paddingFromBaseline(
                top = 184.dp,
                bottom = 16.dp,
            )
    )
}

//@Composable
//fun MyExample() {
//    val painter = rememberImagePainter(
//        data = "http://101.132.138.14/files/123/12/10.png",
//        builder = {
//            crossfade(true)
//        }
//    )
//
//    Box {
//        Image(
//            painter = painter,
//            contentDescription = null,
//            modifier = Modifier.size(128.dp)
//        )
//
//        when (painter.state) {
//            is ImagePainter.State.Loading -> {
//                // Display a circular progress indicator whilst loading
//                CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//            is ImagePainter.State.Error -> {
//                // If you wish to display some content if the request fails
//            }
//        }
//    }
//}

//@Preview(
//    name = "Night Mode",
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//)
//@Preview(
//    name = "Day Mode",
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//)
//@Composable
//private fun LoginScreenPreview() {
//    YikeTheme {
//        LoginScreen()
//    }
//}