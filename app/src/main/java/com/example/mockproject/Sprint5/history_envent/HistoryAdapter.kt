package com.example.mockproject.Sprint5.history_envent

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.R
import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import kotlinx.android.synthetic.main.item_history_event.view.*


class HistoryAdapter(
        val context: Context,
        val historyEvent: ArrayList<EventModel>
)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_history_event, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {

        return historyEvent.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        var duration = historyEvent[position].duration.substringBefore("minutes").trim().toInt()
        var hour = duration/60
        var minutes = duration % 60

        viewHolder.itemView.tvTitleHs.text = "Title: ${historyEvent[position].title}"
        viewHolder.itemView.tvStartTimeHs.text = "StartTime: ${historyEvent[position].startDateTime}"
        if(hour < 10){
            if (minutes < 10)
                viewHolder.itemView.tvDurationHs.text = "Duration: 0${hour}h:0$minutes"
            else
                viewHolder.itemView.tvDurationHs.text = "Duration: 0${hour}h:$minutes"
        }else{
            if (minutes < 10)
                viewHolder.itemView.tvDurationHs.text = "Duration: ${hour}h:0$minutes"
            else
                viewHolder.itemView.tvDurationHs.text = "Duration: ${hour}h:$minutes"
        }
        viewHolder.itemView.tvSpeakerHs.text = "Speaker: ${historyEvent[position].speakerLink}"
        viewHolder.itemView.tvDescriptionHs.text = "Description: ${historyEvent[position].description}"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        }
}