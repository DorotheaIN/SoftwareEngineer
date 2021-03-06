package com.example.yike.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.yike.service.ActivityRepository
import com.example.yike.service.OrgLoginRepository
import com.example.yike.service.OrganizationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActivityCorrectViewModel() : ViewModel(
){
    private val orgInfo = GlobalViewModel.getOrgInfo()
    val GetOrgInfo = orgInfo
    //观察对象：
    private val isInit = MutableLiveData<Boolean>(false)

    private val activityID = MutableLiveData<Int>()

    private val activityToCorrect = MutableLiveData<Activity>()
    //界面变量
    val activityInfo = Transformations.switchMap(activityID) {it->
        ActivityRepository.getActivityDetail(it)
    }

//    val organizationInfo = Transformations.switchMap(isInit) {
//        OrgLoginRepository.checkLoginStatus(3,"tongji_sse")
//    }

    val correctRes = Transformations.switchMap(activityToCorrect){it->
        OrganizationRepository.correctActivity(it.id,it.capacity,it.content,it.date,it.form,it.genres,it.img,it.introduction,it.organizer.id,it.place,it.status,it.title)
    }

    //用户方法：
    fun init(id:Int) {
        isInit.value = true
        activityID.value = id
    }

    fun correct(activity: Activity)= runBlocking{
        val correct = launch {
            activityToCorrect.value = activity
        }
        correct.join()
        val wait = launch {
            delay(280)
        }
        wait.join()
    }
}