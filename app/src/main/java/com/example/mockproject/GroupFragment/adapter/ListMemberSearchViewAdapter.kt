package com.example.mockproject.GroupFragment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.GroupFragment.model.MemberSearch
import com.example.mockproject.GroupFragment.model.createMember.DataPostServer
import com.example.mockproject.GroupFragment.model.createMember.DataResponseCreateMember
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_search_member.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition

class ListMemberSearchViewAdapter (var listMemberSearchView : ArrayList<MemberSearch>):
RecyclerView.Adapter<ListMemberSearchViewAdapter.ViewHolder>() {
    private var dataClient: DataClient? = null
    private lateinit var context: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        val view = LayoutInflater.from(p0.context).
                inflate(R.layout.item_list_search_member,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listMemberSearchView.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Picasso.with(context).load(listMemberSearchView[position].avatar).into(
                viewHolder.itemView.imvAvtSearchMember)
        viewHolder.itemView.tvNameSearchMember.text = listMemberSearchView[position].fullName
        viewHolder.itemView.itemListMemberSearch.setOnClickListener {

            dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
            var dataPostServer = DataPostServer(listMemberSearchView[position].email,Constant.idGroupWhenClickGroup)
            val call = dataClient?.createMembers(Constant.id,dataPostServer)
            call?.enqueue(object : Callback<BaseDataResponseServer<DataResponseCreateMember>>{
                override fun onFailure(call: Call<BaseDataResponseServer<DataResponseCreateMember>>?, t: Throwable?) {
                }
                override fun onResponse(call: Call<BaseDataResponseServer<DataResponseCreateMember>>?,
                                        response: Response<BaseDataResponseServer<DataResponseCreateMember>>?) {
                    if (response?.body()?.code == 0){
                        context.showAlertDialog("Add member success!!")
                        viewHolder.itemView.itemListMemberSearch.visibility = View.GONE

                    }
                }
            })

        }
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)



}