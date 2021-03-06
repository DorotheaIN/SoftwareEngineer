package com.example.yike.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.yike.R
import com.example.yike.viewModel.Activity
import com.example.yike.viewModel.Organization
import com.example.yike.viewModel.OrganizationViewModel

@Composable
fun OrganizationScreen(
    navController: NavController,
    viewModel: OrganizationViewModel
){
    var openDialog: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }//记录是否打开框
    val isGet = viewModel.isGet.observeAsState()
    val refresh = viewModel.refresh.observeAsState()
    if( isGet.value != true ){
        viewModel.getInfo()
    } else {
        val organization = viewModel.GetOrgInfo
        val activityList = viewModel.activityList.observeAsState()
        val delRes = viewModel.delRes.observeAsState()
        reconfirmAlterDialog(openDialog,navController)
        OrganizationScreenContent(
            openDialog,navController, organization,activityList.value,
            { id->
                viewModel.delete(id)
            },{id->
                navController.navigate("activityreflect/${id}")
            }
        )
    }
}

@Composable
fun OrganizationScreenContent(
    openDialog: MutableState<Boolean>,
    navController: NavController,
    organization: Organization?,
    activityDetailList:ArrayList<Activity>?,
    delEvent:(id:Int) -> Unit,
    checkEvent:(id:Int) -> Unit
){
    if(organization == null || activityDetailList == null){
        Scaffold(){ paddingValues ->
            Loader(paddingValues)
        }
    } else {
        Surface(
            elevation = 5.dp,
            modifier = Modifier
                .padding(0.dp, 0.dp)
                .fillMaxSize()
                .background(Color(0xffededed))
        ) {
            LazyColumn(Modifier){
                item {
                    header(organization,navController,openDialog)
                }
                item{
                    PublishItem(navController)
                }
                item{
                    ActivityPublishList(activityDetailList,navController,delEvent,checkEvent)
                }
            }
        }
    }
}

@Composable
private fun Loader(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
        )
    }
}

@Composable
fun header(
    organization:Organization,
    navController: NavController,
    openDialog:MutableState<Boolean>
    ){
    Surface(
        modifier = Modifier
            .padding(bottom = 7.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().clickable {  }
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xB2806FA0), Color(0xE14256C4)),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            LoginOutItem(navController = navController,openDialog)
            Spacer(Modifier.height(20.dp))//
            Box(
                modifier = Modifier
                    .size(550.dp, 105.dp)
                    .padding(15.dp, 0.dp)
                    .clickable { }
            ){
                Row(modifier = Modifier.padding(all = 8.dp)) {
                    Image(
                        painter = rememberImagePainter(organization.avator),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .size(55.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxSize()
//                .border(1.5.dp, MaterialTheme.colors.secondary, RoundedCornerShape(7.dp))
                    )
                    Spacer(modifier = Modifier.width(15.dp))

                    Column (modifier = Modifier.size(300.dp,85.dp)){
                        Text(
                            text = organization.username,
                            color = Color.White,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = organization.introduction,
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
            Spacer(Modifier.height(80.dp))//
        }
    }
}


@Composable
fun ActivityPublishList(
    activityDetailList:ArrayList<Activity>,
    navController: NavController,
    delEvent:(id:Int) -> Unit,
    checkEvent:(id:Int) -> Unit
){
    Column() {
        activityDetailList.forEach { item->
            ActivityPublishedItem(item,navController,delEvent,checkEvent)
        }
    }
}

@Composable
fun ActivityPublishedItem(
    item:Activity,
    navController: NavController,
    delEvent:(id:Int) -> Unit,
    checkEvent:(id:Int) -> Unit
){
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        modifier = Modifier
            .padding(0.dp, 0.1.dp)
            .fillMaxWidth()
    ) {
        Box(
            Modifier
                .padding(15.dp,11.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1F)
                                .clickable {
                                    checkEvent(item.id)
                                }
                        ) {
                            Text(
                                text = item.title,
                                color = Color.Black,
                                style = TextStyle(
                                    fontSize = 16.sp,//
                                    letterSpacing = 1.sp
                                ),
                                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                modifier = Modifier
                                    .paddingFromBaseline(top = 24.dp)
                                    .padding(15.dp, 0.dp)
                            )

                        }
                        Column() {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Follow",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(5.dp, 0.dp)
                                    .clickable {
                                        navController.navigate("activityedit/${item.id}")
                                    }
                            )
                        }
                        Column() {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = "Follow",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(5.dp, 0.dp)
                                    .clickable {
                                        delEvent(item.id)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PublishItem(navController: NavController){
    Surface(
        shape = MaterialTheme.shapes.medium, // 使用 MaterialTheme 自带的形状
        elevation = 5.dp,
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 15.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("activity_publish")
            },
        color = Color(0xFFFFFFFF),
    ) {
        Box(
            Modifier.fillMaxSize()
        ){
            Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 5.dp)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ){
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "发布活动",
                        color = Color(0xFF253ECC),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold, //设置字体粗细
                            fontSize = 16.sp,//
                            letterSpacing = 1.sp
                        ),
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        painterResource(id = R.drawable.edit),
                        contentDescription = "Follow",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(5.dp, 0.dp),
                        tint = Color(0xFF253ECC)
                    )
                }
            }

        }
    }
}

@Composable
private fun LoginOutItem(
    navController: NavController,
    openDialog:MutableState<Boolean>
){
    Box(Modifier.fillMaxSize().clickable {  }){
        Box(modifier = Modifier
            .align(Alignment.TopEnd)){
            IconButton(onClick = { openDialog.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.loginout),
                    contentDescription = "login out",
                    Modifier.size(24.dp),
                    tint = White
                )
            }
        }
    }
}


//qi
@Composable
fun ActivityPublishItem(item:Activity){
    Row(
        Modifier
            .size(600.dp, 40.dp)
            .background(Color.White)
            .clickable { }
    ) {
        Box(
            Modifier
                .size(520.dp, 40.dp)
                .padding(35.dp, 5.dp)
        ){
            Text(
                text = item.title,
                color = Color.Black,
                style = TextStyle(
//                    fontWeight = FontWeight.Bold, //设置字体粗细
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                ),
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif
            )
        }
        Box(
            Modifier
                .padding(5.dp, 7.dp)
                .size(width = 30.dp, height = 40.dp)
        ){
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "",
//                modifier = Modifier
//                    .fillMaxHeight()
            )
        }

    }
    Divider(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 24.dp, end = 24.dp),
        //颜色
        color = Color(0x56979797),
    )
}

@Composable
private fun reconfirmAlterDialog(
    openDialog: MutableState<Boolean>,
    navController:NavController
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "退出登录确认") },
            text = {
                Text(
                    text = "你确定退出登录吗",
                    style = MaterialTheme.typography.body1
                )
            }, confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    navController.navigate("login")
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