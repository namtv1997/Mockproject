package com.example.mockproject.contentScreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.model.ListCreateContent
import com.example.mockproject.model.Posts
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.*
import kotlinx.android.synthetic.main.fragment_create_content.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class
CreateContentFragment : Fragment() {
    var dataClient: DataClient? = null
    var spinner: Spinner? = null
    var idGroup: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idGroup = arguments!!.getString(getString(R.string.idGroupToCreateContentFragment))

        btn_done_create_content.setOnClickListener { doneCreateContent() }
        tv_start_date_create_content.setOnClickListener {
            context?.startPickerDialog(tv_error_start_date_create_content, tv_start_date_empty, tv_start_date_create_content)
        }
        tv_end_date_create_content.setOnClickListener {
            if (tv_start_date_create_content.text.isEmpty()) {
                context?.showAlertDialog(getString(R.string.msg_choose_date_start))
            } else {
                context?.endPickerDialog(tv_error_end_date_create_content, tv_end_date_empty, tv_end_date_create_content)
            }
        }
        btn_cancel_create_content.setOnClickListener { cancel() }
        addTextChange(tv_start_date_create_content, tv_start_date_empty, tv_start_date_empty)
        addTextChange(edt_title_create_content, tv_title_empty, tv_title_empty)
        addTextChange(edt_description_create_content, tv_description_empty, tv_description_empty)
        addTextChange(tv_end_date_create_content, tv_end_date_empty, tv_end_date_empty)
        edt_title_create_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tv_over_quantity.visibility = View.GONE
                if (edt_title_create_content.text.toString().length > 64) {
                    tv_over_quantity.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        spinnerOnclick()

    }


    private fun spinnerOnclick() {
        spinner = this.sp_create_content
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.level))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
        sp_create_content.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun connectServerisCaptain() {
        dataClient = RetrofitClient.getClient()!!.create(DataClient::class.java)
        val userId = Constant.id
        val title = edt_title_create_content.text.toString()
        val dateStart = tv_start_date_create_content.text.toString()
        val dateEnd = tv_end_date_create_content.text.toString()
        //val level = sp_create_content.selectedItem.toString().toInt()
        val level = when (sp_create_content.selectedItem.toString()) {
            "Beginner" -> 1
            "Intermediate" -> 2
            else -> 3
        }
        val description = edt_description_create_content.text.toString()
        Log.d("nmm", "$title,$dateEnd,$dateStart,$level")
        val pots = Posts(
                title,
                dateStart,
                dateEnd,
                level,
                description,
                "",
                userId, idGroup
        )

        val call = dataClient?.createContent(pots, Constant.idCaptainWhenClickGroup!!)
        call?.enqueue(object : Callback<ListCreateContent> {
            override fun onResponse(call: Call<ListCreateContent>?, response: Response<ListCreateContent>?) {

                context?.showAlertDialog(getString(R.string.Create_content_Success))
                activity?.onBackPressed()

            }

            override fun onFailure(call: Call<ListCreateContent>?, t: Throwable?) {}
        })
    }

    private fun connectServerisMember() {
        dataClient = RetrofitClient.getClient()!!.create(DataClient::class.java)
        val userId = Constant.id
        val title = edt_title_create_content.text.toString()
        val dateStart = tv_start_date_create_content.text.toString()
        val dateEnd = tv_end_date_create_content.text.toString()
        //val level = sp_create_content.selectedItem.toString().toInt()
        val level = when (sp_create_content.selectedItem.toString()) {
            "Beginner" -> 1
            "Intermediate" -> 2
            else -> 3
        }
        val description = edt_description_create_content.text.toString()
        Log.d("nmm", "$title,$dateEnd,$dateStart,$level")
        val pots = Posts(
                title,
                dateStart,
                dateEnd,
                level,
                description,
                "",
                userId, idGroup
        )

        val call = dataClient?.createContent(pots, Constant.id)
        call?.enqueue(object : Callback<ListCreateContent> {
            override fun onResponse(call: Call<ListCreateContent>?, response: Response<ListCreateContent>?) {
                if (response?.body()?.code == 0) {
                    context?.showAlertDialog(getString(R.string.Create_content_approve))
                }
                activity?.onBackPressed()
            }

            override fun onFailure(call: Call<ListCreateContent>?, t: Throwable?) {}
        })
    }

    private fun doneCreateContent() {

        val titleCreateContent: String = edt_title_create_content.text.toString()
        val startDateCreateContent: String = tv_start_date_create_content.text.toString()
        val endDateCreateContent: String = tv_end_date_create_content.text.toString()
        val level1 = sp_create_content.selectedItem.toString()
        val descriptionCreateContent: String = edt_description_create_content.text.toString()

        if (titleCreateContent.isEmpty()) {
            tv_title_empty.visibility = View.VISIBLE
        } else if (level1.isEmpty()) {
            context?.showAlertDialog(getString(R.string.tv_error_level_create_content))
        } else if (startDateCreateContent.isEmpty()) {
            tv_start_date_empty.visibility = View.VISIBLE
            tv_error_start_date_create_content.visibility = View.GONE
        } else if (endDateCreateContent.isEmpty()) {
            tv_end_date_empty.visibility = View.VISIBLE
            tv_error_end_date_create_content.visibility = View.GONE
        } else if (descriptionCreateContent.isEmpty()) {
            tv_description_empty.visibility = View.VISIBLE
        } else {
            if (Constant.id == Constant.idCaptainWhenClickGroup) {

                connectServerisCaptain()
            } else {

                connectServerisMember()
            }

        }
    }

    private fun cancel() {
        activity?.onBackPressed()
    }
}
