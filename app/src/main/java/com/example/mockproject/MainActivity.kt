package com.example.mockproject

import android.app.Dialog
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.example.mockproject.GroupFragment.setting.set_user.SetUser
import com.example.mockproject.GroupFragment.GroupFragment
import com.example.mockproject.Sprin7.SettingFragment
import com.example.mockproject.common.Constant
import com.example.mockproject.contentScreen.CreateContentFragment
import com.example.mockproject.homeScreen.HomeFragment
import com.example.mockproject.homeScreen.listGroup.ListGroupFragment
import com.example.mockproject.loginScreen.CallBackTimeCountDown
import com.example.mockproject.loginScreen.LoginFragment
import com.example.mockproject.loginScreen.SplashScreen
import com.example.mockproject.model.Timer
import com.example.mockproject.util.showAlertDialog
import com.example.mockproject.util.swichFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class MainActivity : AppCompatActivity(),
        CallBackTimeCountDown,
        SetUser.InteractionListener,
        CheckConnect.ConnectionReceiverListener,GroupFragment.InteractionListener,SettingFragment.InteractionListener {

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Constant.stateInternet = isConnected
    }


    private var countTime = 0
    private var timer: Timer? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timer = Timer(countTime, 1000*60, 1000, this)
    //    timer?.onCreateTimeCountDown(this)
       swichFragment(SplashScreen())



        baseContext.registerReceiver(CheckConnect(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        MyApplication.instance.setConnectionListener(this)
    }


    override fun onBackPressed() {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.frame_layout_main)
        if(fragment is HomeFragment){
            Log.e("msg", "success")
        }else {
            super.onBackPressed()
        }

        if (fragment is SplashScreen) {
            finish()
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        var fragment: Fragment = supportFragmentManager.findFragmentById(R.id.frame_layout_main)!!
        if (fragment is SplashScreen || fragment is LoginFragment) {
            if (null != timer) {
                timer?.cancel()
            }

        } else {
            if (null != timer) {
                timer?.cancel()
            }
            timer = Timer(0, 60 * 1000, 1000, this)
            timer?.start()
        }

    }

    override fun evenUserInterraction() {
        val loginFragment = LoginFragment()
        loginFragment.logOutOnServer()
        signOut()
        showAlertDialog("Phiên hoạt động đã hết hạn ")
    }

    private fun signOut() {
        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, Constant.gso) }
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
            if (null != timer) {
                timer?.cancel()
            }
            swichFragment(LoginFragment())
        }
    }

    override fun onStop() {
        super.onStop()
        if (!isOpenImage)
            signOut()

    }

    var isOpenImage: Boolean = false
    override fun startOpenImage() {
        isOpenImage = true
    }

    override fun closeImage() {
        isOpenImage = false
    }
}
