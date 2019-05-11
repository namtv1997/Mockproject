package com.example.mockproject.homeScreen.listGroup


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

import android.widget.TextView
import android.widget.Toast
import com.example.mockproject.GroupFragment.GroupFragment

import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.homeScreen.listGroup.model.DataServerResponeGetGroups
import com.example.mockproject.homeScreen.listGroup.model.GroupModelOfGetGroups
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.showCustomDialog
import com.example.mockproject.util.swichFragment
import kotlinx.android.synthetic.main.fragment_list_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.KeyEvent.KEYCODE_BACK
import android.view.*


class ListGroupFragment : Fragment(),IpmCallBackItemClickListGroup {


    private var dataClient: DataClient? = null
    private lateinit var listGroup : ArrayList<GroupModelOfGetGroups>
    private lateinit var adapterListGroup : ListGroupAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dialog= Dialog(activity)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_progressbar)
        dialog.show()

        super.onViewCreated(view, savedInstanceState)
        listGroup = ArrayList()
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.getGroup(Constant.id)
        call?.enqueue(object  : Callback<DataServerResponeGetGroups>{
            override fun onFailure(call: Call<DataServerResponeGetGroups>?, t: Throwable?) {

            }
            override fun onResponse(call: Call<DataServerResponeGetGroups>?, response: Response<DataServerResponeGetGroups>?) {
                if(response?.body()?.code==0) {
                    listGroup.clear()
                    listGroup.addAll(response.body()?.listGroups?.listGroup!!)
                    adapterListGroup.notifyDataSetChanged()
                    dialog.cancel()
                }else{
                    dialog.cancel()
                }
            }
        })

        adapterListGroup = ListGroupAdapter(listGroup,this)
        recycleviewListGroup.layoutManager = GridLayoutManager(activity,2)
        recycleviewListGroup.adapter = adapterListGroup
        recycleviewListGroup.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
    override fun listenceOnClickItem(idGroup : String) {
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        var bundle = Bundle()
        bundle.putString("idGroup",idGroup)
        var groupsFragment = GroupFragment()
        groupsFragment.arguments = bundle
        swichFragment(groupsFragment)
    }

}
