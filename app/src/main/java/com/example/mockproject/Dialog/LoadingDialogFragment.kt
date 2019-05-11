package com.example.mockproject.Dialog

import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import com.example.mockproject.R

class LoadingDialogFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_progressbar,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {

        var window = dialog.window
        var size = Point()
        var display = window.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout((size.x * 0.75).toInt(),WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        super.onResume()
    }
}