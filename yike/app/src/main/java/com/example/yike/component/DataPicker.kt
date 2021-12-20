package com.example.yike.component

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DatePicker(context: Context, type: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .height(40.dp),
        ){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ){
                Text("日期")
            }
            Spacer(modifier = Modifier.size(5.dp))
            DatePickerDemo(context = context)
            if(type == "Long"){
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "   -   ")
                    Spacer(modifier = Modifier.size(8.dp))
                    DatePickerDemo(context = context)
                }
            }
        }
        Divider(modifier = Modifier.height(2.dp))
    }
}

@Composable
fun DatePickerDemo(context: Context) {
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val now = Calendar.getInstance()
    mYear = now.get(Calendar.YEAR)
    mMonth = now.get(Calendar.MONTH)
    mDay = now.get(Calendar.DAY_OF_MONTH)
    now.time = Date()

    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            date.value = getFormattedDate(cal.time, "yyyy-MM-dd")
        }, mYear, mMonth, mDay
    )


//    val day1= Calendar.getInstance()
//    day1.set(Calendar.DAY_OF_MONTH, 1)
//    datePickerDialog.datePicker.minDate = day1.timeInMillis
//    datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ){
        Text(text =
            if(date.value != ""){
                "${date.value}"
            }else{
                "${getFormattedDate(now.time,"yyyy-MM-dd")}"
            },
            modifier = Modifier.clickable {
                datePickerDialog.show()
            }
        )
    }
    Spacer(modifier = Modifier.size(8.dp))
//    Column(
//        modifier = Modifier.fillMaxHeight(),
//        verticalArrangement = Arrangement.Center
//    ) {
//        IconButton(
//            onClick = {
//                datePickerDialog.show()
//            },
//        ) {
//            Icon(Icons.Rounded.Add, null)
//        }
//    }
}

fun getFormattedDate(date: Date?, format: String): String {
    try {
        if (date != null) {
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            return formatter.format(date)
        }
    } catch (e: Exception) {

    }
    return ""
}