package com.example.yike.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yike.NameInputState
import com.example.yike.viewModel.*


@Composable
fun AdminReportScreen(
    adminGetReportsViewModel: AdminGetReportsViewModel,
    adminUpdateIUserViewModel: AdminUpdateIUserViewModel,
    navController:NavController
){
    adminGetReportsViewModel.init()

    val reportsList = adminGetReportsViewModel.reportsList.observeAsState()

    val changeIUserStatus = adminUpdateIUserViewModel.changeIUserStatus.observeAsState()

    var openApproveDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开通过框

    var openRejectDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开拒绝框

    var openReportDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开举报详情框

    var tempReportsInfo:MutableState<ReportInfo>?= remember {
        mutableStateOf(ReportInfo("","","","",""))
    }//信息传到弹框

    val postApplyViewModel = PostApplyResultViewModel()

    val sendPostInfo = postApplyViewModel.sendPostInfo.observeAsState()

    val detailedAnswerViewModel = tempReportsInfo?.value?.let {
        DetailedAnswerViewModel(
            it.question_ID,
            tempReportsInfo?.value.answer_ID)
    }//传入问题ID和回答ID展示回答详情

    if (detailedAnswerViewModel != null) {
        detailedAnswerViewModel.selectQuesAnswer(detailedAnswerViewModel.answerId,detailedAnswerViewModel.questionId)
    }//调用函数

    val quesAnswerInfoList= detailedAnswerViewModel?.quesAnswerInfoList?.observeAsState()

    addApproveAlterDialog(openApproveDialog, tempReportsInfo,
        clickEvent = {
                ID,status->adminUpdateIUserViewModel.updateIUserStatus(ID, status)
        },
        postEmailEvent = {
                content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
        }
    )//同意举报弹窗

    addRejectAlterDialog(openRejectDialog, tempReportsInfo,
        {
                ID,status-> adminUpdateIUserViewModel.updateIUserStatus(ID, status)
        },
        {
                content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
        }
    )//驳回举报弹窗

    if (quesAnswerInfoList != null) {
        quesAnswerInfoList.value?.let {
            addReportAlterDialog(openReportDialog,tempReportsInfo,
                it
            )
        }
    }//展示详细信息弹窗

    AdminReportScreenContent(quesAnswerInfoList,tempReportsInfo,openApproveDialog,openRejectDialog,reportsList.value,adminUpdateIUserViewModel, navController,openReportDialog)
}


@Composable
fun AdminReportScreenContent(
    quesAnswerInfoList:State<QuesAnswer?>?,
    tempReportsInfo:MutableState<ReportInfo>?,
    openApproveDialog: MutableState<Boolean>,
    openRejectDialog: MutableState<Boolean>,
    reportsList:ArrayList<ReportInfo>?,
    adminUpdateIUserViewModel: AdminUpdateIUserViewModel,
    navController:NavController,
    openReportDialog: MutableState<Boolean>
){

//    val postApplyViewModel = PostApplyResultViewModel()
//
//    val sendPostInfo = postApplyViewModel.sendPostInfo.observeAsState()

//    var tempReportsInfo:MutableState<ReportInfo>?= remember {
//        mutableStateOf(ReportInfo("","","","",""))
//    }//信息传到弹框

//    val detailedAnswerViewModel = tempReportsInfo?.value?.let {
//        DetailedAnswerViewModel(
//            it.question_ID,
//        tempReportsInfo?.value.answer_ID)
//    }

//    if (detailedAnswerViewModel != null) {
//        detailedAnswerViewModel.selectQuesAnswer(detailedAnswerViewModel.answerId,detailedAnswerViewModel.questionId)
//    }
//
//    val quesAnswerInfoList= detailedAnswerViewModel?.quesAnswerInfoList?.observeAsState()



    LazyColumn(
        Modifier.background(Color(0xFFDBDBDB))
    ){
        item { top(navController) }
        item { adminInfo()}
        item { split()}


        item(reportsList){
            if (reportsList != null) {
                reportsList.forEach {
                    if (quesAnswerInfoList != null) {

                            reportCard(openApproveDialog, openRejectDialog,openReportDialog, it,tempReportsInfo,
                            )

//                        quesAnswerInfoList.value?.let { it1 ->
//                            addReportAlterDialog(openReportDialog,tempReportsInfo,
//                                it1
//                            )
//                        }
                    }
                }
            }
        }

//        item {
//            addApproveAlterDialog(openApproveDialog, tempReportsInfo,
//                clickEvent = {
//                        ID,status->adminUpdateIUserViewModel.updateIUserStatus(ID, status)
//                },
//                postEmailEvent = {
//                        content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
//                }
//            )
//        }

//        item {
//            addRejectAlterDialog(openRejectDialog, tempReportsInfo,
//                {
//                        ID,status-> adminUpdateIUserViewModel.updateIUserStatus(ID, status)
//                },
//                {
//                        content,title,to ->postApplyViewModel.PostApplyResult(content, title, to)
//                }
//            )
//        }

//        item {
//            if (quesAnswerInfoList != null) {
//                quesAnswerInfoList.value?.let {
//                    addReportAlterDialog(openReportDialog,tempReportsInfo,
//                        it
//                    )
//                }
//            }
//        }



    }
}


@Composable
private fun top(
    navController:NavController
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
                DropdownMenuItem(onClick = {navController.popBackStack()}) {
                    Text("处理申请信息")
                }
            }
        },
    )
}

@Preview
@Composable
private fun split(){
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
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 10.dp)
        ) {
            Text(
                "用户举报信息",
                color = Color(0xF23F3D38),
                style = MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
        }
    }
}


@Composable
private fun reportCard(
    openApproveDialog: MutableState<Boolean>,
    openRejectDialog: MutableState<Boolean>,
    openReportDialog: MutableState<Boolean>,
    reportsInfo:ReportInfo,
    tempReportsInfo:MutableState<ReportInfo>?,
){

    var isExpanded by remember { mutableStateOf(false) } // 创建一个能够检测卡片是否被展开的变量

    Surface() {
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
                    .size(550.dp, 40.dp)
            ){

                Row() {
                    Text(
                        "举报人ID:",
                        color = Color(0xF23F3D38),
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = reportsInfo.whistleblowerID,//被举报人ID
                        color = Color(0xD53D3A3A),
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp,
//                        modifier = Modifier
//                            .clickable { // 添加一个新的 Modifier 扩展方法，可以让元素具有点击的效果
//                                isExpanded = !isExpanded // 编写点击的事件内容
//                            }
//                            .animateContentSize(),
//                        maxLines = if (isExpanded) Int.MAX_VALUE else 1
                    )
                }

            }

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(550.dp, 40.dp)
            ){

                Row() {
                    Text(
                        "被举报人ID:",
                        color = Color(0xF23F3D38),
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = reportsInfo.isReportedID,//被举报人ID
                        color = Color(0xD53D3A3A),
                        style = MaterialTheme.typography.body1,
                        fontSize = 18.sp,
//                        modifier = Modifier
//                            .clickable { // 添加一个新的 Modifier 扩展方法，可以让元素具有点击的效果
//                                isExpanded = !isExpanded // 编写点击的事件内容
//                            }
//                            .animateContentSize(),
//                        maxLines = if (isExpanded) Int.MAX_VALUE else 1
                    )
                }

            }

            Spacer(Modifier.height(15.dp))

            Box(){
                Column() {
                    Text(
                        text = "举报详情：",
                        color = Color(0xF23F3D38),
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(Modifier.padding(horizontal = 5.dp)) {
                        Text(
                            text = reportsInfo.reason,
                            color = Color(0xD53D3A3A),
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .clickable {
                                    tempReportsInfo?.value=reportsInfo
                                    openReportDialog.value = true
                                    isExpanded = !isExpanded
                            },
                            maxLines = if (isExpanded) Int.MAX_VALUE else 3
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

            Box(Modifier.align(Alignment.CenterHorizontally)) {
                Row() {
                    Box() {
                        TextButton(
                            onClick = {
                                tempReportsInfo?.value=reportsInfo
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
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "通过",
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
                                tempReportsInfo?.value=reportsInfo
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
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "驳回",
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
    tempReportsInfo:MutableState<ReportInfo>?,
//    adminApplyViewModel: AdminApplyViewModel,
    clickEvent:(ID:String,status:String)-> Unit,
    postEmailEvent:(content:String,title: String,to: String)->Unit
) {
    if (openApproveDialog.value) {
        AlertDialog(
            onDismissRequest = { openApproveDialog.value = false },
            title = { Text(text = "通过举报确认") },
            text = {
                Text(
                    text = "你确定通过该用户的举报吗",
                    style = MaterialTheme.typography.body1
                )
            }, confirmButton = {
                TextButton(onClick = {
                    openApproveDialog.value = false
                    clickEvent(tempReportsInfo?.value?.isReportedID.toString(),"0")//将被举报人的状态设置为0，表示冻结
                    tempReportsInfo?.value?.let {
                        postEmailEvent("您的不良行为被用户举报，举报原因："+tempReportsInfo.value.reason+"  现已对你的账号进行封禁","一刻App通知：您的不良行为已被举报",
                            it.isReportedID)
                    }
//                    tempReportsInfo?.value?.let { adminApplyViewModel.changeInfoStatus(it.id,1) }
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
    tempReportsInfo:MutableState<ReportInfo>?,
//    adminApplyViewModel: AdminApplyViewModel,
    clickEvent:(ID:String,status:String)-> Unit,
    postEmailEvent:(content:String,title: String,to: String)->Unit
) {

    var textReplyContent = remember {
        NameInputState()
    }

    if (openRejectDialog.value) {
        AlertDialog(
            onDismissRequest = { openRejectDialog.value = false },
            title = { Text(text = "驳回举报确认") },
            text = {
                Column() {
                    Text(
                        text = "你确定驳回该用户的举报吗，请输入理由",
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
                                "请输入驳回理由",
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
                    clickEvent(tempReportsInfo?.value?.isReportedID.toString(),"1")
                    tempReportsInfo?.value?.let {
                        postEmailEvent("您举报的用户"+tempReportsInfo.value.isReportedID+" 举报失败。"+"拒绝理由："+textReplyContent.text,"一刻App通知：您的举报失败",
                            it.whistleblowerID)
                    }
//                    tempReportsInfo?.value?.let { adminApplyViewModel.changeInfoStatus(it.id,-1) }
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

@Composable
private fun addReportAlterDialog(
    openReportDialog: MutableState<Boolean>,
    tempReportsInfo:MutableState<ReportInfo>?,
    quesInfo:QuesAnswer,
) {

    var isExpanded by remember { mutableStateOf(false) } // 创建一个能够检测卡片是否被展开的变量
    var isQuesExpanded by remember { mutableStateOf(false) } // 创建一个能够检测卡片是否被展开的变量

    if (openReportDialog.value) {
        AlertDialog(
            onDismissRequest = { openReportDialog.value = false },
            title = { Text(text = "被举报回答详情：") },
            text = {
                Column {
                    Text(
                        text = "相关问题： "+quesInfo.question,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.clickable{isQuesExpanded = !isQuesExpanded},
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "被举报回答： "+quesInfo.answer.substringAfter('/'),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.clickable{isExpanded = !isExpanded},
                        maxLines = if (isExpanded) Int.MAX_VALUE else 4
                    )
                }
            }, confirmButton = {
                TextButton(onClick = {
                    openReportDialog.value = false
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