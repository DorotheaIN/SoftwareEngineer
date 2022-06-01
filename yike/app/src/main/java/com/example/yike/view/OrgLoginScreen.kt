package com.example.yike.view


import android.view.Gravity
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yike.EmailState
import com.example.yike.PasswordInputState
import com.example.yike.component.OrgRegisterDialog
import com.example.yike.component.PrimaryButton
import com.example.yike.component.RequiredInputState
import com.example.yike.viewModel.GlobalViewModel
import com.example.yike.viewModel.OrgLoginViewModel
import com.example.yike.viewModel.Organization

@Composable
fun OrgLoginScreen(
    viewModel: OrgLoginViewModel,
    routeEvent:()->Unit = {},
    changeEvent:()->Unit = {},
    registerEvent: () -> Unit = {},
    findBackEvent: () -> Unit = {},//跳转到找回密码界面
    adminEvent:()->Unit = {},//跳转到管理员登陆界面
){
    val orgInfo = viewModel.orgInfo.observeAsState()
    LoginContent(orgInfo = orgInfo.value, routeEvent,
        { id, password ->
            viewModel.checkLoginStatus(id,password)
        },
        registerEvent,
        findBackEvent,
        changeEvent,
        adminEvent
    )
}

@Composable
private fun LoginContent(
    orgInfo:Organization?,
    routeEvent:()->Unit = {},
    clickEvent:(id:Int,password:String) -> Unit,
    registerEvent: () -> Unit,
    findBackEvent: () -> Unit = {},
    changeEvent:()->Unit = {},
    adminEvent:()->Unit = {},//跳转到管理员登陆界面
){
//    val openDialog = remember { mutableStateOf(false) }
//    val isSuccess = remember { mutableStateOf(false) }
    if(orgInfo != null ) {
        if(orgInfo.id == -1){
            Toast.makeText(LocalContext.current, "该账户尚未注册", Toast.LENGTH_SHORT).show()
        }else {
            if(orgInfo.status == -2){
                Toast.makeText(LocalContext.current, "密码错误", Toast.LENGTH_SHORT).show()
            }else if(orgInfo.status == 0){
                Toast.makeText(LocalContext.current, "该组织还未通过审核", Toast.LENGTH_SHORT).show()
            }else if(orgInfo.status == -1){
                Toast.makeText(LocalContext.current, "组织用户申请被拒绝，详情可见邮箱", Toast.LENGTH_SHORT).show()
            }
            else {
                println(orgInfo)
                GlobalViewModel.updateOrgInfo(orgInfo)
                Toast.makeText(LocalContext.current, "登录成功", Toast.LENGTH_SHORT).show()
                run(routeEvent)
            }
        }
    } else {
        println(orgInfo)
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


            TermsOfServiceLabel(registerEvent)

            findBackLabel(findBackEvent)

            Spacer(Modifier.height(16.dp))


            LoginButton( onClick = {
                if( idInput.isValid && passwordInput.isValid ){
                    run{
                        println(idInput.text + passwordInput.text)
                        clickEvent(idInput.text.toInt(), passwordInput.text)
                    }
                }
            })
//<<<<<<< HEAD
////            OrgRegisterDialog(isSuccess, openDialog)
//            ChangeLoginEntry(changeEvent)
//=======

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
            Text(text = "请输入组织ID")
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
    onClick: () -> Unit = {},
    adminEvent:()->Unit = {},//跳转到管理员登陆界面
){
    Box(
//        Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(0.dp, 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "个人用户登录入口",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .paddingFromBaseline(top = 24.dp)
                    .clickable { onClick() },
                color = Color(0xFF227AFF)
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