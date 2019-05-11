package com.example.mockproject.Sprin7


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.GroupFragment.model.DataResponseGetGroup

import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelCreateCourse.CreateCourse
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.addTextChange
import com.example.mockproject.util.showAlertDialog
import kotlinx.android.synthetic.main.fragment_create_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroupFragment : Fragment() {
    var dataClient: DataClient? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_create_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextChange(edt_description_create_group,tv_description_empty,tv_description_empty)
        addTextChange(edt_title_create_group,tv_title_empty_createGroup,tv_title_empty_createGroup)
        btn_cancel_create_group.setOnClickListener {
            activity?.onBackPressed()
        }
        btn_done_create_group.setOnClickListener {
            connectServer()
        }

    }

    private fun connectServer() {
        val titleCreateGroup: String = edt_title_create_group.text.toString()
        val descriptionCreategroup: String = edt_description_create_group.text.toString()
        if (titleCreateGroup.isEmpty()){
            tv_title_empty_createGroup.visibility=View.VISIBLE
        }else if (descriptionCreategroup.isEmpty()){
            tv_description_empty.visibility=View.VISIBLE
        }else{
            dataClient = RetrofitClient.getClient()!!.create(DataClient::class.java)
            val post=CreateCourse(titleCreateGroup,descriptionCreategroup, Constant.id,"","")
            val call=dataClient?.createGroup(post)
            call?.enqueue(object :Callback<BaseDataResponseServer<DataResponseGetGroup>>{
                override fun onFailure(call: Call<BaseDataResponseServer<DataResponseGetGroup>>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<BaseDataResponseServer<DataResponseGetGroup>>?, response: Response<BaseDataResponseServer<DataResponseGetGroup>>?) {
                if (response?.body()?.code==0){
                    context?.showAlertDialog(getString(R.string.Create_course_Success))
                    activity?.onBackPressed()
                    Log.e("msg", response.body().code.toString())
                }
                }

            })
        }
    }
}
