package com.example.mockproject.Dialog


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.R
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.dialog_progressbar_custom.*


class ProgressBarDialog : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_progressbar_custom,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fadingCircle = FadingCircle()
        spin_kit.setIndeterminateDrawable(fadingCircle)
    }

}


