package com.example.yike.view

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yike.LinkText
import com.example.yike.LinkTextData
import com.example.yike.NameInputState
import com.example.yike.R
import com.example.yike.viewModel.*

@Composable
fun AdminApplyScreen(
    adminApplyViewModel: AdminApplyViewModel,
    updateApplyViewModel: UpdateApplyViewModel,
    navController:NavController,
    change2ReportEvent:()->Unit
){
    adminApplyViewModel.init()
    val applicationInfoList = adminApplyViewModel.applicationInfoList.observeAsState()

    val changeApplyStatus = updateApplyViewModel.changeApplyStatus.observeAsState()

    val postApplyViewModel = PostApplyResultViewModel()

    val sendPostInfo = postApplyViewModel.sendPostInfo.observeAsState()

    var openApproveDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开通过申请框

    var openRejectDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开拒绝框

    var tempApplyInfo: MutableState<ApplyInfo>? = remember {
        mutableStateOf(ApplyInfo(0,0,"","","","",""))
    }//信息传到弹框

    val replyContentInput = remember { NameInputState() }//记录输入

    addApproveAlterDialog(openApproveDialog,tempApplyInfo,adminApplyViewModel,
        {
                ID,status-> updateApplyViewModel.updateApplyStatus(ID, status)
        },
        {
                content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
        }
    )

    addRejectAlterDialog(openRejectDialog,replyContentInput,tempApplyInfo,adminApplyViewModel,
        {
                ID,status-> updateApplyViewModel.updateApplyStatus(ID, status)
        },
        {
                content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
        }
    )



    AdminApplyScreenContent(tempApplyInfo,applicationInfoList.value,change2ReportEvent,openApproveDialog,openRejectDialog)

}

@Composable
fun AdminApplyScreenContent(
    tempApplyInfo: MutableState<ApplyInfo>?,
    applicationInfoList: ArrayList<ApplyInfo>?,
    change2ReportEvent:()->Unit,
    openApproveDialog: MutableState<Boolean>,
    openRejectDialog: MutableState<Boolean>,

){

//    val postApplyViewModel = PostApplyResultViewModel()
//
//    val sendPostInfo = postApplyViewModel.sendPostInfo.observeAsState()
//
//
//    var tempApplyInfo: MutableState<ApplyInfo>? = remember {
//        mutableStateOf(ApplyInfo(0,0,"","","","",""))
//    }//信息传到弹框


//    val replyContentInput = remember { NameInputState() }//记录输入


    LazyColumn(
        Modifier.background(Color(0xFFDBDBDB))
    ){
        item { top(change2ReportEvent) }
        item { adminInfo()}
        item { Split()}


        if (applicationInfoList != null) {
            for(applicationInfo in applicationInfoList){
                if (applicationInfo.status == 1 ||applicationInfo.status == -1) {
                    println(applicationInfo.status)
                    continue
                }
                else
                {
                    println(applicationInfo.id)
                    println(applicationInfo.status)
                    item{applyInfo(openApproveDialog,openRejectDialog,applicationInfo,tempApplyInfo)}
                }
            }
        }

//        item {
//            addApproveAlterDialog(openApproveDialog,tempApplyInfo,adminApplyViewModel,
//                {
//                        ID,status-> updateApplyViewModel.updateApplyStatus(ID, status)
//                },
//                {
//                        content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
//                }
//            )
//        }
//        item {
//            addRejectAlterDialog(openRejectDialog,replyContentInput,tempApplyInfo,adminApplyViewModel,
//                {
//                        ID,status-> updateApplyViewModel.updateApplyStatus(ID, status)
//                },
//                {
//                        content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
//                }
//            )
//        }

    }
}

@Composable
private fun top(
    change2ReportEvent:()->Unit
){
    TopAppBar(
        modifier =Modifier,
        backgroundColor = Color(0xB2D3D2D6),
        title = {
            Text(text = "管理员界面",
            color = Color(0xF23F3D38)
            )
        },
        actions = {
            var menuExpanded by remember { mutableStateOf(false) }

            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
                },
            ) {
                DropdownMenuItem(onClick = change2ReportEvent) {
                    Text("处理举报")
                }
            }
        },
    )
}

@Composable
fun adminInfo(){
    Surface(
        modifier = Modifier
            .padding(bottom = 7.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF3E5151), Color(0xFFDECBA4)),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
//            com.example.yike.LoginOutItem(navController)
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .size(550.dp, 85.dp)
                    .padding(15.dp, 0.dp)
            ){
                val getImg = GlobalViewModel.getAdminInfo()?.avator
//                val getIntro = GlobalViewModel.getUserInfo()?.introduction
//                val img = if(getImg == ""){
//                    "http://101.132.138.14/files/user/"+ (1..199).random().toString()+".jpg"
//                }else{
//                    getImg
//                }
//                val intro = if(getIntro == null){
//                    "还未填写简介"
//                }else{
//                    getIntro
//                }
                val img = if(getImg == ""){
                    R.drawable.admin
                }else{
                    getImg
                }
                Row(modifier = Modifier.padding(all = 8.dp)) {
                    Image(
                        rememberImagePainter(img),//管理员头像
                        contentDescription = null,
                        modifier = Modifier
                            .padding(0.dp, 5.dp)
                            .size(55.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
//                .border(1.5.dp, MaterialTheme.colors.secondary, RoundedCornerShape(7.dp))
                    )
                    Spacer(modifier = Modifier.width(15.dp))

                    Column (modifier = Modifier.size(300.dp,65.dp)){
                        GlobalViewModel.getAdminInfo()?.let {
                            Text(
                                text = it.username,//管理员名称
                                color = Color.White,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "我是一个管理员",//管理员下面小字介绍
                            color = Color(0x7EFFFFFF),
                            style = MaterialTheme.typography.caption
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Add,null)
                    }
                }
            }
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 24.dp, end = 24.dp),
                //颜色
                color = Color(0x2CFFFFFF),
            )
            Spacer(Modifier.height(60.dp))
        }
    }
}

@Preview
@Composable
private fun Split(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFADA996), Color(0xFFDBDBDB)
                    ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(0.dp, 10.dp)
        ) {
            Text(
                "组织申请信息",
                color = Color(0xF23F3D38),
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun applyInfo(
    openApproveDialog: MutableState<Boolean>,
    openRejectDialog: MutableState<Boolean>,
    applyInfo: ApplyInfo,
    tempApplyInfo: MutableState<ApplyInfo>?
    ) {
//    var openDialog by remember { mutableStateOf<Boolean>(false) }

    var isExpanded by remember { mutableStateOf(false) } // 创建一个能够检测卡片是否被展开的变量

    var isIntroExpanded by remember { mutableStateOf(false) } // 创建一个能够检测组织简介是否被展开的变量
    Surface(
        Modifier.background(Color(0xFFDBDBDB))
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 7.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFADA996), Color(0xFFF2F2F2), Color(0xFFDBDBDB), Color(
                                0xFFEAEAEA
                            )
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
                .padding(15.dp, 0.dp)
        ) {

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(550.dp, 85.dp)
//                    .padding(15.dp, 0.dp)
            ) {
                Row() {
                    Image(
                        rememberImagePainter(applyInfo.avator),//申请组织头像
                        contentDescription = null,
                        modifier = Modifier
                            .padding(0.dp, 5.dp)
                            .size(55.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
                    )
                    Spacer(Modifier.width(15.dp))
                    Box(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Row() {

                            Text(
                                "申请组织名称:",
                                color = Color(0xF23F3D38),
                                style = MaterialTheme.typography.body1,
                                fontSize = 18.sp
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = applyInfo.username,//组织名称
                                color = Color(0xD53D3A3A),
                                style = MaterialTheme.typography.body1,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .clickable { // 添加一个新的 Modifier 扩展方法，可以让元素具有点击的效果
                                        isExpanded = !isExpanded // 编写点击的事件内容
                                    }
                                    .animateContentSize(),
                                maxLines = if (isExpanded) Int.MAX_VALUE else 1
                            )
                        }
                    }
                }

            }

            Box(
            ) {
                Column() {
                    Text(
                        text = "申请组织简介：",
                        color = Color(0xF23F3D38),
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(Modifier.padding(horizontal = 5.dp)) {
                        Text(
                            text = applyInfo.introduction,
                            color = Color(0xD53D3A3A),
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.clickable {
                                isIntroExpanded = !isIntroExpanded
                            },
                            maxLines = if (isIntroExpanded) Int.MAX_VALUE else 5
                        )
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            Box(){
                Column() {
                    Text(
                        text = "申请组织提交材料：",
                        color = Color(0xF23F3D38),
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(Modifier.padding(horizontal = 5.dp)) {
//                        Text(
//                            text = applyInfo.certification,
//                            color = Color(0xD53D3A3A),
//                            style = MaterialTheme.typography.body2,
//                            modifier = Modifier.clickable {
//                                isIntroExpanded = !isIntroExpanded
//                            },
//                            maxLines = if (isIntroExpanded) Int.MAX_VALUE else 1
//                        )
//                        LinkText(
//                            linkTextData = listOf(
//                                LinkTextData(
//                                    text = applyInfo.certification,
//                                    tag = "申请材料",
//                                    annotation = applyInfo.certification,
//                                    onClick = {
//                                        Log.d("Link text", "${it.tag} ${it.item}")
//                                    },
//                                )
//                            ),
//                        )
                        val annotatedLinkString: AnnotatedString = buildAnnotatedString {
                            val str = applyInfo.certification
//                            val startIndex = str.indexOf("link")
//                            val endIndex = startIndex + 4
                            val startIndex = str.indexOf(applyInfo.certification)
                            val endIndex = startIndex + str.length
                            append(str)
                            addStyle(
                                style = SpanStyle(
                                    color = Color(0xff64B5F6),
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp,
                                    fontWeight = FontWeight.Normal,
                                    textDecoration = TextDecoration.Underline
                                ), start = startIndex ,end = endIndex
                            )
                            // attach a string annotation that stores a URL to the text "link"
                            addStringAnnotation(
                                tag = "URL",
                                annotation = applyInfo.certification,
                                start = startIndex,
                                end = endIndex
                            )
                        }
// UriHandler parse and opens URI inside AnnotatedString Item in Browse
                        val uriHandler = LocalUriHandler.current
// ? Clickable text returns position of text that is clicked in onClick callback
                        ClickableText(
                            modifier = Modifier
//                                .padding(16.dp)
                                .fillMaxWidth(),
                            text = annotatedLinkString,
                            style = MaterialTheme.typography.body2,
                            onClick = {
                                annotatedLinkString
                                    .getStringAnnotations("URL", it, it)
                                    .firstOrNull()?.let { stringAnnotation ->
                                        uriHandler.openUri(stringAnnotation.item)
                                    }
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(15.dp))

            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFADA996), Color(0xFFF2F2F2), Color(0xFFDBDBDB), Color(
                        0xFFEAEAEA
                    )
                ),
                start = Offset(0f, Float.POSITIVE_INFINITY),
                end = Offset(Float.POSITIVE_INFINITY, 0f)
            )

            Box(Modifier.align(CenterHorizontally)) {
                Row() {
                    Box() {
                        TextButton(
                            onClick = {
                                tempApplyInfo?.value=applyInfo
                                openApproveDialog.value = true
                                      },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .background(
                                    brush = gradientBrush,
                                    shape = RoundedCornerShape(10.dp),
                                    alpha = 0.2f
                                )
                        ) {
                            Box(
                                modifier = Modifier.align(CenterVertically)
                            ) {
                                Text(
                                    text = "通过申请",
                                    color = Color(0xF23F3D38),
                                    style = MaterialTheme.typography.button,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(100),
                                )
                            }

                        }

                    }

                    Spacer(modifier = Modifier.width(90.dp))

                    Box() {
                        TextButton(
                            onClick = {
                                tempApplyInfo?.value=applyInfo
                                openRejectDialog.value = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .background(
                                    brush = gradientBrush,
                                    shape = RoundedCornerShape(10.dp),
                                    alpha = 0.2f
                                )
                        ) {
                            Box(
                                modifier = Modifier.align(CenterVertically)
                            ) {
                                Text(
                                    text = "拒绝申请",
                                    color = Color(0xF23F3D38),
                                    style = MaterialTheme.typography.button,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(100),
                                )
                            }

                        }

                    }
                }
            }

        }
    }
}

@Composable
private fun addApproveAlterDialog(
    openApproveDialog: MutableState<Boolean>,
    tempApplyInfo:MutableState<ApplyInfo>?,
    adminApplyViewModel:AdminApplyViewModel,
    clickEvent:(ID:String,status:String)-> Unit,
    postEmailEvent:(content:String,title: String,to: String)->Unit
    ) {
    if (openApproveDialog.value) {
        AlertDialog(
            onDismissRequest = { openApproveDialog.value = false },
            title = { Text(text = "通过申请确认") },
            text = {
                Text(
                    text = "你确定通过该组织的申请吗",
                    style = MaterialTheme.typography.body1
                )
            }, confirmButton = {
                TextButton(onClick = {
                    openApproveDialog.value = false
                    clickEvent(tempApplyInfo?.value?.id.toString(),"1")
                    tempApplyInfo?.value?.let {
                        postEmailEvent("您已通过认证，快加入一刻吧！","一刻App通知：您已通过认证",
                            it.email)
                    }
                    tempApplyInfo?.value?.let { adminApplyViewModel.changeInfoStatus(it.id,1) }
                }) {
                    Text(text = "确认",
                        color = Color(0xF23F3D38),
                        )
                }
            }, dismissButton = {
                TextButton(onClick = { openApproveDialog.value = false }) {
                    Text(text = "取消",
                        color = Color(0xF23F3D38),
                        )
                }
            })
    }
}

@Composable
private fun addRejectAlterDialog(
    openRejectDialog: MutableState<Boolean>,
    replyContentInput:NameInputState,
    tempApplyInfo: MutableState<ApplyInfo>?,
    adminApplyViewModel:AdminApplyViewModel,
    clickEvent:(ID:String,status:String)-> Unit,
    postEmailEvent:(content:String,title: String,to: String)->Unit
) {

    var textReplyContent = remember {
        NameInputState()
    }

    if (openRejectDialog.value) {
        AlertDialog(
            onDismissRequest = { openRejectDialog.value = false },
            title = { Text(text = "拒绝申请确认") },
            text = {
                Column() {
                    Text(
                        text = "你确定拒绝该组织的申请吗，请输入理由",
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = textReplyContent.text,
                        onValueChange = { newString ->
                            textReplyContent.text = newString
                            replyContentInput.text = textReplyContent.text
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color(0xFF0D0D0E),
                            backgroundColor = Color.Transparent,
                            cursorColor = Color(0xFF045DA0),
                            focusedIndicatorColor = Color.Transparent,

                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp
                        ),
                        placeholder = {
                            Text(
                                "给提出的问题一些补充吧",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = Color(0xFFBBB4B4),
                                fontSize = 16.sp,
                            )
                        },
                        modifier = Modifier.border(
                            1.dp,
                            Color(0xFFBBB4B4),
                            shape = RoundedCornerShape(0.dp)
                        ),
                        maxLines = 3
                    )
                }
            }, confirmButton = {
                TextButton(onClick = {
                    openRejectDialog.value = false
                    clickEvent(tempApplyInfo?.value?.id.toString(),"-1")
                    tempApplyInfo?.value?.let {
                        postEmailEvent("拒绝理由："+textReplyContent.text,"一刻App通知：您的申请已被拒绝",
                            it.email)
                    }
                    tempApplyInfo?.value?.let { adminApplyViewModel.changeInfoStatus(it.id,-1) }
                }) {
                    Text(text = "确认",
                        color = Color(0xF23F3D38),
                    )
                }
            }, dismissButton = {
                TextButton(onClick = { openRejectDialog.value = false }) {
                    Text(text = "取消",
                        color = Color(0xF23F3D38),
                    )
                }
            })
    }
}