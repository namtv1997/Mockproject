package com.example.mockproject.listContent

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.mockproject.ListAttendance.ipmCallBackAttendanceClick
import com.example.mockproject.R
import com.example.mockproject.Sprint5.createEventScreen.view.IpmCallCreateEventFragment
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelGetCousre.Content
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import com.example.mockproject.util.showCustomDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.header_item_list_content.view.*
import kotlinx.android.synthetic.main.header_list_group.view.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list_request_join_togroup.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ListContentAdapter(var listContent: ArrayList<Content>,
                         var editContentOnClick: IpmCallBackEditContentOnClick,
                         var btnAttendace: ipmCallBackAttendanceClick,
                         var btnCreateEventOnClick: IpmCallCreateEventFragment,
                         var idGroup: String) :
        RecyclerSwipeAdapter<ListContentAdapter.ViewHolderContent>() {
    private var dataClient: DataClient? = null

    private lateinit var context: Context

    private val itemStateArray = SparseBooleanArray()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderContent {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_content,
                parent, false)
        return ViewHolderContent(view)
    }

    override fun getSwipeLayoutResourceId(position: Int) = R.id.swipe

    override fun getItemCount(): Int = listContent.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolderContent, position: Int) {
        val content = listContent[position]
        viewHolder.itemView.swipe.showMode = SwipeLayout.ShowMode.PullOut
        viewHolder.itemView.swipe.addDrag(SwipeLayout.DragEdge.Right,
                viewHolder.itemView.swipe.findViewById(R.id.layout_swipe_right))
        viewHolder.itemView.swipe.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onOpen(layout: SwipeLayout?) {

            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
            }

            override fun onStartOpen(layout: SwipeLayout?) {
            }

            override fun onStartClose(layout: SwipeLayout?) {
            }

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
            }

            override fun onClose(layout: SwipeLayout?) {
            }

        })
        if (Constant.id == Constant.idCaptainWhenClickGroup) {
            viewHolder.itemView.btnCreateEvent.visibility = View.VISIBLE
            viewHolder.itemView.btnEditcontent.visibility = View.VISIBLE
            viewHolder.itemView.btnDeleteContent.visibility = View.VISIBLE
        }else {

            if (Constant.id != (content.userId)) {
                viewHolder.itemView.btnDeleteContent.visibility = View.GONE
                viewHolder.itemView.btnEditcontent.visibility = View.GONE
            }else{
                viewHolder.itemView.btnDeleteContent.visibility = View.VISIBLE
                viewHolder.itemView.btnEditcontent.visibility = View.VISIBLE
            }
        }
        viewHolder.itemView.tvTittle.text = content.title
        viewHolder.itemView.tvDateCreate.text = "Date : " + content.dateStart
        viewHolder.itemView.tvDateStart.text = "Start : " + content.dateStart
        viewHolder.itemView.tvDateEnd.text = "End: " + content.dateEnd
        viewHolder.itemView.tvSummaryContent.text = content.description
        viewHolder.itemView.tvDetailContant.text = content.description
        Picasso.with(context).load(content.userCreate?.avatar).into(viewHolder.itemView.AvtContent)

        val startdate = content.dateStart
        val enddate = content.dateEnd
        val getStartdate = convertStringToDate(startdate)
        val getEnddate = convertStringToDate(enddate)

        val dateCurrent = Calendar.getInstance().getTime()


       justify(viewHolder.itemView.tvDetailContant)


        when (content.level) {
            1 -> viewHolder.itemView.tvLevel.text = context.getString(R.string.beginner)
            2 -> viewHolder.itemView.tvLevel.text = context.getString(R.string.intermediate)
            3 -> viewHolder.itemView.tvLevel.text = context.getString(R.string.advance)
        }
        //viewHolder.itemView.tvAvtContent.text = content.title.subSequence(0, 1).toString().toUpperCase()

        viewHolder.itemView.btnDeleteContent.setOnClickListener {
            val dialog = Dialog(context)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.fragment_dialog_choose)
            dialog.show()
            val textView = dialog.findViewById<TextView>(R.id.tv_message)
            textView.text = "Delete content!!"
            val textViewYes = dialog.findViewById<TextView>(R.id.tv_yes)
            textViewYes.setOnClickListener {
                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                var call = dataClient?.deleteContent(content.id, Constant.id)
                call?.enqueue(object : Callback<BaseDataResponseServer<Any>> {
                    override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<BaseDataResponseServer<Any>>?,
                                            response: Response<BaseDataResponseServer<Any>>?) {
                        mItemManger.removeShownLayouts(viewHolder.itemView.swipe)
                        listContent.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listContent.size)
                        mItemManger.closeAllItems()
                        context.showAlertDialog("Success!!")

                        dialog.cancel()

                    }
                })

            }
            val textViewNo = dialog.findViewById<TextView>(R.id.tv_no)
            textViewNo.setOnClickListener {
                dialog.cancel()

            }
        }
        viewHolder.itemView.btnEditcontent.setOnClickListener {
            editContentOnClick.btnEditContentOnClick(listContent[position], idGroup)
        }

        viewHolder.itemView.btnAttendance.setOnClickListener {
            btnAttendace.onClickBtnAttendance(listContent[position].id)
        }


        viewHolder.itemView.btnCreateEvent.setOnClickListener {
            btnCreateEventOnClick.btnCreateEventOnClick(listContent[position].id)
        }
        var isCheckExpanded = itemStateArray.get(position, false)
        var isExpand = false
        viewHolder.itemView.tvSummaryContent.visibility = if (!isCheckExpanded) View.VISIBLE else View.GONE
        viewHolder.itemView.tvDetailContant.visibility = if (isCheckExpanded) View.VISIBLE else View.GONE
        if (Constant.id == Constant.idCaptainWhenClickGroup)
            if(
                    ( getStartdate.before(dateCurrent) && getEnddate.after(dateCurrent) )
                    || dateCurrent == getStartdate
                    || dateCurrent == getEnddate
                    ){
                viewHolder.itemView.btnAttendance.visibility = View.VISIBLE

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

    class ViewHolderContent(itemView: View) : RecyclerView.ViewHolder(itemView)
    private fun convertStringToDate(date: String): Date{
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
        var d: Date? = null
        try {
            d = sdf.parse(date);
        } catch (ex: ParseException) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        return d!!
    }
}
