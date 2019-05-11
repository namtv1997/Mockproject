package com.example.mockproject.model

import android.os.CountDownTimer
import android.util.Log
import com.example.mockproject.loginScreen.CallBackTimeCountDown


class Timer(var count: Int = 0, var millisInFuture: Long, var countDownInterval: Long, var callBackTimeCountDown: CallBackTimeCountDown) :
        CountDownTimer(millisInFuture, countDownInterval) {

    override fun onFinish() {
        Log.d("BBB", "Xong")
        callBackTimeCountDown?.evenUserInterraction()
    }

    override fun onTick(p0: Long) {
        count += 1
        Log.d("BBB", count.toString())
    }

}