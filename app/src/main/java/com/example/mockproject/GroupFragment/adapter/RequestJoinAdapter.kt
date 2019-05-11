package com.example.mockproject.GroupFragment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.GroupFragment.model.DataRequestJoinToGroup
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_request_join_togroup.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition

class RequestJoinAdapter(var listRequest : ArrayList<DataRequestJoinToGroup>) :
        RecyclerView.Adapter<RequestJoinAdapter.ViewHolder>() {
    private var dataClient: DataClient? = null
    lateinit var context : Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        var view  = LayoutInflater.from(p0.context).inflate(R.layout.item_list_request_join_togroup,
                p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int  = listRequest.size

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.itemView.tvMemberName.text = listRequest[p1].userFullName
        viewHolder.itemView.btnDelete.setOnClickListener {
            viewHolder.itemView.itemListRequest.visibility = View.GONE
            deleteRequestJoinToGroup(1,p1)
            context.showAlertDialog(" Deleted Request Join!!")
        }
        viewHolder.itemView.btnOk.setOnClickListener {
            viewHolder.itemView.itemListRequest.visibility = View.GONE
            deleteRequestJoinToGroup(0,p1)
            context.showAlertDialog("OK Request Join!!")
        }
        Picasso.with(context).load(listRequest[p1].userAvatar).into(viewHolder.itemView.avtMember)
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    fun deleteRequestJoinToGroup(code: Int, position: Int){
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.deleteQueue(listRequest[position].queueId,Constant.id,code)
        call?.enqueue(object : Callback<BaseDataResponseServer<Any>>{
            override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<BaseDataResponseServer<Any>>?,
                                    response: Response<BaseDataResponseServer<Any>>?) {

            }

        })
    }
}