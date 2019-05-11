package com.example.mockproject.Sprint5.calendarScreen.presenter

import android.graphics.Color
import android.util.Log
import com.example.mockproject.Sprint5.calendarScreen.model.EventModelByMonth
import com.example.mockproject.Sprint5.calendarScreen.model.EventResponses
import com.example.mockproject.Sprint5.calendarScreen.model.GetEventMonthYearResponses
import com.example.mockproject.Sprint5.calendarScreen.view.CalendarFragment
import com.example.mockproject.Sprint5.calendarScreen.view.CalendarScreenViewIpm
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.github.sundeepk.compactcalendarview.domain.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LogicCalendarScreen(var calendarFragment : CalendarFragment) : CalendarScreenViewIpm, CalendarScreenPresenterImp {

    var dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
    var simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    var listEvetnByMonth = ArrayList<EventModelByMonth>()


    override fun receiveDataFromModel(listEventByMonth: ArrayList<EventModelByMonth>) {
        for (i in 0 until listEventByMonth.size) {
            var dateEvent: Date = simpleDateFormat.parse(listEventByMonth[i].startDate)
            if (listEventByMonth[i].fromContent) {
                var event = Event(Color.RED, dateEvent.time, listEventByMonth[i])
                calendarFragment.sendEventFromPresenterToView(event)
            } else{
                var event = Event(Color.WHITE, dateEvent.time, listEventByMonth[i])
                calendarFragment.sendEventFromPresenterToView(event)
            }
        }
    }
    override fun onFailReceiveDataFromModel() {
    }

    override fun onDayClick (date: String) {
        var dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call =
                dataClient?.getEventByDay(date,Constant.idGroupWhenClickGroup)
        call?.enqueue(object : Callback<BaseDataResponseServer<EventResponses>> {
            override fun onFailure(call: Call<BaseDataResponseServer<EventResponses>>?, t: Throwable?) {

            }
            override fun onResponse(call: Call<BaseDataResponseServer<EventResponses>>?, response:
            Response<BaseDataResponseServer<EventResponses>>?) {
                if (response?.body()?.data?.listEventByDay !=  null){
                    calendarFragment.sendListEventToView(response.body().data?.listEventByDay!!)
                }
            }
        })
    }
    override fun sendDateAndIdGroupFromViewToPresenter(date: String, idGroup: String) {
        Constant.date = date
        var call = dataClient?.getEventByMonth(date, idGroup)
        call?.enqueue(object : Callback<BaseDataResponseServer<GetEventMonthYearResponses>> {
            override fun onFailure(call: Call<BaseDataResponseServer<GetEventMonthYearResponses>>?, t: Throwable?) {

            }
            override fun onResponse(call: Call<BaseDataResponseServer<GetEventMonthYearResponses>>?,
                                    response: Response<BaseDataResponseServer<GetEventMonthYearResponses>>?) {
                if (response?.body()?.data?.listEventByMonth != null){
                    receiveDataFromModel(response.body()?.data?.listEventByMonth!!)
                }
            }
        })
    }
}
