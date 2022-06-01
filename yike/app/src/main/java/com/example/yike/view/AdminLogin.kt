package com.example.yike.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yike.PasswordInputState
import com.example.yike.component.RequiredInputState
import com.example.yike.viewModel.AdminInfo
import com.example.yike.viewModel.AdminLoginViewModel
import com.example.yike.viewModel.GlobalViewModel
import com.example.yike.viewModel.Organization


@Composable
fun adminLoginScreen(
    adminLoginViewModel: AdminLoginViewModel,
    routeEvent:()->Unit = {},
    change2PerEvent:()->Unit = {},
    change2OrgEvent:()->Unit = {}
){
    val adminLoginInfo = adminLoginViewModel.adminLoginInfo.observeAsState()
    loginContent(adminLoginInfo.value, routeEvent,
        { id, password ->
            adminLoginViewModel.inputAdminLogin(id.toString(),password)
        },
        change2PerEvent,
        change2OrgEvent
    )
}

@Composable
private fun loginContent(
    adminInfo: AdminInfo?,
    routeEvent:()->Unit = {},
    clickEvent:(id:Int,password:String) -> Unit,
    change2PerEvent:()->Unit = {},
    change2OrgEvent:()->Unit = {}
){
    if(adminInfo != null) {
        println(adminInfo)
        GlobalViewModel.updateAdminInfo(adminInfo)
        run(routeEvent)
    } else {
        println(adminInfo)
    }
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val passwordInput = remember { PasswordInputState() }
        val idInput = remember { RequiredInputState() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 20.dp),
        ) {

//            MyExample()

            LogInHeader()

            Spacer(Modifier.height(12.dp))

            IdInput(idInput)

            Spacer(Modifier.height(8.dp))

            PasswordInput(passwordInput)


//            TermsOfServiceLabel(registerEvent)

            Spacer(Modifier.height(16.dp))


            LoginButton( onClick = {
                if( idInput.isValid && passwordInput.isValid ){
                    run{
                        println(idInput.text + passwordInput.text)
                        clickEvent(idInput.text.toInt(), passwordInput.text)
                    }
                }
            })

            ChangeLoginEntry(change2PerEvent,change2OrgEvent)
        }
    }
}


@Composable
private fun LoginButton( onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xD83D59FC)
        ),
        modifier = Modifier.fillMaxWidth(0.92f)
    ) {
        Text(
            "登录",
            color = Color.White,
            style = TextStyle(
//                fontWeight = FontWeight.SemiBold, //设置字体粗细
                fontSize = 20.sp,
                letterSpacing = 25.sp
            )
        )
    }
}


@Composable
private fun PasswordInput(passwordInput: PasswordInputState) {
    val textState = remember {
        PasswordInputState()
    }
    OutlinedTextField(
        value = textState.text,
        onValueChange = { newText ->
            textState.text = newText
            passwordInput.text = textState.text
        },
        label = {
            Text(text = "请输入密码")
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
private fun TermsOfServiceLabel(
    registerEvent: () -> Unit = {}
) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color(0xFFC7C7C7),
                fontSize = 16.sp
            )
        ){
            append("还没有账号？")
        }
        pushStringAnnotation(
            tag = "tag",
            annotation = "转到注册界面"
        )
        withStyle(
            style = SpanStyle(
                color = Color(0xFF227AFF),
                fontSize = 16.sp
            )
        ) {
            append("马上注册")
        }
        pop()
    }
    ClickableText(
        text = text,
        modifier = Modifier
            .paddingFromBaseline(top = 24.dp),
        onClick = { offset ->
            text.getStringAnnotations(
                tag = "tag", start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                run(registerEvent)
            }
        },
    )
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
private fun IdInput(idInput: RequiredInputState) {
    val textState = remember {
        RequiredInputState()
    }
    OutlinedTextField(
        value = textState.text,
        onValueChange = { newString ->
            textState.text = newString
            idInput.text = textState.text
        },
        label = {
            Text(text = "请输入管理员ID")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
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
        text = "登录",
        style = MaterialTheme.typography.h1,
        letterSpacing = 8.sp,
        modifier = Modifier
            .paddingFromBaseline(
                top = 184.dp,
                bottom = 16.dp,
            )
    )
}

@Composable
private fun ChangeLoginEntry(
    change2PerEvent: () -> Unit = {},
    change2OrgEvent: () -> Unit = {}
){
    Column() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp),
              contentAlignment = Alignment.BottomCenter,
        ) {
            Text(
                text = "个人用户登录入口",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .paddingFromBaseline(top = 24.dp)
                    .clickable { change2PerEvent() },
                color = Color(0xFF227AFF)
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
//                        .align(Alignment.BottomCenter)
                .padding(0.dp, 20.dp),
                contentAlignment = Alignment.BottomCenter,
        ) {
            Text(
                text = "官方组织用户登录入口",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .paddingFromBaseline(top = 24.dp)
                    .clickable { change2OrgEvent() },
                color = Color(0xFF227AFF)
            )
        }

    }
}