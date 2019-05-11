package com.example.mockproject.Sprint5.createEventScreen.view

interface IpmCallBackViewCreateEvent {
    fun showDialogErrorTitleIsEmpty()
    fun showDialogErrorTitleOver64Character()
    fun showDialogErrorDescriptionOver255Character()
    fun showDialogErrorLinkNotUrl()
    fun showDialogErrorSpeakerOver32Character()
    fun showDialogErrorSpeakerIsEmpty()
    fun showDialogErrorStartTimeIsEmpty()
    fun showDialogErrorDurationIsEmpty()
    fun showDialogCreateEventSuccess()
    fun cancelCreateEvent()
}