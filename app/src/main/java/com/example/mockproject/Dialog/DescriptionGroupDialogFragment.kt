package com.example.mockproject.Dialog


import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.*
import android.widget.TextView
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance

import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelCreateCourse.CreateCourse
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.showAlertDialog
import kotlinx.android.synthetic.main.fragment_description_group_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class DescriptionGroupDialogFragment : Fragment() {
    private var dataClient: DataClient? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_description_group_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtdescriptionGroup.setText(Constant.description)
        btnCancelDescription.setOnClickListener {
            activity?.onBackPressed()
        }
        if (Constant.id == Constant.idCaptainWhenClickGroup) {
            edtdescriptionGroup.isEnabled = true
            btnEditDescription.visibility = View.VISIBLE
            btnEditDescription.setOnClickListener {
                val editTitle = edtdescriptionGroup.text.toString()
                val post = CreateCourse("", editTitle, Constant.idCaptainWhenClickGroup!!, "", "")
                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                val call = dataClient?.putCourse(post, Constant.idGroupWhenClickGroup, Constant.idCaptainWhenClickGroup!!, 3)
                call?.enqueue(object : Callback<getTakeAttendance> {
                    override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                        if (response?.body()?.code == 0) {
                            context?.showAlertDialog("Sửa description thành công")
                             activity?.onBackPressed()
                        }
                    }

                })
            }
        }
    }


}
