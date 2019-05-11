package com.example.mockproject.Sprint5.calendarScreen.view

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.example.mockproject.R
import com.example.mockproject.Sprint5.CallbackClickItemListEvent

import com.example.mockproject.Sprint5.ListEventByDayAdater
import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import com.example.mockproject.Sprint5.calendarScreen.model.EventModelByMonth
import com.example.mockproject.Sprint5.calendarScreen.presenter.LogicCalendarScreen
import com.example.mockproject.Sprint5.calendarScreen.presenter.SendEventFromPresenterToView

import com.example.mockproject.Sprint5.createEventScreen.view.CreateEventFragment
import com.example.mockproject.Sprint5.eventDetail.EventDetailFragment
import com.example.mockproject.Sprint5.history_envent.HistoryEventFragment
import com.example.mockproject.common.Constant

import com.example.mockproject.util.swichFragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList




class CalendarFragment : Fragment(), SendEventFromPresenterToView {
    override fun sendListEventToView(listEvent: ArrayList<EventModel>) {
        val adapter = ListEventByDayAdater(listEvent,object  : CallbackClickItemListEvent{
            override fun onClickItem(eventId : String,title: String, description: String, link: String, speaker: String, startDete: String, endDate: String, fromContent: Boolean) {
                val bundleSendDataToDetail = Bundle()
                bundleSendDataToDetail.putString("eventId",eventId)
                bundleSendDataToDetail.putString("titte", title)
                bundleSendDataToDetail.putString("description", description)
                bundleSendDataToDetail.putString("link", link)
                bundleSendDataToDetail.putString("speaker", speaker)
                bundleSendDataToDetail.putString("startDete", startDete)
                bundleSendDataToDetail.putString("endDate", endDate)
                bundleSendDataToDetail.putBoolean("fromContent", fromContent)
                val detailEvent = EventDetailFragment()
                detailEvent.arguments = bundleSendDataToDetail
                swichFragment(detailEvent)
            }
        })
        recycleListEvent.adapter = adapter
        recycleListEvent.layoutManager = LinearLayoutManager(activity)


    }

    override fun sendEventFromPresenterToView(event: Event) {

        compactCalendar.addEvent(event)
    }

    private var dateFormat = SimpleDateFormat("yyyy/MM")
    private var dateFormatFull  = SimpleDateFormat("yyyy/MM/dd")
    private var dateFormatMonth = SimpleDateFormat("MMMM- yyyy", Locale.getDefault())
    private var listEvent = ArrayList<EventModel>()

    private var logicCalendar = LogicCalendarScreen(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAddEvent.setOnClickListener { swichFragment(CreateEventFragment()) }

        compactCalendar.setUseThreeLetterAbbreviation(true)
        if (Constant.id == Constant.idCaptainWhenClickGroup){
            tvAddEvent.visibility = View.VISIBLE
        }

        val dateCurrent = Calendar.getInstance().time
        getEventMontNow(dateCurrent)

        compactCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                logicCalendar.onDayClick(dateFormatFull.format(dateClicked))
            }
            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                compactCalendar.removeAllEvents()
                tvCalendar.text = dateFormatMonth.format(firstDayOfNewMonth)
                val date: String = dateFormat.format(firstDayOfNewMonth)
                logicCalendar.sendDateAndIdGroupFromViewToPresenter(date,Constant.idGroupWhenClickGroup)
            }
        })
        btnHistoryEvent.setOnClickListener {
            swichFragment(HistoryEventFragment())
        }
    }

    private fun getEventMontNow(firstDayOfNewMonth: Date){
        tvCalendar.text = dateFormatMonth.format(firstDayOfNewMonth)
        val date: String = dateFormat.format(firstDayOfNewMonth)
        logicCalendar.sendDateAndIdGroupFromViewToPresenter(date,Constant.idGroupWhenClickGroup)
    }

}
