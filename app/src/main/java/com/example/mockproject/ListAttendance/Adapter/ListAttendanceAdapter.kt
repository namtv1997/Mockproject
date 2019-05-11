package com.example.mockproject.ListAttendance.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.ListAttendance.Model.Attendances
import com.example.mockproject.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_get_attendance.view.*

class ListAttendanceAdapter(var listAttendance: ArrayList<Attendances>) : RecyclerView.Adapter<ListAttendanceAdapter.viewHolderAttendance>() {
    private lateinit var context: Context
    private var mAttent : ClickShowDialog? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewHolderAttendance {
        context = p0.context
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_get_attendance, p0, false)
        return viewHolderAttendance(view)

    }

    fun showDialog(attent: ClickShowDialog) {
        mAttent = attent
    }

    override fun getItemCount(): Int {
        return listAttendance.size
    }

    override fun onBindViewHolder(p0: viewHolderAttendance, p1: Int) {
        p0.itemView.cb_get_attendance.text = listAttendance[p1].userFullName
        p0.itemView.cb_get_attendance.isChecked = listAttendance[p1].attendanced
        p0.itemView.edt_comment_attendance.text = listAttendance[p1].comment

        Picasso.with(context).load(listAttendance[p1].userAvatar).into(p0.itemView.img_get_attendance)

        p0.itemView.cb_get_attendance.setOnCheckedChangeListener { _, isCheck ->
            listAttendance[p0.adapterPosition].attendanced = isCheck
        }

        p0.itemView.submitTextView.setOnClickListener {
            val attentModel = Attendances(listAttendance[p1].userId, listAttendance[p1].userAvatar, listAttendance[p1].userFullName, listAttendance[p1].attendanced, listAttendance[p1].comment)
            mAttent?.onClick(attentModel)
        }
    }

    class viewHolderAttendance(itemView: View) : RecyclerView.ViewHolder(itemView)



    interface ClickShowDialog{
        fun onClick(attentModel : Attendances)
    }

}