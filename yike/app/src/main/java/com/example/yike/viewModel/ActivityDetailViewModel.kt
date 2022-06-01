package com.example.yike.viewModel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.yike.service.ActivityRepository
import com.example.yike.service.UserActivityRepository
import com.example.yike.viewModel.GlobalViewModel.getUserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


data class Evaluation(
    val reviewerID:String,
    val activityID:Int,
    val content:String,
    val score:Int,
    val reviewerName:String,
    val reviewerIntroduction:String,
    val reviewerAvator:String
)


data class RelationInput(
    val activityID: Int,
    val userID:String,
    val status:Int = 1
)

data class UARelation(
    val likeStatus: Boolean,
    val subscribeStatus: Boolean
)

data class ActivityDetail(
    val title:String,
    val img:String,
    val date:String,
    val place:String,
    val form:String,
    val introduction:String,
    val content:String,
    val genres:String,
    var likeNum:Int,
    val capacity:Int,
    var status:Int,
    var subscriberNum:Int,/////////////
    val organizer: Organization,
    val id:Int,
)


class ActivityDetailViewModel(
    _activityId:Int
) :ViewModel(){
    private val activityID = _activityId
    private val userID = GlobalViewModel.getUserInfo()?.id
    private val _isGet = MutableLiveData<Boolean>()
    private val toReview = MutableLiveData<Evaluation>()
    private val _refresh = MutableLiveData<Int>()
    val isGet: LiveData<Boolean> = _isGet
    private val _isDel = MutableLiveData<Int>()
    private val isDel = MutableLiveData<Int>(0)
    val getUserID = userID
    private val toLike = MutableLiveData<Int>()

    private val toSub = MutableLiveData<Int>()

    val activityDetail = Transformations.switchMap(_isGet){
        ActivityRepository.getActivityDetail(activityID)
    }

    val evaluationList = Transformations.switchMap(_refresh){
        ActivityRepository.getEvaluationList(activityID)
    }

    val likeStatus = Transformations.switchMap(_refresh){
        UserActivityRepository.checkLike(activityID,userID!!)
    }

    val subscribeStatus = Transformations.switchMap(_refresh){
        UserActivityRepository.checkSubscribe(activityID,userID!!)
    }

    val activityRecommendedList = Transformations.switchMap(_isGet){it->
        ActivityRepository.getActivityRecommended(userID!!)
    }

    val likeRes = Transformations.switchMap(toLike){it->
        UserActivityRepository.postLikeActivity(activityID,userID!!,it)
    }

    val subRes = Transformations.switchMap(toSub){it->
        UserActivityRepository.postSubActivity(activityID,userID!!,it)
    }

    val reviewRes = Transformations.switchMap(toReview){it->
        UserActivityRepository.postReviewActivity(it.activityID,it.content,it.reviewerID,it.score)
    }

    val delReviewRes = Transformations.switchMap(_isDel){
        UserActivityRepository.postDelReviewActivity(activityID,userID!!)
    }

    fun getActivityDetail(){
        _isGet.value = true
        _refresh.value = 0
    }

//    fun save(like:Boolean,subscribe:Boolean){
//        println(like)
//        println(subscribe)
//        if(likeStatus.value != like && likeStatus!=null){
//            if(like == true){//点赞
//                toLike.value = 1
//                UserActivityRepository.postLikeActivity(activityID,userID!!,1)
//            }else{
//                toLike.value = 0
//                UserActivityRepository.postLikeActivity(activityID,userID!!,0)
//            }
//        }
//        if(subscribeStatus.value != subscribe && subscribeStatus!=null){
//            if(subscribe == true){
//                toSub.value = 1
//                UserActivityRepository.postSubActivity(activityID,userID!!,1)
//            }else {
//                toSub.value = 0
//                UserActivityRepository.postSubActivity(activityID,userID!!,0)
//            }
//        }
//    }

    fun like(like:Boolean){
        if(like == true){ // 点赞
            toLike.value = 1
        }else {
            toLike.value = 0
        }
    }

    fun subscribe(sub:Boolean){
        if(sub == true){
            toSub.value = 1
        }else {
            toSub.value = 0
        }
    }


    fun review(content: String) = runBlocking {
        val review = launch {
            println(getNow())
            toReview.value = Evaluation(userID!!,activityID,getNow()+'/'+content,5,"","","")
            delay(1000)
        }
        review.join()
        val ref = launch {
            _refresh.value = _refresh.value?.plus(1)
        }
        ref.join()
    }

    fun deleteReview() = runBlocking {
        val del = launch {
            isDel.value = isDel.value?.plus(1)
            _isDel.value = isDel.value
            delay(1000) // 好像时间很看后端速度
        }
        del.join()
        val ref = launch {
            _refresh.value = _refresh.value?.plus(1)
        }
        ref.join()
    }
}
fun getNow(): String {
    if (android.os.Build.VERSION.SDK_INT >= 24){
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }else{
        var tms = Calendar.getInstance()
        return tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString()
    }

}