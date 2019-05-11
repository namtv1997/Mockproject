package com.example.mockproject.GroupFragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.GroupFragment.adapter.ListMemberSearchViewAdapter
import com.example.mockproject.GroupFragment.model.MemberSearch
import com.example.mockproject.GroupFragment.model.MemberSearchResponse
import com.example.mockproject.GroupFragment.model.createMember.DataPostServer

import com.example.mockproject.R
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import kotlinx.android.synthetic.main.fragment_add_member_for_captain.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddMemberForCaptainFragment : Fragment() {
    var idGroup : String? = ""
    var listMember = ArrayList<MemberSearch>()
    private lateinit var adapter : ListMemberSearchViewAdapter

    var dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_member_for_captain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        btnSearchMember.setOnClickListener {
            var bundle = arguments
            idGroup = bundle?.getString(getString(R.string.idGroupForFragmentSearchMember))
            dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)

            val call = dataClient?.getSearchMember(this.idGroup!!,edtSearchMember.text)
            call?.enqueue(object : Callback<BaseDataResponseServer<MemberSearchResponse>>{
                override fun onFailure(call: Call<BaseDataResponseServer<MemberSearchResponse>>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<BaseDataResponseServer<MemberSearchResponse>>?,
                                        response: Response<BaseDataResponseServer<MemberSearchResponse>>?) {
                    if( response?.body()?.data?.usersNotMemberResponse == null){
                        context?.showAlertDialog(getString(R.string.Sorry))
                    }else{
                        listMember.clear()
                        listMember = response.body()?.data?.usersNotMemberResponse!!
                        adapter = ListMemberSearchViewAdapter(listMember)
                        adapter.notifyDataSetChanged()
                        listMemberSearch.layoutManager = LinearLayoutManager(activity)
                        listMemberSearch.adapter = adapter

                    }
                }
            })

        }

    }

}
