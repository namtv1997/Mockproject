package com.example.mockproject.contentScreen


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.mockproject.GroupFragment.GroupFragment
import com.example.mockproject.MainActivity
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.model.UserContent
import com.example.mockproject.model.modelGetCousre.Content
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import com.example.mockproject.util.swichFragment
import kotlinx.android.synthetic.main.fragment_create_content.*
import kotlinx.android.synthetic.main.fragment_edit_content.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditContentFragment : Fragment(), View.OnClickListener {

    var dataClient: DataClient? = null
    private var mStartDate = ""
    var spinner: Spinner? = null
    private var contentEdit: Content? = null
    var idGroup: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_content, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        contentEdit = bundle!!.getSerializable("sendContentToEditFragment") as Content
        initSpinner()
        getDataToUser(contentEdit!!)
        onChangeView()

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val currentDay = "$year/${month + 1}/$day"
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val dateToDay = sdf.parse(currentDay)

        tv_start_date_edit_content_ESC.setOnClickListener {
            startDate(sdf, dateToDay, year, month, day)
        }
        tv_end_date_edit_content_ESC.setOnClickListener {
            clickEndDate(sdf, year, month, day)
        }
        btn_done_edit_content_ESC.setOnClickListener(this)
        btn_cancel_edit_content_ESC.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.btn_done_edit_content_ESC -> { updateContent() }
            R.id.btn_cancel_edit_content_ESC -> clickCancel()
        }
    }

    fun clickCancel() {
        (activity as MainActivity).onBackPressed()
    }

    private fun clickEndDate(sdf: SimpleDateFormat,
                             year: Int,
                             month: Int,
                             day: Int) {
        if (tv_start_date_edit_content_ESC.text.isEmpty()) {
            context?.showAlertDialog(getString(R.string.msg_choose_date_start))
        } else {
            endDate(sdf, year, month, day)
        }
    }

    private fun getDataToUser(content: Content) {

        edt_title_edit_content.setText(content.title)
        spinner!!.setSelection(content.level)
        tv_start_date_edit_content_ESC.text = content.dateStart
        tv_end_date_edit_content_ESC.text = content.dateEnd
        edt_description_edit_content_ESC.setText(content.description.toString())
    }

    private fun endDate(sdf: SimpleDateFormat, year: Int, month: Int, day: Int) {

            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
                var dayOnCalendar = ""
                if (monthOfYear + 1 <10){
                    if(dayOfMonth < 10) {
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/0$dayOfMonth"
                    }else{
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/$dayOfMonth"
                    }
                }else {
                    if(dayOfMonth < 10) {
                        dayOnCalendar = "$_year/${monthOfYear + 1}/0$dayOfMonth"
                    }else{
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/$dayOfMonth"
                    }
                }
            val userPickEndDate = sdf.parse(dayOnCalendar)
            val startDate: String = tv_start_date_edit_content_ESC.text.toString()

            if (tv_start_date_edit_content_ESC.text.toString().isNotEmpty())
                when {
                userPickEndDate.after(sdf.parse(startDate)) -> {
                    tv_end_date_error_ESC.visibility = View.GONE
                    tv_end_date_edit_content_ESC.text = dayOnCalendar
                }
                userPickEndDate == sdf.parse(startDate) -> {
                    tv_end_date_error_ESC.visibility = View.GONE
                    tv_end_date_edit_content_ESC.text = dayOnCalendar
                }
                userPickEndDate.before(sdf.parse(startDate)) -> {
                    tv_end_date_error_ESC.setText(R.string.msg_choose_date_start)
                    tv_end_date_error_ESC.visibility = View.VISIBLE
                    tv_end_date_edit_content_ESC.text = ""
                }

            } else {
                context?.showAlertDialog(getString(R.string.msg_choose_date_start))
            }
        }, year, month, day)
        dpd.show()
    }

    private fun startDate(sdf: SimpleDateFormat, dateToDay: Date, year: Int, month: Int, day: Int) {
        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
            var dayOnCalendar = ""
            if (monthOfYear + 1 <10){
                if(dayOfMonth < 10) {
                    dayOnCalendar = "$_year/0${monthOfYear + 1}/0$dayOfMonth"
                }else{
                    dayOnCalendar = "$_year/0${monthOfYear + 1}/$dayOfMonth"
                }
            }else {
                if(dayOfMonth < 10) {
                    dayOnCalendar = "$_year/${monthOfYear + 1}/0$dayOfMonth"
                }else{
                    dayOnCalendar = "$_year/0${monthOfYear + 1}/$dayOfMonth"
                }
            }
            val userPickStartDate = sdf.parse(dayOnCalendar)

            when {
                userPickStartDate.after(dateToDay) -> {
                    tv_error_start_date_ESC.visibility = View.GONE
                    tv_start_date_edit_content_ESC.text = dayOnCalendar
                    mStartDate = dayOnCalendar
                }
                userPickStartDate.before(dateToDay) -> {
                    tv_start_date_edit_content_ESC.text = ""
                    tv_error_start_date_ESC.visibility = View.VISIBLE
                }
                userPickStartDate == dateToDay -> {
                    tv_error_start_date_ESC.visibility = View.GONE
                    tv_start_date_edit_content_ESC.text = dayOnCalendar
                    mStartDate = dayOnCalendar
                }
            }
        }, year, month, day)
        dpd.show()
    }

    private fun initSpinner() {
        spinner = this.sp_edit_content_ESC
        val adapter = ArrayAdapter(
                this.context,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.level)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
    }

    private fun onUpdate() {

        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val contentId = contentEdit?.id
        val contentUserId: String = contentEdit?.userId!!
        val title = edt_title_edit_content.text.toString()
        val dateStart = tv_start_date_edit_content_ESC.text.toString()
        val dateEnd = tv_end_date_edit_content_ESC.text.toString()
        //val level = sp_edit_content_ESC.selectedItem.toString().toInt()
        val level = when(sp_edit_content_ESC.selectedItem.toString()) {
            "Beginner" -> 1
            "Intermediate" -> 2
            else ->3
        }
        val description = edt_description_edit_content_ESC.text.toString()
        val userId = Constant.id
        val userContent = UserContent(contentUserId, title, description, "", dateStart, dateEnd, level, userId)
        val call = dataClient?.updateContent(contentId!!, userContent)
        call?.enqueue(object : Callback<BaseDataResponseServer<String>> {
            override fun onResponse(call: Call<BaseDataResponseServer<String>>?,
                                    response: Response<BaseDataResponseServer<String>>?) {
                if (response != null) {
                    if (response.isSuccessful)
                        clickCancel()
                    swichFragment(GroupFragment())
                }
            }
            override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {
        }
        })
    }

    private fun updateContent() {

        if (onInputData())
            onUpdate()
    }

    private fun onInputData(): Boolean {

        val notification: String = getString(R.string.msg_not_empty)
        var check = true

        if (edt_title_edit_content.text.isEmpty()) {
            check = false
            tv_error_title_ESC.text = notification
            tv_error_title_ESC.visibility = View.VISIBLE
        }
        if (sp_edit_content_ESC.selectedItem.toString().isEmpty()) {
            check = false
            context?.showAlertDialog(getString(R.string.msg_choose_date_start))
        }
        if (tv_start_date_edit_content_ESC.text.isEmpty()) {
            check = false
            tv_error_start_date_ESC.text = notification
            tv_error_start_date_ESC.visibility = View.VISIBLE
        }
        if (tv_end_date_edit_content_ESC.text.isEmpty()) {
            check = false
            tv_end_date_error_ESC.text = notification
            tv_end_date_error_ESC.visibility = View.VISIBLE
        }
        if (edt_description_edit_content_ESC.text.isEmpty()) {
            check = false
            tv_error_description_ESC.text = notification
            tv_error_description_ESC.visibility = View.VISIBLE
        }
        return check
    }

    private fun onChangeView() {

        edt_title_edit_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tv_error_title_ESC.visibility = View.GONE
                if (edt_title_edit_content.text.toString().length > 64) {
                    tv_error_title_ESC.text = getString(R.string.tv_error_title_too_create_content)
                    tv_error_title_ESC.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tv_start_date_edit_content_ESC.addTextChangedListener(TextChange(tv_error_start_date_ESC))

        tv_end_date_edit_content_ESC.addTextChangedListener(TextChange(tv_error_title_ESC))

        edt_description_edit_content_ESC.addTextChangedListener(TextChange(tv_error_description_ESC))

    }

    inner class TextChange(
            private var tv: TextView? = null
    ) : TextWatcher{

        override fun afterTextChanged(s: Editable?) {
            tv?.visibility = View.GONE
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

}
