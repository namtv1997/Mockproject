package com.example.mockproject.Sprint5

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.R
import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import kotlinx.android.synthetic.main.item_list_event.view.*


class ListEventByDayAdater (var listEventByDay : ArrayList<EventModel>, var callback : CallbackClickItemListEvent):
        RecyclerView.Adapter<ListEventByDayAdater.ViewHolderEvent>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderEvent {
        var view = LayoutInflater.from(p0.context).inflate(R.layout.item_list_event,p0,false)
        return ViewHolderEvent(view)
    }

    override fun getItemCount(): Int = listEventByDay.size

    override fun onBindViewHolder(p0: ViewHolderEvent, position: Int) {
        var ev = listEventByDay[position]
        p0.itemView.tvAvtEvent.text = ev.title.substring(0,1).toUpperCase()
        p0.itemView.tvTitleEvent.text = ev.title
        p0.itemView.tvDateEvent.text = ev.startDateTime
        p0.itemView.itemListEvent.setOnClickListener {
            callback.onClickItem(ev.id,
                    ev.title,ev.description,
                    ev.documentLink,ev.speakerLink,
                    ev.startDateTime,ev.endDateTime,
                    ev.fromContent)
        }

    }
    class ViewHolderEvent (itemView : View): RecyclerView.ViewHolder(itemView)



}