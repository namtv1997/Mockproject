package com.example.mockproject.Sprint5.createEventScreen.presenter

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import com.example.mockproject.Sprint5.createEventScreen.apiHelper.ImlCallBackApi
import com.example.mockproject.Sprint5.createEventScreen.apiHelper.IpmCallBackFromModelToPresenterCreateEvent
import com.example.mockproject.Sprint5.createEventScreen.model.CreateEventModelPostServer
import com.example.mockproject.Sprint5.createEventScreen.view.IpmCallBackViewCreateEvent
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class LogicCreateEvent : IpmCallBackLogicCreateEvent, IpmCallBackFromModelToPresenterCreateEvent {

    var impCallBackCreateEvent: IpmCallBackViewCreateEvent? = null

    fun callAPICreateEvent(createEventModelPostServer: CreateEventModelPostServer, idContent: String?) {

        var dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        if(idContent != null) {
            var call = dataClient?.createEvent(createEventModelPostServer, Constant.id, idContent)
            call?.enqueue(object : Callback<BaseDataResponseServer<Any>> {
                override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<BaseDataResponseServer<Any>>?,
                                        response: Response<BaseDataResponseServer<Any>>?) {
                    if (response?.body()?.code == 0) {
                        callbackFromModelToPresenter()
                    }
                }
            })
        }else{
            var call = dataClient?.createEventNotEvent(createEventModelPostServer, Constant.id)
            call?.enqueue(object : Callback<BaseDataResponseServer<Any>> {
                override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<BaseDataResponseServer<Any>>?,
                                        response: Response<BaseDataResponseServer<Any>>?) {
                    if (response?.body()?.code == 0) {
                        callbackFromModelToPresenter()
                    }
                }
            })
        }
    }

    override fun callbackFromModelToPresenter() {
        impCallBackCreateEvent?.showDialogCreateEventSuccess()
    }

    override fun setView(view: IpmCallBackViewCreateEvent) {
        impCallBackCreateEvent = view
    }

    override fun btnCancelOnClick() {
        impCallBackCreateEvent?.cancelCreateEvent()
    }

    override fun btnAddOnClick(titleEvent: String, descriptionEvent: String?,
                               document: String?, speaker: String,
                               startTime: String, endTime: String,
                               idContent: String?) {
        if (titleEvent.isEmpty()) {

            impCallBackCreateEvent?.showDialogErrorTitleIsEmpty()
        } else if (titleEvent.length > 64) {

            impCallBackCreateEvent?.showDialogErrorTitleOver64Character()
        } else if (descriptionEvent?.length!! > 255) {

            impCallBackCreateEvent?.showDialogErrorDescriptionOver255Character()
        } else if (!checkURL(document)) {
            impCallBackCreateEvent?.showDialogErrorLinkNotUrl()
        } else if (speaker.length > 32) {

            impCallBackCreateEvent?.showDialogErrorSpeakerOver32Character()
        } else if (speaker.isEmpty()) {

            impCallBackCreateEvent?.showDialogErrorSpeakerIsEmpty()
        } else if (startTime.isEmpty()) {

            impCallBackCreateEvent?.showDialogErrorStartTimeIsEmpty()
        } else if (endTime.isEmpty()) {

            impCallBackCreateEvent?.showDialogErrorDurationIsEmpty()
        } else {
            val createEventModelPostServer = CreateEventModelPostServer(Constant.idGroupWhenClickGroup,

                    titleEvent, descriptionEvent, document, speaker, startTime, endTime)
                    callAPICreateEvent(createEventModelPostServer, idContent)

        }
    }

    override fun destroyView() {

        impCallBackCreateEvent = null
    }

    var imlCallBackApi: ImlCallBackApi? = null

    private fun checkURL(input: CharSequence?): Boolean {
        if (TextUtils.isEmpty(input)) {
            return true
        }
        val URL_PATTERN = Patterns.WEB_URL
        var isURL = URL_PATTERN.matcher(input).matches()
        if (!isURL) {
            val urlString = input.toString() + ""
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    URL(urlString)
                    isURL = true
                } catch (e: Exception) {
                }
            }
        }
        return isURL
    }
}