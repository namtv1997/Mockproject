package com.example.mockproject.util

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.mockproject.R
import com.example.mockproject.R.id.frame_layout_main
import com.example.mockproject.common.Constant
import com.example.mockproject.loginScreen.LoginFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import java.text.SimpleDateFormat
import java.util.*





fun AppCompatActivity.swichFragment(fragment: Fragment) {
    val name: String = fragment.javaClass.name
    val ft: FragmentTransaction = supportFragmentManager.beginTransaction().apply {
        replace(R.id.frame_layout_main, fragment)
        addToBackStack(name)
        setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
        commitAllowingStateLoss()
    }
}

fun Fragment.swichFragment(fragment: Fragment, idFrame: Int = R.id.frame_layout_main) {
    val name: String = fragment.javaClass.name
    val ft: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
    ft?.replace(idFrame, fragment)
    ft?.addToBackStack(name)
    ft?.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
    ft?.commitAllowingStateLoss()
}

fun Fragment.addFragment(fragment: Fragment, idFrame: Int = R.id.frame_layout_main) {
    val name: String = fragment.javaClass.name
    val ft: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
    ft?.add(idFrame, fragment)
    ft?.addToBackStack(name)
    ft?.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
    ft?.commitAllowingStateLoss()
}

fun Context.showCustomDialog(message2: String){
    val dialog= Dialog(this)
    dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.fragment_dialog_choose)
    dialog.show()
    val textView = dialog.findViewById<TextView>(R.id.tv_message)
    textView.text=message2
    val textViewYes = dialog.findViewById<TextView>(R.id.tv_yes)
    textViewYes.setOnClickListener {
        Toast.makeText(this, "no", Toast.LENGTH_LONG).show()
    }
    val textViewNo = dialog.findViewById<TextView>(R.id.tv_no)
    textViewNo.setOnClickListener {
        Toast.makeText(this, "yes", Toast.LENGTH_LONG).show()
    }
    dialog.show()
}
fun Context.loadingProgressBar(){
    val dialog = Dialog(this)
    dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_progressbar)
    dialog.show()
}
fun Context.showAlertDialog(message1: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle("Thông báo")
    builder.setIcon(R.drawable.icons8_notification)
    builder.setMessage(message1)
    builder.setNegativeButton("Ok") { dialog, _ ->
        dialog.dismiss()
    }

    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()

}
fun addTextChange(view: TextView, textVisible: TextView, textGone: TextView){
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(p0: Editable?) {
        if (p0!!.isEmpty()) {
            textVisible.visibility = View.VISIBLE
        } else {
            textGone.visibility = View.GONE
        }
    }
})
}
var mStartDate = "2019/01/08"
fun Context.startPickerDialog(textError:TextView,textDateEmpty:TextView,textDate:TextView){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val currentDay = "$year/${month + 1}/$day"
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    val dateToDay = sdf.parse(currentDay)
    val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
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
                textError.visibility = View.GONE
                textDate.text = dayOnCalendar
                mStartDate = dayOnCalendar
            }
            userPickStartDate.before(dateToDay) -> {
                textDate.text = ""
                textDateEmpty.visibility = View.GONE
                textError.visibility = View.VISIBLE
            }
            userPickStartDate == dateToDay -> {
                textError.visibility = View.GONE
                textDate.text = dayOnCalendar
                mStartDate = dayOnCalendar
            }
        }
    }, year, month, day)
    dpd.show()
}
fun Context.endPickerDialog(textError:TextView,textDateEmpty:TextView,textDate:TextView) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val currentDay = "$year/${month + 1}/$day"
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    val dateToDay = sdf.parse(currentDay)
    val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
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

        when {
            userPickEndDate.after(dateToDay) -> {
                textError.visibility = View.GONE
                textDate.text = dayOnCalendar
                if (userPickEndDate.before(sdf.parse(mStartDate))) {
                    textDate.text = ""
                    textDateEmpty.visibility = View.GONE
                    textError.visibility = View.VISIBLE
                }
            }
            userPickEndDate.before(dateToDay) -> {
                textDate.text = ""
                textDateEmpty.visibility = View.GONE
                textError.visibility = View.VISIBLE
            }
            userPickEndDate == dateToDay -> {
                textError.visibility = View.GONE
                textDate.text = dayOnCalendar
            }
        }
    }, year, month, day)
    dpd.show()
}

fun Activity.showAlertDialog(message1: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle("Thông báo")
    builder.setIcon(R.drawable.icon_error)
    builder.setMessage(message1)
    builder.setNegativeButton("Cancel") { dialog, _ ->
        dialog.dismiss()
    }

    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}
fun Fragment.joinToGroupOnClick(){

}

fun Context. showToast_Short(thongbao: String){
    Toast.makeText(this, thongbao, Toast.LENGTH_SHORT).show()
}
fun Context.date(textView:TextView){

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val currentDay = "$year/${month + 1}/$day"
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    val dateToDay = sdf.parse(currentDay)
    val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, _year, monthOfYear, dayOfMonth ->
        val dayOnCalendar = "$_year/${monthOfYear + 1}/$dayOfMonth"
       textView.text=dayOnCalendar


    }, year, month, day)
    dpd.show()
}



