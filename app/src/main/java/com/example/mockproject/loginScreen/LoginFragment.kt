package com.example.mockproject.loginScreen


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.R


import com.example.mockproject.common.Constant
import com.example.mockproject.homeScreen.HomeFragment
import com.example.mockproject.model.modelUser.ResponseUser
import com.example.mockproject.model.modelUser.UserEntity
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog

import com.example.mockproject.util.swichFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient


import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    var dataClient: DataClient? = null
    var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mGooogleSingInClient = context?.let { GoogleSignIn.getClient(it, Constant.gso) }
        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            if (Constant.stateInternet) {
                val signInIntent = mGooogleSingInClient?.signInIntent
                startActivityForResult(signInIntent, Constant.requestCode)

            }else{
                context?.showAlertDialog("Check your Internet")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.requestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        val account = completedTask.getResult(ApiException::class.java)
        if (isEmailNTQ(account.email.toString()) != (getString(R.string.link_ntq))) {
            signOut()
            context?.showAlertDialog(getString(R.string.messageLoginNotNTQ))
        } else {
            Constant.isOperation = true
            Constant.userName = account.displayName
            Constant.email = account.email
            Constant.avt = account.photoUrl.toString()
            Constant.id = account.id ?: ""

            val user = UserEntity(account.id.toString(), account.email.toString(),
                    account.displayName.toString(), account.familyName.toString(),
                    account.givenName.toString(), account.photoUrl.toString(), null, "", "")
            dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
            val call = dataClient?.createUser(user)
            call?.enqueue(object : Callback<ResponseUser> {
                override fun onFailure(call: Call<ResponseUser>?, t: Throwable?) {

                }
                override fun onResponse(call: Call<ResponseUser>?, response: Response<ResponseUser>?) {

                    Log.e("msg", response?.body()?.code.toString())
                    if(response?.body()?.code == 0)
                        swichFragment(HomeFragment())
                }

            })


        }
    } catch (e: ApiException) {
    }

    fun isEmailNTQ(email: String): String {

        val domain: String? = email.substringAfterLast("@")
        return domain.toString()
    }

    fun logOutOnServer(){
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.logOutServer(Constant.id)
        call?.enqueue(object :  Callback<BaseDataResponseServer<String>> {

            override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<BaseDataResponseServer<String>>?, response: Response<BaseDataResponseServer<String>>?) {
                if (response?.body()?.code == 0){
                    signOut()
                }
            }

        })
    }

    private fun signOut() {
        Log.e("msg", "log out")
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, Constant.gso) }
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(Activity()) {
            Log.e("msg","sign out google")
            swichFragment(LoginFragment())
        }
    }
}

