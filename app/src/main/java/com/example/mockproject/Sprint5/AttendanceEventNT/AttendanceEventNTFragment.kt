package com.example.mockproject.Sprint5.AttendanceEventNT


import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.Dialog.AttentDialogEventFragment
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance
import com.example.mockproject.R
import com.example.mockproject.Sprint5.AttendanceEventNT.Adapter.ListAttendanceEventAdapter
import com.example.mockproject.Sprint5.AttendanceEventNT.Model.AttendancesEvent
import com.example.mockproject.Sprint5.AttendanceEventNT.Model.ListGetAttendanceEvent
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelPostAttendanceEvent.ListPostMemberUserIdEvent
import com.example.mockproject.model.modelPostAttendanceEvent.PostAttendanceEvent
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.showToast_Short
import kotlinx.android.synthetic.main.fragment_attendance.*
import kotlinx.android.synthetic.main.fragment_attendance_event_nt.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceEventNTFragment : Fragment(), ListAttendanceEventAdapter.ClickShowDialogEvent, AttentDialogEventFragment.PassCommentEvent {
    private var attentDialogEventFragment: AttentDialogEventFragment? = null
    private var eventId: String = ""

    override fun onPassEvent(attendancesEvent: AttendancesEvent?) {
        for (i in 0 until listGetAttendanceEvent.size) {
            if (listGetAttendanceEvent[i].userId == attendancesEvent?.userId) {
                listGetAttendanceEvent.removeAt(i)
                listGetAttendanceEvent.add(i, attendancesEvent)
            }
        }
    }

    override fun onClick(attentModelEvent: AttendancesEvent) {
        attentDialogEventFragment?.fillDataEvent(attentModelEvent)
        attentDialogEventFragment?.show(activity?.supportFragmentManager, "")
    }




    private var listGetAttendanceEvent = ArrayList<AttendancesEvent>()

    private lateinit var adapter: ListAttendanceEventAdapter

    private var dataclient = RetrofitClient.getClient()?.create(DataClient::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_attendance_event_nt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle = arguments
        eventId = bundle?.getString("EventIdFromDetailToAttendance")!!

        adapter = ListAttendanceEventAdapter(listGetAttendanceEvent)
        adapter.showDialogEvent(this)
        val call = dataclient?.getAttendancesEvent(eventId, Constant.idGroupWhenClickGroup)

        call?.enqueue(object : Callback<ListGetAttendanceEvent> {
            override fun onFailure(call: Call<ListGetAttendanceEvent>?, t: Throwable?) {}
            override fun onResponse(call: Call<ListGetAttendanceEvent>?, response: Response<ListGetAttendanceEvent>?) {
                Log.e("msg", "get:${response?.body()?.code.toString()}")
                listGetAttendanceEvent.clear()
                if (response?.body()?.data?.listAttendanceEvent != null) {
                    listGetAttendanceEvent.addAll(response.body()?.data?.listAttendanceEvent!!)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        recyclergetAttendanceEvent.layoutManager = LinearLayoutManager(context)
        recyclergetAttendanceEvent.adapter = adapter
        attentDialogEventFragment = AttentDialogEventFragment()
        attentDialogEventFragment?.onTransEvent(this)
        btn_done_attendance_event.setOnClickListener {
            MyAsync().execute()
            adapter.notifyDataSetChanged()
            activity?.onBackPressed()
        }
    }

    private fun getListMemberUserIdEvent(): ArrayList<PostAttendanceEvent> {
        val listPostAttendanceEvent = ArrayList<PostAttendanceEvent>()
        for (i in 0 until listGetAttendanceEvent.size) {
            if (!listGetAttendanceEvent[i].attendanced) {
                val id: String = listGetAttendanceEvent[i].userId
                listPostAttendanceEvent.add(PostAttendanceEvent(id, listGetAttendanceEvent[i].comment))
            }
        }
        return listPostAttendanceEvent
    }

    inner class MyAsync : AsyncTask<Void, Void, ArrayList<PostAttendanceEvent>>() {
        override fun doInBackground(vararg p0: Void?): ArrayList<PostAttendanceEvent> {
            return getListMemberUserIdEvent()
        }

        override fun onPostExecute(result: ArrayList<PostAttendanceEvent>?) {
            super.onPostExecute(result)

            val call1 = dataclient?.postAttendancesEvent(ListPostMemberUserIdEvent(eventId, result!!))

            call1?.enqueue(object : Callback<getTakeAttendance> {
                override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                    context?.showToast_Short(getString(R.string.Attendance_content_Success))

                }
            })
        }
    }
}
