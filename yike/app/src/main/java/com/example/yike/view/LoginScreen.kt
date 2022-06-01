package com.example.yike.view

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yike.EmailState
import com.example.yike.PasswordInputState
import com.example.yike.component.PrimaryButton
import com.example.yike.viewModel.GlobalViewModel
import com.example.yike.viewModel.LoginViewModel
import com.example.yike.viewModel.UserInfo
import kotlin.reflect.typeOf



@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    routeEvent: () -> Unit = {},
    changeEvent:()->Unit = {},
    registerEvent:()->Unit = {},//跳转到组织登录
    findBackEvent: () -> Unit = {},//跳转到找回密码界面
    adminEvent:()->Unit = {},//跳转到管理员登陆界面
) {
    val userInfo = viewModel.userInfo.observeAsState()
    LoginContent(userInfo = userInfo.value, routeEvent,changeEvent,registerEvent,findBackEvent,adminEvent) { email, password ->
        viewModel.checkLoginStatus(email, password)
    }
}

@Composable
private fun LoginContent(userInfo: UserInfo?, routeEvent: () -> Unit = {},changeEvent:()->Unit = {},registerEvent:()->Unit = {},findBackEvent: () -> Unit = {},
                         adminEvent:()->Unit = {},//跳转到管理员登陆界面
                         clickEvent: (email: String, password: String) -> Unit,
) {
    val loginStatus = userInfo?.status
    if(loginStatus == 1) {
        if (userInfo != null) {
            if(userInfo.id==""){
                Toast.makeText(LocalContext.current, "该账号尚未注册", Toast.LENGTH_SHORT).show()
            }else{
                if(userInfo.user_NAME==""){
                    Toast.makeText(LocalContext.current, "密码错误", Toast.LENGTH_SHORT).show()
                }else {
                    println("thisisuserinfo!!!!!")
                    println(userInfo)
                    GlobalViewModel.updateUserInfo(userId = userInfo.id, userName =  userInfo.user_NAME,
                        userStatus = userInfo.status, avatar = userInfo.avator, introduction = userInfo.introduction, token = userInfo.token)
                    run(routeEvent)
                }
            }
//=======
//            println(userInfo)
//            GlobalViewModel.updateUserInfo(userId = userInfo.id, userName =  userInfo.user_NAME,
//                userStatus = userInfo.status, avatar = userInfo.avator, introduction = userInfo.introduction, token = userInfo.token)
//            run(routeEvent)
//>>>>>>> master
        }
    } else {
        println(loginStatus)
    }
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val passwordInput = remember { PasswordInputState() }
        val emailInput = remember { EmailState() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 20.dp),
        ) {

//            MyExample()

            LogInHeader()

            Spacer(Modifier.height(12.dp))

            EmailInput(emailInput)

            Spacer(Modifier.height(8.dp))

            PasswordInput(passwordInput)


            TermsOfServiceLabel(registerEvent)

            findBackLabel(findBackEvent)

            Spacer(Modifier.height(16.dp))


            LoginButton( onClick = {
                if( emailInput.isValid && passwordInput.isValid ){
                    run{
                            println(emailInput.text + passwordInput.text)
                            clickEvent(emailInput.text, passwordInput.text)
                    }
                }
            })


            ChangeLoginEntry(changeEvent,adminEvent)


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
//    PrimaryButton(
//        buttonText = "Log in",
//        onClick = onClick,
//    )
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
private fun EmailInput(emailInput: EmailState) {
    val textState = remember {
        EmailState()
    }

    OutlinedTextField(
        value = textState.text,
        onValueChange = { newString ->
            textState.text = newString
            emailInput.text = textState.text
        },
        label = {
            Text(text = "请输入个人邮箱地址")
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

@Preview
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
    onClick: () -> Unit = {},
    adminEvent:()->Unit = {},//跳转到管理员登陆界面
){
    Column() {
        Box(
//            Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(0.dp, 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "组织用户登录入口",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .paddingFromBaseline(top = 24.dp)
                        .clickable { onClick() },
                    color = Color(0xFF227AFF)
//                color = Color(0xFF172A8F),

                )
            }
        }

        Box(
//            Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(0.dp, 0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "管理员登录入口",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .paddingFromBaseline(top = 24.dp)
                        .clickable { adminEvent() },
                    color = Color(0xFF227AFF)
//                color = Color(0xFF172A8F),

                )
            }
        }

    }
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

@Composable
fun findBackLabel(
    findBackEvent: () -> Unit = {}
) {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color(0xFFC7C7C7),
                fontSize = 16.sp
            )
        ){
            append("忘记密码？")
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
            append("找回密码")
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
                run(findBackEvent)
            }
        },
    )
}