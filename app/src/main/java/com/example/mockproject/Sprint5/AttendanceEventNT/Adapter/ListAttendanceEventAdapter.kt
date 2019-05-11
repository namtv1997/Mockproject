package com.example.mockproject.Sprint5.AttendanceEventNT.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.ListAttendance.Adapter.ListAttendanceAdapter
import com.example.mockproject.ListAttendance.Model.Attendances
import com.example.mockproject.R
import com.example.mockproject.Sprint5.AttendanceEventNT.Model.AttendancesEvent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_get_attendance.view.*
import kotlinx.android.synthetic.main.item_get_attendance_event.view.*

class ListAttendanceEventAdapter(var listAttendanceEvent: ArrayList<AttendancesEvent>) : RecyclerView.Adapter<ListAttendanceEventAdapter.ViewHolderAttendanceEvent>() {
    private lateinit var context: Context
    private var mAttentEvent : ClickShowDialogEvent?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderAttendanceEvent {
        context = p0.context
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.item_get_attendance_event, p0, false)
        return ViewHolderAttendanceEvent(view)
    }

    fun showDialogEvent(attentEvent:ClickShowDialogEvent) {
        mAttentEvent = attentEvent
    }

    override fun getItemCount(): Int {
        return listAttendanceEvent.size
    }

    override fun onBindViewHolder(p0: ViewHolderAttendanceEvent, p1: Int) {
        p0.itemView.cb_get_attendance_event.text = listAttendanceEvent[p1].userFullName
        p0.itemView.cb_get_attendance_event.isChecked = listAttendanceEvent[p1].attendanced
        p0.itemView.edt_comment_attendance_event.text = listAttendanceEvent[p1].comment

        Picasso.with(context).load(listAttendanceEvent[p1].userAvatar).into(p0.itemView.img_get_attendance_event)

        p0.itemView.cb_get_attendance_event.setOnCheckedChangeListener { _, isCheck ->
            listAttendanceEvent[p0.adapterPosition].attendanced = isCheck
        }
        p0.itemView.submitTextViewEvent.setOnClickListener {
            val attentModelEvent=AttendancesEvent(listAttendanceEvent[p1].userId, listAttendanceEvent[p1].userAvatar, listAttendanceEvent[p1].userFullName, listAttendanceEvent[p1].attendanced, listAttendanceEvent[p1].comment)
            mAttentEvent?.onClick(attentModelEvent)
        }
    }

    class ViewHolderAttendanceEvent(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface ClickShowDialogEvent {
        fun onClick(attentModelEvent: AttendancesEvent)
    }
}