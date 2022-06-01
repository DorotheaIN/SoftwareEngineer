package com.example.yike.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.yike.PasswordInputState
import com.example.yike.service.OfficialRepository
import com.example.yike.service.SendEmailRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object GlobalViewModel: ViewModel() {
    private val globalUserInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()
    private val globalQuestionList: MutableLiveData<ArrayList<Question>> = MutableLiveData<ArrayList<Question>>()

    private val globalOrgInfo:MutableLiveData<Organization> = MutableLiveData<Organization>()
    private val globalEmail: MutableLiveData<String> = MutableLiveData<String>()
    private val globalVerifyCode: MutableLiveData<String> = MutableLiveData<String>()
    private val globalPassWord:MutableLiveData<String> = MutableLiveData<String>()

    private val globalAdminInfo:MutableLiveData<AdminInfo> = MutableLiveData<AdminInfo>()

    private val registerImg = MutableLiveData<RequestBody>()
    private val registerDoc = MutableLiveData<RequestBody>()

    val imgUri = Transformations.switchMap(registerImg){
        OfficialRepository.fileUpload(registerImg.value!!)
    }
    val docUri = Transformations.switchMap(registerDoc){
        OfficialRepository.fileUpload(registerDoc.value!!)
    }

    fun updateImg(uri: String) {
        val file = File(uri)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("myFile", file.name, requestBody)
            .build()
        registerImg.value = multipartBody
    }

    fun  updateDoc(uri: String) {
        val file = File(uri)
        val requestBody = RequestBody.create(MediaType.parse("application/*"), file)
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("myFile", file.name, requestBody)
            .build()
        registerDoc.value = multipartBody
    }

    fun getToken(): String?{
        return globalUserInfo.value?.token
    }

    fun updateUserInfo(userId:String, userName: String, userStatus: Int, avatar: String, introduction: String, token: String) {
        globalUserInfo.value = UserInfo(userId, userName, userStatus, introduction, avatar, token)//反了？
    }

    fun updataQuestionList(questionList: ArrayList<Question>, questionTheme: ArrayList<QTheme>) {
        questionTheme.forEach {
            val q = Question(it.id, it.title, it.description, it.followNum, it.answerNum)
            questionList.add(q)
        }
        globalQuestionList.value = questionList
    }


    fun updateOrgInfo(organization: Organization) {
        globalOrgInfo.value = organization
    }

    fun updateEmail(email:String?) {
        globalEmail.value = email
    }

    fun updateVerifyCode(verifyCode: String) {
        globalVerifyCode.value = verifyCode
        println(verifyCode)
    }

    fun updateAdminInfo(adminInfo: AdminInfo){
        globalAdminInfo.value = adminInfo
    }

    fun updatePassWord(passWord:String){
        globalPassWord.value=passWord
    }

    fun getUserInfo(): UserInfo? { //为啥用这个 因为observe有问题 不知道为何？ 可以再试试
        return globalUserInfo.value
//        return UserInfo("1@126.com","hcy1658339245",1,"","")
    }

    fun getOrgInfo():Organization?{
        return globalOrgInfo.value
//        return Organization(79355314,1,"","同济小软家","")
    }

    fun getQuestion(id: String): Question? {
        var q = Question("","","",0,0)
        globalQuestionList.value?.forEach {
            if (it.id == id) q = it
        }
        return q
    }

    fun getEmail(): String? { //为啥用这个 因为observe有问题 不知道为何？ 可以再试试
        return globalEmail.value
    }

    fun getVerifyCode(): String? { //为啥用这个 因为observe有问题 不知道为何？ 可以再试试
        return globalVerifyCode.value
    }

    fun getAdminInfo():AdminInfo?{
        return globalAdminInfo.value
    }

    fun getPassWord():String?{
        return globalPassWord.value
    }


    private val emailLiveData = MutableLiveData<EmailInput>()

    //界面变量
    val sendEmailInfo = Transformations.switchMap(emailLiveData) { emailInput ->
        SendEmailRepository.sendEmail(emailInput.email)
    }


    //用户方法
    fun checksendStatus(inputEmail: String) {
        emailLiveData.value = EmailInput(inputEmail)
    }

    fun getQuestionList(_questionKyWd: String): ArrayList<Question> {
        val qL = ArrayList<Question>()
        globalQuestionList.value?.forEach {
            if (it.title.contains(_questionKyWd)) qL.add(it)
        }
        return qL
    }
}