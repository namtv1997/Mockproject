package com.example.mockproject.GroupFragment.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.text.*
import android.text.style.ImageSpan
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.mockproject.GroupFragment.GroupFragment
import com.example.mockproject.GroupFragment.Pending.ListPutPending
import com.example.mockproject.GroupFragment.Pending.Pending
import com.example.mockproject.GroupFragment.Pending.Putpending
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.endPickerDialog
import com.example.mockproject.util.showAlertDialog
import com.example.mockproject.util.startPickerDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.header_item_list_content.view.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ContentRequestCreateAdapter(var listContentRequestCreate: ArrayList<Pending>, var groupFragment: GroupFragment) :
        RecyclerSwipeAdapter<ContentRequestCreateAdapter.ViewHolderContentRequest>() {
    private var dataClient: DataClient? = null
    private var mStartDate = "2019/03/08"
    private lateinit var context: Context

    private val itemStateArray = SparseBooleanArray()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ContentRequestCreateAdapter.ViewHolderContentRequest {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_content,
                parent, false)
        return ContentRequestCreateAdapter.ViewHolderContentRequest(view)

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: ViewHolderContentRequest, position: Int) {
        val pending = listContentRequestCreate[position]
        viewHolder.itemView.swipe.showMode = SwipeLayout.ShowMode.PullOut
        viewHolder.itemView.swipe.addDrag(SwipeLayout.DragEdge.Right,
                viewHolder.itemView.swipe.findViewById(R.id.layout_swipe_right))

        viewHolder.itemView.swipe.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onOpen(layout: SwipeLayout?) {

            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}

            override fun onStartOpen(layout: SwipeLayout?) {}

            override fun onStartClose(layout: SwipeLayout?) {}

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {}

            override fun onClose(layout: SwipeLayout?) {}

        })
        viewHolder.itemView.tvTittle.text = pending.title
        viewHolder.itemView.tvDateCreate.text = "Date : " + pending.dateStart
        viewHolder.itemView.tvDateStart.text = pending.dateStart
        viewHolder.itemView.tvDateEnd.text = pending.dateEnd
        viewHolder.itemView.tvSummaryContent.text = pending.description
        viewHolder.itemView.tvDetailContant.text = pending.description
        viewHolder.itemView.btnAttendance.visibility = View.GONE
        viewHolder.itemView.tvUser.visibility = View.VISIBLE
        viewHolder.itemView.tvUser.text = pending.userFullName
        Picasso.with(context).load(pending.avatar).into(viewHolder.itemView.AvtContent)
        viewHolder.itemView.tvStart.visibility = View.VISIBLE
        viewHolder.itemView.tvEnd.visibility = View.VISIBLE

        justify(viewHolder.itemView.tvDetailContant)
        if (Constant.id==Constant.idCaptainWhenClickGroup){
            viewHolder.itemView.btnApprove.visibility=View.VISIBLE
            viewHolder.itemView.btnDeleteContent.visibility=View.VISIBLE
        }
        viewHolder.itemView.btnApprove.setOnClickListener {
            dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
            val call = dataClient?.deletePending(pending.id, 0)
            call?.enqueue(object : Callback<getTakeAttendance> {
                override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                    if (response?.body()?.code == 0) {
                        mItemManger.removeShownLayouts(viewHolder.itemView.swipe)
                        listContentRequestCreate.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listContentRequestCreate.size)
                        mItemManger.closeAllItems()
                        context.showAlertDialog("Success!!")
                        //update list content
                        groupFragment.getProfileGroup()

                    }

                }
            })
        }
        viewHolder.itemView.btnDeleteContent.setOnClickListener {
            val dialog = Dialog(context)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.fragment_dialog_choose)
            dialog.show()
            val textView = dialog.findViewById<TextView>(R.id.tv_message)
            textView.text = "Ẩn content!"
            val textViewYes = dialog.findViewById<TextView>(R.id.tv_yes)
            textViewYes.setOnClickListener {
                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                val call = dataClient?.deletePending(pending.id, 1)
                call?.enqueue(object : Callback<getTakeAttendance> {
                    override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                        if (response?.body()?.code == 0) {
                            mItemManger.removeShownLayouts(viewHolder.itemView.swipe)
                            listContentRequestCreate.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, listContentRequestCreate.size)
                            mItemManger.closeAllItems()
                            context.showAlertDialog("Success!!")
                        }
                        dialog.cancel()

                    }
                })
            }
            val textViewNo = dialog.findViewById<TextView>(R.id.tv_no)
            textViewNo.setOnClickListener {
                dialog.cancel()

            }
        }
        viewHolder.itemView.tvDateStart.setOnClickListener {

            Log.e("msg", "run here")
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val currentDay = "$year/${month + 1}/$day"
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val dateToDay = sdf.parse(currentDay)
            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
                var dayOnCalendar = "$_year/${monthOfYear + 1}/$dayOfMonth"
                if (monthOfYear + 1 < 10) {
                    if(dayOfMonth < 10) {
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/0$dayOfMonth"
                    }else{
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/$dayOfMonth"
                    }
                }

                val userPickStartDate = sdf.parse(dayOnCalendar)
                when {
                    userPickStartDate.after(dateToDay) -> {


                        viewHolder.itemView.tvDateStart.text = dayOnCalendar
                        mStartDate = dayOnCalendar
                    }
                    userPickStartDate.before(dateToDay) -> {
                        context.showAlertDialog("Bạn không được chọn ngày nhỏ hơn hôm nay")
                    }
                    userPickStartDate == dateToDay -> {


                        viewHolder.itemView.tvDateStart.text = dayOnCalendar
                        mStartDate = dayOnCalendar
                    }
                }
            }, year, month, day)
            dpd.show()

        }
        viewHolder.itemView.tvDateEnd.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val currentDay = "$year/${month + 1}/$day"
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val dateToDay = sdf.parse(currentDay)
            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
                var dayOnCalendar = "$_year/${monthOfYear + 1}/$dayOfMonth"
                if (monthOfYear + 1 < 10) {
                    if(dayOfMonth < 10) {
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/0$dayOfMonth"
                    }else{
                        dayOnCalendar = "$_year/0${monthOfYear + 1}/$dayOfMonth"
                    }
                }
                val userPickEndDate = sdf.parse(dayOnCalendar)

                when {
                    userPickEndDate.after(dateToDay) -> {

                        viewHolder.itemView.tvDateEnd.text = dayOnCalendar
                        if (userPickEndDate.before(sdf.parse(mStartDate))) {
                            context.showAlertDialog("Bạn không được chọn ngày nhỏ hơn hôm nay và ngày bắt đầu!")

                        }
                    }
                    userPickEndDate.before(dateToDay) -> {
                        context.showAlertDialog("Bạn không được chọn ngày nhỏ hơn hôm nay")

                    }
                    userPickEndDate == dateToDay -> {
                        viewHolder.itemView.tvDateEnd.text = dayOnCalendar
                    }
                }
            }, year, month, day)
            dpd.show()

        }
        viewHolder.itemView.tvDateStart.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val putPending = Putpending(viewHolder.itemView.tvDateStart.text.toString(), "")
                Log.e("msg", viewHolder.itemView.tvDateStart.text.toString())
                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                val call = dataClient?.putPending(putPending, pending.id, 0)
                call?.enqueue(object : Callback<ListPutPending> {
                    override fun onFailure(call: Call<ListPutPending>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ListPutPending>?, response: Response<ListPutPending>?) {

                    }


                })
            }
        })
        viewHolder.itemView.tvDateEnd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val putPending = Putpending("",viewHolder.itemView.tvDateEnd.text.toString())
                Log.e("msg", viewHolder.itemView.tvDateStart.text.toString())
                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                val call = dataClient?.putPending(putPending, pending.id, 1)
                call?.enqueue(object : Callback<ListPutPending> {
                    override fun onFailure(call: Call<ListPutPending>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ListPutPending>?, response: Response<ListPutPending>?) {

                    }


                })
            }
        })
        var isCheckExpanded = itemStateArray.get(position, false)
        var isExpand = false
        viewHolder.itemView.tvSummaryContent.visibility = if (!isCheckExpanded) View.VISIBLE else View.GONE
        viewHolder.itemView.tvDetailContant.visibility = if (isCheckExpanded) View.VISIBLE else View.GONE
        if (Constant.id == Constant.idCaptainWhenClickGroup) {
            viewHolder.itemView.btnAttendance.visibility = View.GONE
        }

        isExpand = if (isCheckExpanded) {
            viewHolder.itemView.buttonShowdetail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_up_black, 0, 0, 0)
            viewHolder.itemView.buttonShowdetail.visibility=View.GONE
            true
        } else {
            if (viewHolder.itemView.tvDetailContant.text.toString().length >= 200)
                viewHolder.itemView.buttonShowdetail.visibility=View.VISIBLE
                viewHolder.itemView.buttonShowdetail.text = context.getString(R.string.viewAll)

            false
        }
        viewHolder.itemView.buttonShowdetail.setOnClickListener {

            if (!itemStateArray.get(position, false)) {
                itemStateArray.put(position, true)
            } else {
                itemStateArray.put(position, false)
            }
            isCheckExpanded = !isExpand
            notifyItemChanged(viewHolder.adapterPosition)
        }

    }


    override fun getSwipeLayoutResourceId(position: Int): Int = R.id.swipe

    override fun getItemCount(): Int = listContentRequestCreate.size
    private fun justify(textView: TextView) {

        val isJustify = AtomicBoolean(false)
        val textString = textView.text.toString()
        val textPaint = textView.paint
        val builder = SpannableStringBuilder()

        textView.post {
            if (!isJustify.get()) {

                val lineCount = textView.lineCount
                val textViewWidth = textView.width

                for (i in 0 until lineCount) {
                    val lineStart = textView.layout.getLineStart(i)
                    val lineEnd = textView.layout.getLineEnd(i)
                    val lineString = textString.substring(lineStart, lineEnd)

                    if (i == lineCount - 1) {
                        builder.append(SpannableString(lineString))
                        break
                    }

                    val trimSpaceText = lineString.trim { it <= ' ' }
                    val removeSpaceText = lineString.replace(" ".toRegex(), "")
                    val removeSpaceWidth = textPaint.measureText(removeSpaceText)
                    val spaceCount = (trimSpaceText.length - removeSpaceText.length).toFloat()
                    val eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount
                    val spannableString = SpannableString(lineString)

                    for (j in 0 until trimSpaceText.length) {
                        val c = trimSpaceText[j]
                        if (c == ' ') {
                            val drawable = ColorDrawable(0x00ffffff)
                            drawable.setBounds(0, 0, eachSpaceWidth.toInt(), 0)
                            val span = ImageSpan(drawable)
                            spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }
                    }
                    builder.append(spannableString)
                }
                textView.text = builder
                isJustify.set(true)
            }
        }
    }

    class ViewHolderContentRequest(itemView: View) : RecyclerView.ViewHolder(itemView)

}