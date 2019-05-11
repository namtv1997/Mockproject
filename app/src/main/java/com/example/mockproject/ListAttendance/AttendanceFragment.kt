package com.example.mockproject.ListAttendance


import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.Dialog.AttentDialogFragment
import com.example.mockproject.ListAttendance.Adapter.ListAttendanceAdapter
import com.example.mockproject.ListAttendance.Model.Attendances
import com.example.mockproject.ListAttendance.Model.ListGetAttendance
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance

import com.example.mockproject.R
import com.example.mockproject.model.modelPostAttendance.ListPostMemberUserId
import com.example.mockproject.model.modelPostAttendance.PostAttendanceContent
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.showToast_Short
import kotlinx.android.synthetic.main.fragment_attendance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class AttendanceFragment : Fragment(), ListAttendanceAdapter.ClickShowDialog, AttentDialogFragment.PassComment {
    private var attentDialogFragment: AttentDialogFragment? = null

    override fun onPass(attendances: Attendances?) {
        for (i in 0 until listGetAttendance.size) {
            if (listGetAttendance[i].userId == attendances?.userId) {
                listGetAttendance.removeAt(i)
                listGetAttendance.add(i, attendances)
            }
        }
    }


    override fun onClick(attendances: Attendances) {
        attentDialogFragment?.fillData(attendances)
        attentDialogFragment?.show(activity?.supportFragmentManager, "")
    }

    var courseId: String? = ""
    var contentId: String? = ""
    private var listGetAttendance = ArrayList<Attendances>()

    private lateinit var adapter: ListAttendanceAdapter
    private var dataclient = RetrofitClient.getClient()?.create(DataClient::class.java)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListAttendanceAdapter(listGetAttendance)
        adapter.showDialog(this)
        val bundle = arguments
        courseId = bundle?.getString("courseId")
        contentId = bundle?.getString("contentId")

        val call = dataclient?.getAttendances(contentId, courseId)
        call?.enqueue(object : Callback<ListGetAttendance> {
            override fun onFailure(call: Call<ListGetAttendance>?, t: Throwable?) {}
            override fun onResponse(call: Call<ListGetAttendance>?, response: Response<ListGetAttendance>?) {
                listGetAttendance.clear()
                if(response?.body()?.data?.listAttendance!=null){
                listGetAttendance.addAll(response.body()?.data?.listAttendance!!)
                adapter.notifyDataSetChanged()
                }
            }
        })

        recyclergetAttendance.layoutManager = LinearLayoutManager(context)
        recyclergetAttendance.adapter = adapter

        attentDialogFragment = AttentDialogFragment()
        attentDialogFragment?.onTrans(this)

        btn_done_attendance.setOnClickListener {
            MyAsync().execute()
            adapter.notifyDataSetChanged()
            activity?.onBackPressed()
        }

    }

    private fun getListMemberUserId(): ArrayList<PostAttendanceContent> {
        val listPostAttendance= ArrayList<PostAttendanceContent>()
        for (i in 0 until listGetAttendance.size) {
            if (!listGetAttendance[i].attendanced) {
                val id: String = listGetAttendance[i].userId
                listPostAttendance.add(PostAttendanceContent(id, listGetAttendance[i].comment))

            }
        }
        return listPostAttendance
    }

    inner class MyAsync : AsyncTask<Void, Void, ArrayList<PostAttendanceContent>>() {
        override fun doInBackground(vararg p0: Void?): ArrayList<PostAttendanceContent> {
            return getListMemberUserId()
        }

        override fun onPostExecute(result: ArrayList<PostAttendanceContent>?) {
            super.onPostExecute(result)

            val call1 = dataclient?.postAttendances(ListPostMemberUserId(contentId, result!!))
            call1?.enqueue(object : Callback<getTakeAttendance> {
                override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                    context?.showToast_Short(getString(R.string.Attendance_content_Success))
                    Log.d("namm", response?.body()?.code.toString())
                }

            })

        }
    }
}


