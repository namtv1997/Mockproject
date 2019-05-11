package com.example.mockproject.GroupFragment.setting.set_user


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mockproject.R
import kotlinx.android.synthetic.main.fragment_user_setting.*
import android.content.Intent
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelUser.ResponseUser
import com.example.mockproject.model.modelUser.UserEntity
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_request_join_togroup.*
import kotlinx.android.synthetic.main.menu_group_fragment.*
import okhttp3.MediaType
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class SetUser : Fragment(), SetUserView {

    var dataClient: DataClient? = null
    val setUserPresenter: SetUserPresenter = SetUserPresenter(this, SetUserInteractor())

    interface InteractionListener {
        fun startOpenImage()
        fun closeImage()
    }

    var mListener: InteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_setting, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onDisplayProfileUser()
        imgChangeAvatar.setOnClickListener { setUserPresenter?.onShowDirectory() }
        btnChangeProfile.setOnClickListener { onOpenViewEnterInformation()}
        btnOKSet.setOnClickListener {onChangeProfileUser() }
        btnCancelSet.setOnClickListener { activity?.onBackPressed() }

        addTextChange(edtFamilyName, btnOKSet, btnCancelSet)
        addTextChange(edtGivenName, btnOKSet, btnCancelSet)
        addTextChange(edtNumberTelephone, btnOKSet, btnCancelSet)
        addTextChange(edtSkypeSet, btnOKSet, btnCancelSet)
    }

    private fun showImageAvt(uri: Uri?) {
        if (uri != null) {
            activity?.imgAvtSetting?.setImageURI(uri)
        }else { }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mListener?.closeImage()
        if (requestCode == Constant.SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    showImageAvt(data.data)
                    postImageUserOnServer(data?.data!!,context!!)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is InteractionListener) {
            //init the listener
            mListener = context as InteractionListener
        } else {
            throw RuntimeException(context.toString() + " must implement InteractionListener")
        }
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun hideDirector() {

    }

    override fun showEditInformation() {

    }

    override fun navigationOnBackPress() {

    }

    override fun showDirector() {
        mListener?.startOpenImage()
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constant.SELECT_IMAGE)

    }

    fun onOpenViewEnterInformation(){
        btnOKSet.visibility = View.VISIBLE
        btnCancelSet.visibility = View.VISIBLE
        imgChangeAvatar.visibility = View.VISIBLE
        edtFamilyName.visibility = View.VISIBLE
        edtFamilyName.setText(Constant.familyName)
        edtGivenName.visibility = View.VISIBLE
        edtGivenName.setText(Constant.givenName)
        edtNameSet.visibility = View.GONE
        edtNumberTelephone.isEnabled = true
        edtSkypeSet.isEnabled = true
        btnChangeProfile.visibility = View.GONE
    }

    fun addTextChange(view: EditText, btnOK: Button, btnCancel: Button){
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                btnOK.isEnabled = true
                btnCancel.isEnabled = true
            }
        })
    }

    fun onDisplayProfileUser(){
        Picasso.with(context).load(Constant.avt).into(imgAvtSetting)
        this.edtNameSet.text = Editable.Factory.getInstance().newEditable(Constant.userName)
        this.emailProfile.text = Constant.email
        this.edtSkypeSet.text = Editable.Factory.getInstance().newEditable(Constant.skype)
        this.edtNumberTelephone.text = Editable.Factory.getInstance().newEditable(Constant.phone)
    }

    fun onChangeProfileUser(){
       val user = UserEntity(
               edtFamilyName.text.toString(),
               edtGivenName.text.toString(),
               edtSkypeSet.text.toString(),
               edtNumberTelephone.text.toString()
       )
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.changeProfileUser(Constant.id, user)
        call?.enqueue(object : retrofit2.Callback<BaseDataResponseServer<String>> {
            override fun onFailure(call: retrofit2.Call<BaseDataResponseServer<String>>?, t: Throwable?) {
            }
            override fun onResponse(call: retrofit2.Call<BaseDataResponseServer<String>>?, response: Response<BaseDataResponseServer<String>>?) {

                if(response?.body()?.code == 0) {
                    activity?.showAlertDialog("Change Infomation Success")
                    activity?.onBackPressed()
                }
            }

        })
    }

    fun postImageUserOnServer(linkFileImage: Uri, context: Context){
//        val file = linkFileImage.toImageFile(context)
        val id = DocumentsContract.getDocumentId(linkFileImage);
        val inputStream = context.contentResolver.openInputStream(linkFileImage);
        val bytes = inputStream.readBytes()
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)

        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("file", UUID.randomUUID().toString(), requestFile)
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call: Call<BaseDataResponseServer<String>>? = dataClient?.uploadImageApi(Constant.id, body)
        call?.enqueue(object : retrofit2.Callback<BaseDataResponseServer<String>>{
            override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {
                Log.e("msg", "Exception1:"+t.toString())
            }

            override fun onResponse(call: Call<BaseDataResponseServer<String>>?, response: Response<BaseDataResponseServer<String>>?) {
               Log.e("msgCode", "${response?.body()?.code}")
            }

        })
    }

}
