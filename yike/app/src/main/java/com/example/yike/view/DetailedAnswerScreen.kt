package com.example.yike

import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.sharp.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yike.data.Answer
import com.example.yike.data.AnswerData
import com.example.yike.data.CommentData
import com.example.yike.data.Taylor
import com.example.yike.viewModel.*

@Composable
fun DetailedScreen(navController: NavController,
                   detailedAnswerViewModel: DetailedAnswerViewModel,
                   reportViewModel: ReportViewModel
                   ) {
//    println("c111111111111111111111111111111111")
//    println(detailedAnswerViewModel.answerId)
//    println(detailedAnswerViewModel.questionId)
    val questionId = detailedAnswerViewModel.questionId
    val answererId = detailedAnswerViewModel.answerId
    detailedAnswerViewModel.selectQuesAnswer(detailedAnswerViewModel.answerId,detailedAnswerViewModel.questionId)
    val quesAnswerInfoList = detailedAnswerViewModel.quesAnswerInfoList.observeAsState()

    val reportInfo = reportViewModel.reportInfo.observeAsState()
    var openReportDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开通过申请框

    addRejectAlterDialog(detailedAnswerViewModel,openReportDialog,quesAnswerInfoList.value,
    ) { aID,qID,rID,reason,wID ->
        reportViewModel.sendReportInfo(aID,qID,rID,reason,wID)
    }

    DetailAnswer(quesAnswerInfoList.value,questionId,answererId,navController,openReportDialog,reportViewModel)
}


//@Preview
@Composable
//fun DetailAnswer(questionAnswerInfoList:ArrayList<QuesAnswer>?,navController: NavController)
fun DetailAnswer(
    questionAnswerInfoList:QuesAnswer?,
    questionId:String,
    answererId:String,
    navController: NavController,
    openReportDialog: MutableState<Boolean>,
    reportViewModel: ReportViewModel
    ){

    Scaffold(
        topBar = {
            TopAppBar(
                title={},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()//回退
                            } //do something
                        ) {
                            Icon(Icons.Filled.ArrowBack,
                                null,
                                tint = Color(0xFFB1A8A1)
                            )
                        }
                    },
                backgroundColor = Color(0xFFFFFF),
                contentColor = Color(0xFFFFFF),
                elevation = 0.dp,
                actions = {
//                    addRejectAlterDialog(openReportDialog,questionAnswerInfoList,
//                    ) { aID,qID,rID,reason,wID ->
//                        reportViewModel.sendReportInfo(aID,qID,rID,reason,wID)
//                    }
                    IconButton(onClick = {
                        openReportDialog.value = true
                    }) {
                        Icon(
                            Icons.Filled.ErrorOutline,
                            null,
                            tint = Color(0xFFB1A8A1)
                        )
                    }

                    TextButton(onClick = {
                        val questionId = questionId
                        val questionTitle = questionAnswerInfoList?.question
//                        navController.navigate("publishAnswer_screen/${questionId}/${questionTitle}/${answererId}")
                        navController.navigate("publishAnswer_screen/${questionId}/${questionTitle}")
                    }) {
                        Text("写回答",
                            color = Color(0xFF1084E0)
                            )
                    }
                }
            )
        },
        bottomBar ={
            BottomAppBar(
                backgroundColor = Color(0xFFFDFDFD),
                contentColor = Color(0xFFFFFF),
                elevation = 0.dp,
                contentPadding = PaddingValues(start = 10.dp,end= 5.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                content = {
                    ThumbUpButton()
                    CommentButton(navController, answererId,)
                }
            )
        }
    ) {
            LazyColumn(Modifier) {
                item {
                    if (questionAnswerInfoList != null) {
                        DetailedQuestionPart(questionAnswerInfoList)
                        DetailedUserPart(questionAnswerInfoList)
                        ShowAnswer(questionAnswerInfoList)
                        Spacer(Modifier.height(20.dp))
                        Text("评论",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.height(5.dp))
                    }
                }
                if (questionAnswerInfoList != null) {
                    item(questionAnswerInfoList.comment){
                        questionAnswerInfoList.comment.reversed().forEach() {
                            CommentCard(it)
                        }
                    }
                }
                item{
                    Spacer(modifier = Modifier.padding(vertical = 30.dp))
                }
            }

    }
}


@Composable
fun DetailedUserPart(answerer:QuesAnswer){
//    val userData = Taylor
    Surface(
        modifier = Modifier
            .padding(all = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp,end = 8.dp)
        ) {
            Image(
//                painterResource(id = answerer.info.pic),
                rememberImagePainter(answerer.info.pic),//回答者头像
                contentDescription = "profile picture", //这个描述用于无障碍
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Column {
                Text(
                    text = answerer.info.name,//回答者名字
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.subtitle1 // 添加 style
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = answerer.info.intro,//回答者简介
                    style = MaterialTheme.typography.body1, // 添加 style
                )
            }
        }
    }
}

@Composable
fun DetailedQuestionPart(ques:QuesAnswer)
{
    Column() {
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
        Row {
            Box(Modifier.padding(horizontal = 4.dp))
            Surface(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    text = ques.question,//展示问题内容
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h4,
//                    modifier = Modifier.padding(start = 5.dp,end = 5.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 0.dp, end = 0.dp),
            //颜色
            color = Color.LightGray,
        )
    }
}

@Composable
fun ShowAnswer(ans: QuesAnswer){
Column() {
    Box(modifier = Modifier.padding(vertical = 2.dp))
    Text(
//        text = AnswerData.answer.answerContent,
        text = ans.answer.substringAfter('/'),//展示回答内容
        modifier = Modifier.padding(horizontal = 10.dp),
        fontSize = 20.sp,
        style = MaterialTheme.typography.body1, // 添加 style
        )
    Spacer(modifier = Modifier.height(10.dp))//展示回答日期
    Text(
        text = ans.answer.substringBefore('/'),
        modifier = Modifier.padding(horizontal = 10.dp),
        fontSize = 15.sp,
        color = Color(0xFFA5A5A5)

    )
}
}

@Composable
fun Collect() {
    var change by remember{ mutableStateOf(false) }
    var flag by remember{ mutableStateOf(false) }

    val buttonSizeVariable = remember {
        Animatable(24.dp, Dp.VectorConverter)
    }

    LaunchedEffect(change) {
        buttonSizeVariable.animateTo(if(change) 32.dp else 24.dp)
    }
    if(buttonSizeVariable.value == 32.dp) {
        change = false
    }
    IconButton(
        onClick = {
            change = true
            flag = !flag
        }
    ) {
        Icon(
            Icons.Rounded.Favorite,
            contentDescription = null,
            modifier = Modifier.size(buttonSizeVariable.value),
            tint = if(flag) Color.Red else Color.Gray
        )
    }
}

@Composable
fun CommentCard(com:CommentInfo){
    var isExpanded by remember { mutableStateOf (false) } // 创建一个能够检测卡片是否被展开的变量

    if(com==null){
        Text(
            text = "目前还没有评论噢"
        )
    }
    else {
        // 创建一个能够根据 isExpanded 变量值而改变颜色的变量
        val surfaceColor by animateColorAsState(
            targetValue = if (isExpanded) Color(0xFFCCCCCC) else MaterialTheme.colors.surface
        )

        Surface(
            shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
            elevation = 0.dp,
            modifier = Modifier
                .padding(start = 8.dp,top = 8.dp,end = 8.dp, bottom = 3.dp)
                .fillMaxWidth()
                .clickable { // 添加一个新的 Modifier 扩展方法，可以让元素具有点击的效果
                    isExpanded = !isExpanded // 编写点击的事件内容
                },
            color = surfaceColor
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp,top = 8.dp,end = 8.dp, bottom = 3.dp)
            ) {
                Image(
//                painterResource(id = R.drawable.tay),
                    rememberImagePainter(com.info.pic),//评论者头像
                    contentDescription = "profile picture", //这个描述用于无障碍
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Column {
                    Text(
                        text = com.info.name,//评论者名字
                        style = MaterialTheme.typography.subtitle2 // 添加 style
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = com.content.substringAfter('/'),//评论者内容
                        style = MaterialTheme.typography.body2, // 添加 style
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        // Composable 大小的动画效果
                        modifier = Modifier.animateContentSize()
                    )
                    Spacer(modifier = Modifier.height(10.dp))//显示评论日期
                    Text(
                        text = com.content.substringBefore('/'),
                        fontSize = 15.sp,
                        color = Color(0xFFA5A5A5)
                    )
                }
            }
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 8.dp, end = 8.dp),
                //颜色
                color = Color.LightGray,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Composable
fun ThumbUpButton(){
//    val like = remember{mutableStateOf(false)}
//    val dislike = remember{mutableStateOf(false)}
//    val change = remember{mutableStateOf(false)}
//    val check:Boolean = false

//    if(check==false){
//        change.value == false
//    }
    Surface(
        shape = RoundedCornerShape(10.dp), // 使用 MaterialTheme 自带的形状
        color = Color(0x252C7DB4),
        modifier = Modifier.padding(10.dp,10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                modifier = Modifier
                    .size(50.dp)
                    .padding(0.dp, 1.dp)
                    .clickable { },
                onClick = {},
            ) {
                Text(
                    "赞同",
                    color = Color(0xFF1084E0),
                )
            }
            Icon(
                Icons.Sharp.ArrowDropDown,
                contentDescription = "dislike",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp, 0.dp)
                    .clickable { },
                tint = Color(0xFF1084E0)
            )
        }
    }
}

//@Preview
//@Composable
//fun Thumbed(){
//    Surface(
//        shape = RoundedCornerShape(10.dp), // 使用 MaterialTheme 自带的形状
//        color = Color(0x252C7DB4),
//        modifier = Modifier.padding(10.dp,10.dp)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.End
//        ) {
//            TextButton(onClick = { } ,
//                modifier = Modifier
////                    .size(50.dp)
//                    .padding(0.dp, 1.dp)
//                    .clickable { }
//                    .align(Alignment.CenterVertically),
//            ) {
//                Text(
//                    "已赞同",
//                    color = Color(0xFF1084E0),
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun DisLiked(){
//    Surface(
//        shape = RoundedCornerShape(10.dp), // 使用 MaterialTheme 自带的形状
//        color = Color(0x252C7DB4),
//        modifier = Modifier.padding(10.dp,10.dp)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.End
//        ) {
//            TextButton(onClick = {  } ,
//                modifier = Modifier
////                    .size(50.dp)
//                    .padding(0.dp, 1.dp)
//                    .clickable { }
//                    .align(Alignment.CenterVertically),
//            ) {
//                Text(
//                    "已踩",
//                    color = Color(0xFF1084E0),
//                )
//            }
//        }
//    }
//}

@Composable
fun CommentButton(
    navController: NavController,
    answererId: String,
    ){
    IconButton(
        onClick = {
        navController.navigate("inputComment_Screen/${answererId}") },
        ) {
        Icon(
            painterResource(id = R.drawable.comment),
            contentDescription = "Comment",
            modifier = Modifier
                .size(30.dp)
                .padding(5.dp, 0.dp),
            tint = Color(0xFF1084E0)
        )

    }
}

@Composable
private fun addRejectAlterDialog(
    detailedAnswerViewModel: DetailedAnswerViewModel,
    openReportDialog: MutableState<Boolean>,
    questionAnswerInfoList:QuesAnswer?,
    clickEvent:(aID:String,qID:String,rID:String,reason:String,wID:String)-> Unit,
) {

    var textReplyContent = remember {
        NameInputState()
    }

    if (openReportDialog.value) {
        AlertDialog(
            onDismissRequest = { openReportDialog.value = false },
            title = { Text(text = "举报确认") },
            text = {
                Column() {
                    Text(
                        text = "你确定要举报该回答吗，请输入举报理由：",
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = textReplyContent.text,
                        onValueChange = { newString ->
                            textReplyContent.text = newString
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
                                "请输入举报理由",
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
                    openReportDialog.value = false
                    GlobalViewModel.getUserInfo()?.let {
                        if (questionAnswerInfoList != null) {
                            clickEvent(detailedAnswerViewModel.answerId,detailedAnswerViewModel.questionId,it.id,textReplyContent.text,questionAnswerInfoList.info.id)
                        }
                    }
                }) {
                    Text(text = "确认",
                        color = Color(0xF23F3D38),
                    )
                }
            }, dismissButton = {
                TextButton(onClick = { openReportDialog.value = false }) {
                    Text(text = "取消",
                        color = Color(0xF23F3D38),
                    )
                }
            })
    }
}





