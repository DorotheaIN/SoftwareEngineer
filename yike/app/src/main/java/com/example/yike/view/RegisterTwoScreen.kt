package com.example.yike.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yike.*
import com.example.yike.viewModel.GetPersonRegisterViewModel
import com.example.yike.viewModel.GlobalViewModel
import com.example.yike.viewModel.VerifyCodeViewModel

@Composable
fun RegisterTwoScreen(
    navController: NavController,
    getPersonRegisterViewModel: GetPersonRegisterViewModel,
    verifyCodeViewModel: VerifyCodeViewModel
    ) {
    println("跳转成功")
    println("code = " + GlobalViewModel.getVerifyCode())

    var openDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开通过提示框

    val getPersonRegisterInfo = getPersonRegisterViewModel.personRegisterInfo.observeAsState()

    val inputVerifyCode = verifyCodeViewModel.inputVerifyCode.observeAsState()


    alterDialog(openDialog)

        RegisterTwoScreenContent(getPersonRegisterInfo.value,navController, GlobalViewModel.getEmail().toString(),inputVerifyCode.value,openDialog,
        {
                email,name,password -> getPersonRegisterViewModel.checkPersonRegisterStatus(email,name,password)
        },
        {
                inputCode ->  verifyCodeViewModel.verifyCode(inputCode)
        }

    )


}


@Composable
fun RegisterTwoScreenContent(
    registerResult: String?,
    navController: NavController,
    email:String,
//    verifyCode:String?,
    isSuccess:String?,
    openDialog: MutableState<Boolean>,
    clickEvent: (email:String,name:String,password:String) -> Unit,
    verifyEvent: (inputCode:String) -> Unit
    ){

    println("进入成功")

    val nameInput = remember { NameInputState() }
    val passwordInput = remember { PasswordInputState() }
    val rePasswordInput = remember{RePasswordInputState()}
    val verifyCodeInput = remember { VerifyCodeInputState() }

    GlobalViewModel.updatePassWord(passwordInput.text)

    if(registerResult=="success"){
        println("2222222isSuccess = $isSuccess")
//        clickEvent(email,nameInput.text,passwordInput.text)
        GlobalViewModel.updateUserInfo(email,nameInput.text,1,"","快填写简介吧","")
        navController.navigate("login")
    }else if(registerResult=="wrong")
    {
        println("2222222isSuccess = $isSuccess")
        openDialog.value = true
    }


//    if(registerResult == "success"){
//
//    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xB2806FA0), Color(0xE14256C4)),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            ),
    ) {
        Box(
            Modifier.align(Alignment.TopStart)
        ){
            RegisterTwoTable(navController)
        }
        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 100.dp)
            ) {

                Spacer(Modifier.height(50.dp))
                RegistDescript()
                Spacer(Modifier.height(30.dp))
                TextName(nameInput)
                Spacer(Modifier.height(10.dp))
                TextCode(passwordInput)
                Spacer(Modifier.height(10.dp))
                ReTextCode(rePasswordInput)
                Spacer(Modifier.height(10.dp))
                TextVerify(verifyCodeInput)
                Spacer(Modifier.height(10.dp))
                RegisterButton(
                    onClick = {
                        if(passwordInput.isValid && nameInput.isValid && verifyCodeInput.isValid && verifyCodeInput.text == GlobalViewModel.getVerifyCode()&&rePasswordInput.isValid){
                            clickEvent(email,nameInput.text,passwordInput.text)
                            println("isSuccess = $isSuccess")
                        }
                        else{
                            openDialog.value = true
                        }
                    }
                )

            }
        }
    }

}

@Composable
fun RegisterTwoTable(navController: NavController){
    TextButton(onClick = {
        navController.navigate("personRegister_screen")
    }) {
        Text(
            text = "上一步",
            fontSize =20.sp,
            color = Color(0xFFFFFFFF),
            style = MaterialTheme.typography.button,
            modifier = Modifier.padding(start = 8.dp,end = 8.dp,top = 8.dp)
        )
    }
}

@Composable
fun TextName(nameInput:NameInputState){
    val textName = remember {
        NameInputState()
    }
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0x51E4DFDB),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = textName.text,
            onValueChange = {newString ->
                textName.text = newString
                nameInput.text = textName.text
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0D0D0E),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFF045DA0),
            ),
            maxLines = 1,
            placeholder = { Text("请输入名称",
                modifier = Modifier
//                    .padding(start = 115.dp, end = 50.dp)
                    .fillMaxWidth(),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            ) },
            shape = RoundedCornerShape(30.dp)
        )
    }
}

@Composable
fun TextCode(passwordInput:PasswordInputState){
    val textCode = remember {
        PasswordInputState()
    }
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0x51E4DFDB),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = textCode.text,
            onValueChange = {newText ->
                textCode.text = newText
                passwordInput.text = textCode.text
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0D0D0E),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFF045DA0),
            ),
            maxLines = 1,
            placeholder = { Text("请输入密码",
                modifier = Modifier
//                    .padding(start = 115.dp, end = 50.dp)
                    .fillMaxWidth(),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            ) },
            shape = RoundedCornerShape(30.dp),
            visualTransformation = if (textCode.shouldHidePassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                Crossfade(targetState = textCode.shouldHidePassword) { hidePassword ->
                    if (hidePassword) {
                        PasswordVisabilityIcon(
                            iconToUse = Icons.Default.VisibilityOff,
                            textState = textCode
                        )
                    } else {
                        PasswordVisabilityIcon(
                            iconToUse = Icons.Default.Visibility,
                            textState = textCode
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun ReTextCode(passwordInput:RePasswordInputState){
    val reTextCode = remember {
        RePasswordInputState()
    }
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0x51E4DFDB),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = reTextCode.text,
            onValueChange = {newText ->
                reTextCode.text = newText
                passwordInput.text = reTextCode.text
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0D0D0E),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFF045DA0),
            ),
            maxLines = 1,
            placeholder = { Text("请再次输入密码确认",
                modifier = Modifier
//                    .padding(start = 115.dp, end = 50.dp)
                    .fillMaxWidth(),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            ) },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier.onFocusChanged {
                val isFocused = it.isFocused
                reTextCode.onFocusChange(isFocused)

                if (!isFocused) {
                    reTextCode.enableShowErrors()
                }
            },
            isError = reTextCode.showErrors,
            visualTransformation = if (reTextCode.shouldHidePassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                Crossfade(targetState = reTextCode.shouldHidePassword) { hidePassword ->
                    if (hidePassword) {
                        RePasswordVisabilityIcon(
                            iconToUse = Icons.Default.VisibilityOff,
                            textState = reTextCode
                        )
                    } else {
                        RePasswordVisabilityIcon(
                            iconToUse = Icons.Default.Visibility,
                            textState = reTextCode
                        )
                    }
                }
            },


        )

    }
    reTextCode.getError()?.let { errorMessage ->
        TextFieldError(textError = errorMessage)
    }
}

@Composable
fun TextVerify(verifyCodeInput:VerifyCodeInputState){
    val textVerifyCode = remember {
        VerifyCodeInputState()
    }
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0x51E4DFDB),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = textVerifyCode.text,
            onValueChange = {newText ->
                textVerifyCode.text = newText
                verifyCodeInput.text = textVerifyCode.text
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFF0D0D0E),
                backgroundColor = Color.Transparent,
                cursorColor = Color(0xFF045DA0),
            ),
            maxLines = 1,
            placeholder = { Text("请输入邮箱验证码",
                modifier = Modifier
//                    .padding(start = 95.dp, end = 50.dp)
                    .fillMaxWidth(),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center
            ) },
            shape = RoundedCornerShape(30.dp)
        )
    }
}

@Composable
fun RegisterButton(
    onClick: () -> Unit
){
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color(0xFFFFFFFF),
        modifier = Modifier
            .size(width = 700.dp, height = 55.dp)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {
        Text("点击注册",
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            style =MaterialTheme.typography.button,
            color = Color(0xFF0D0D0E),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun alterDialog(
    openDialog: MutableState<Boolean>,
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "错误提醒") },
            text = {
                Text(
                    text = "输入验证码有误",
                    style = MaterialTheme.typography.body1
                )
            }, confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text(text = "确认",
                        color = Color(0xF23F3D38),
                    )
                }
            }, dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "取消",
                        color = Color(0xF23F3D38),
                    )
                }
            })
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
private fun RePasswordVisabilityIcon(
    iconToUse: ImageVector,
    textState: RePasswordInputState
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
private fun TextFieldError(textError: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp,horizontal = 30.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}
