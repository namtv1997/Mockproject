package com.example.mockproject.Sprint5.history_envent


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mockproject.R
import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import com.example.mockproject.Sprint5.calendarScreen.model.EventResponses
import com.example.mockproject.Sprint5.calendarScreen.model.HistoryEventResponses
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import kotlinx.android.synthetic.main.fragment_history_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryEventFragment : Fragment() {

    var dataClient: DataClient? = null
    var listItemHistoryEvent: ArrayList<EventModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHistoryEvent()
    }

    private fun getHistoryEvent(){

        dataClient = RetrofitClient.getClient()!!.create(DataClient::class.java)
        val call = dataClient?.getHistoryEvent(Constant.idGroupWhenClickGroup)
        call?.enqueue(object : Callback<BaseDataResponseServer<HistoryEventResponses>> {

            override fun onFailure(call: Call<BaseDataResponseServer<HistoryEventResponses>>?, t: Throwable?) {
            }

            override fun onResponse(
                    call: Call<BaseDataResponseServer<HistoryEventResponses>>?,
                    response: Response<BaseDataResponseServer<HistoryEventResponses>>?
            ) {
                if(response?.body()?.code == 0){
                    listItemHistoryEvent = response.body()?.data?.listHistoryEventResponses
                    Log.e("msg", "size:${listItemHistoryEvent?.size}")

                    if(listItemHistoryEvent?.size == 0) {
                        tvNotificationEmptyItemEvent.visibility = View.VISIBLE
                    }else{
                        tvNotificationEmptyItemEvent.visibility = View.GONE
                        listItemHistoryEvent?.let { setAdapter(it) }
                    }
                }
            }
        })
    }

    fun setAdapter(listItemHistoryEvent: ArrayList<EventModel>){
        val adapterH = HistoryAdapter(this.context!!, listItemHistoryEvent)
        recyclerHistoryEvent.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = adapterH
        }
    }
}
