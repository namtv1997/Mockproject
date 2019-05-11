package com.example.mockproject.Dialog


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Gravity
import android.view.WindowManager
import android.graphics.Point
import android.util.Log
import com.example.mockproject.ListAttendance.Model.Attendances
import com.example.mockproject.R
import kotlinx.android.synthetic.main.fragment_attent.*

@SuppressLint("ValidFragment")
class AttentDialogFragment : DialogFragment() {
    private var mPassComment : PassComment? =null
    private var mAttendances : Attendances? =null

    interface PassComment{
        fun onPass(attendances: Attendances?)
    }

    fun onTrans(passComment: PassComment){
        mPassComment = passComment
    }

    fun fillData(attendances: Attendances?){
        mAttendances = attendances
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit.setOnClickListener {
            val comment = attenEditText.text.toString()
            mAttendances?.comment = comment
            mPassComment?.onPass(mAttendances)
            attenEditText.setText("")
            dismiss()
        }
    }

    override fun onResume() {
        val window = dialog.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 0.75).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }
}
