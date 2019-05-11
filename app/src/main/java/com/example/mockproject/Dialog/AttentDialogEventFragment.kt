package com.example.mockproject.Dialog


import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*

import com.example.mockproject.R
import com.example.mockproject.Sprint5.AttendanceEventNT.Model.AttendancesEvent
import kotlinx.android.synthetic.main.fragment_attent_dialog_event.*
@SuppressLint("ValidFragment")
class AttentDialogEventFragment : DialogFragment() {
    private var mPassComment : PassCommentEvent?=null
    private var mAttendancesEvent : AttendancesEvent? =null

    interface PassCommentEvent{
        fun onPassEvent(attendancesEvent: AttendancesEvent?)
    }

    fun onTransEvent(passCommentEvent:PassCommentEvent){
        mPassComment = passCommentEvent
    }

    fun fillDataEvent(attendancesEvent: AttendancesEvent?){
        mAttendancesEvent = attendancesEvent
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_attent_dialog_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitEvent.setOnClickListener {
            val commentEvent=attenEditTextEvent.text.toString()
            mAttendancesEvent?.comment=commentEvent
            mPassComment?.onPassEvent(mAttendancesEvent)
            attenEditTextEvent.setText("")
            dismiss()
        }
    }
    override fun onResume() {
        val window = dialog.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 0.75).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }

}
