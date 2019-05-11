package com.example.mockproject.Sprint5.calendarScreen.view



interface CalendarScreenViewIpm {
    fun onDayClick(date : String)
    fun sendDateAndIdGroupFromViewToPresenter(date : String , idGroup : String )
}