package com.example.mockproject.homeScreen

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle

import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.util.Log

import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.mockproject.GroupFragment.setting.set_user.SetUser

import com.example.mockproject.R
import com.example.mockproject.Sprin7.CreateGroupFragment
import com.example.mockproject.common.Constant

import com.example.mockproject.homeScreen.listGroup.ListGroupFragment

import com.example.mockproject.loginScreen.LoginFragment
import com.example.mockproject.model.modelUser.ResponseUser
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient

import com.example.mockproject.util.swichFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.squareup.picasso.Callback

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    var dataClient: DataClient? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holderViewFliper()
        onGetProfileUser()
        swichFragment(ListGroupFragment(),R.id.frame_layout_home_screen)
        nav_view.setNavigationItemSelectedListener(this)


        val loginFragment = LoginFragment()
        loginFragment.setTargetFragment(this, 112)

        btn_menu.setImageResource(R.mipmap.nav_open_menu)
        btn_menu.setOnClickListener {
            drawer_layout.openDrawer(Gravity.START)
        }
        imgCreateNewContent.setOnClickListener {
            swichFragment(CreateGroupFragment())

        }

    }

    private fun signOut() {
        val dialog = Dialog(activity)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_dialog_choose)
        dialog.show()
        val textView = dialog.findViewById<TextView>(R.id.tv_message)
        textView.text = getString(R.string.AreYouSureSignOut)

        val textViewNo = dialog.findViewById<TextView>(R.id.tv_no)
        textViewNo.setOnClickListener {
            dialog.cancel()
        }

        val textViewYes = dialog.findViewById<TextView>(R.id.tv_yes)
        textViewYes.setOnClickListener {
            val loginFragment = LoginFragment()
            loginFragment.logOutOnServer()
            signOutGoogle()
            dialog.cancel()
            }
        dialog.show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 112) {
//
//            Constant.email = data?.getStringExtra(getString(R.string.emai))!!
//            Constant.userName = data.getStringExtra(getString(R.string.userName))!!
//            Constant.avt = data.getStringExtra(getString(R.string.avt))!!
//        }
//    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_user_calendar -> {

            }
            R.id.nav_following -> {

            }
            R.id.nav_help -> {

            }
            R.id.nav_contact -> {
                swichFragment(SetUser())
            }
            R.id.nav_faq -> {

            }
            R.id.nav_logout -> {
                signOut()

            }
        }
        drawer_layout.closeDrawer(Gravity.START)
        return true
    }

    fun onGetProfileUser(){

        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.getProfileUser(Constant.id)
        call?.enqueue(object : retrofit2.Callback<ResponseUser?> {
            override fun onFailure(call: Call<ResponseUser?>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseUser?>?, response: Response<ResponseUser?>?) {
                if(response?.body()?.code == 0){
                    Constant.email = response?.body()?.data?.user?.email
                    Constant.userName = response?.body()?.data?.user?.fullName
                    Constant.familyName = response?.body()?.data?.user?.familyName
                    Constant.givenName = response?.body()?.data?.user?.givenName
                    Constant.avt = response?.body()?.data?.user?.photoUrl
                    Log.e("avt", Constant.avt.toString() )
                    Constant.dateOfBirth = response?.body()?.data?.user?.dateOfBirth.toString()
                    Constant.skype = response?.body()?.data?.user?.skype.toString()
                    Constant.phone = response?.body()?.data?.user?.phone.toString()

                    val hView = nav_view.getHeaderView(0)
                    val navEmail = hView.findViewById<TextView>(R.id.tvEmail)
                    val navUserNamr = hView.findViewById<TextView>(R.id.tvUserName)
                    val navAvt = hView.findViewById<ImageView>(R.id.imgAvt)

                    navEmail.text = Constant.email
                    navUserNamr.text = Constant.userName
                    Picasso.with(activity).load(Constant.avt).into(navAvt)
                }
            }

        })
    }

    private fun holderViewFliper() {
        val arrImageAdvertisement = ArrayList<Int>()
        arrImageAdvertisement.add(R.drawable.anh1)
        arrImageAdvertisement.add(R.drawable.anh3)
        arrImageAdvertisement.add(R.drawable.anh4)
        arrImageAdvertisement.add(R.drawable.anh5)
        arrImageAdvertisement.add(R.drawable.anh6)
        for (i in 0 until arrImageAdvertisement.size) {
            val imageView = ImageView(activity?.applicationContext)
            imageView.setImageResource(arrImageAdvertisement[i])
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            viewFlipperHome.addView(imageView)
        }
        viewFlipperHome.setFlipInterval(Constant.timeToSlideViewFlipper)
        val inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
        val outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_right)
        viewFlipperHome.inAnimation = inAnimation
        viewFlipperHome.outAnimation = outAnimation
    }

    private fun signOutGoogle() {
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, Constant.gso) }
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(Activity()) {
            Log.e("msg","sign out google")
            swichFragment(LoginFragment())
        }
    }

}
