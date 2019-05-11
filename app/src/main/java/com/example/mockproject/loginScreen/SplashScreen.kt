package com.example.mockproject.loginScreen


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.util.swichFragment
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_splash_screen.*

class SplashScreen : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fromTop = AnimationUtils.loadAnimation(activity, R.anim.fromtop)
        activity?.logoNTQ?.animation = fromTop
        val fadingCircle = FadingCircle()
        progresbarCircle.setIndeterminateDrawable(fadingCircle)

        val timeToFinishSplashScreen = Thread() {
            run {
                var waited = 0
                while (waited < Constant.timeToSplashScreen) {
                    try {
                        Thread.sleep(Constant.oneHundredMiliSecondLong)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    waited += Constant.oneHundredMiliSecond
                }
                swichFragment(LoginFragment())
            }
        }
        timeToFinishSplashScreen.start()
    }
}
