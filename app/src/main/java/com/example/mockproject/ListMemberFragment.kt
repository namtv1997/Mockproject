package com.example.mockproject


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.getmember.ResponeMembers
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import kotlinx.android.synthetic.main.fragment_list_member.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mockproject.getmember.NewCaptain
import com.example.mockproject.model.modelMember.MemberCourse
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.swichFragment



class ListMemberFragment : Fragment(), IpmCallBackOnClickListMember {
    override fun onClickItem(name: String, avt: String, emailMember: String, join: String, skype: String, phone: String) {
        val bundle = Bundle()
        bundle.putString("name",name)
        bundle.putString("avt",avt)
        bundle.putString("email",emailMember)
        bundle.putString("join", join)
        bundle.putString("skype",skype)
        bundle.putString("phone",phone)

        val memberFragment = MemberFragment()
        memberFragment.arguments = bundle
        swichFragment(memberFragment)

    }

    var dataClient: DataClient? = null
    var idCaptain: String = ""
    var page: Int = 1
    var size: Int = 5
    var membes: List<MemberCourse>? = null
    var isCaptain: Boolean = false
    var userId: String = ""
    var courseId: String = ""
    private var swipeAdapter: ListMemberAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataGroup()
        getMembers()

    }

    private fun getDataGroup(){
        var bundle = arguments
        courseId = arguments!!.getString("idGroup")
        isCaptain = arguments!!.getBoolean("isCaptain")
        idCaptain = arguments!!.getString("idCaptain")
    }

    private fun setAdapter(items: List<MemberCourse>) {
            swipeAdapter = ListMemberAdapter(
                    this.context!!,
                    idCaptain,
                    listMember,
                    items,
                    isCaptain,
                    courseId,
                    this,
                    object: ListMemberAdapter.OnSwipeItem {
                        override fun onSetCaptain(position: Int) {
                            setCaptain(position)
                        }
                        override fun onSwipeLeft(item: MemberCourse) {}
                        override fun onSwipeRight(item: MemberCourse) {}
                        override fun onSwipeTop(item: MemberCourse) {}
                        override fun onSwipeBottom(item: MemberCourse) {}
                        override fun onClickItem(item: MemberCourse) {}
        })

        listMember.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = swipeAdapter
        }
    }

    fun getMembers(){
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.getMembersAPI(courseId)
        call?.enqueue(object : Callback<ResponeMembers> {
            override fun onFailure(call: Call<ResponeMembers>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<ResponeMembers>?, response: Response<ResponeMembers>?) {
                membes = response?.body()?.data?.memberResponses
                membes?.let { setAdapter(it) }

            }
        })
    }

    fun loadMore(adapter: ListMemberAdapter){
        adapter.loadMore
    }

    fun setCaptain(position: Int){

        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        userId = membes!![position].userId
        val newCaptain = NewCaptain(membes!![position].userId)
        val call = dataClient?.setCaptain(courseId, idCaptain,4, newCaptain)
        call?.enqueue(object : Callback<BaseDataResponseServer<String>>{
            override fun onResponse(call: Call<BaseDataResponseServer<String>>?, response: Response<BaseDataResponseServer<String>>?) {

                if(response?.body()?.code == 0) {
                    isCaptain = false
                    idCaptain = membes!![position].userId
                    getMembers()
                }
            }
            override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {
            }
        })
    }

}
