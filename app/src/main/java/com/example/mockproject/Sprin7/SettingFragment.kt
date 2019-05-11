package com.example.mockproject.Sprin7


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance

import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelCreateCourse.CreateCourse
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import kotlinx.android.synthetic.main.fragment_setting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SettingFragment : Fragment() {
    interface InteractionListener {
        fun startOpenImage()
        fun closeImage()
    }
    private var dataClient: DataClient? = null
    var mListener: InteractionListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCancelSetting.setOnClickListener {
            activity?.onBackPressed()
        }

        if (Constant.id==Constant.idCaptainWhenClickGroup){
            btnOkSetting.visibility=View.VISIBLE
            imgChangeBanner.isClickable=true
            imgChangeCourse.isClickable=true
            imgChangeBanner.setOnClickListener {
                Constant.idImage=R.id.imgChangeBanner
                showDirector()

            }
            imgChangeCourse.setOnClickListener {
                Constant.idImage=R.id.imgChangeCourse
                showDirector()

            }
        }
        btnOkSetting.setOnClickListener {
            val editTitle=edt_setting_title.text.toString()
            val editDescription=edt_edit_description.text.toString()
            if(!editTitle.isEmpty()){
                val post= CreateCourse(editTitle,"",Constant.idCaptainWhenClickGroup!!,"","")
                dataClient= RetrofitClient.getClient()?.create(DataClient::class.java)
                val call=dataClient?.putCourse(post,Constant.idGroupWhenClickGroup,Constant.idCaptainWhenClickGroup!!,2)
                call?.enqueue(object : Callback<getTakeAttendance> {
                    override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                        if (response?.body()?.code==0){
                            context?.showAlertDialog("Sửa title thành công")
                            activity?.onBackPressed()
                        }
                    }

                })
            }
            if (!editDescription.isEmpty()){
                val post = CreateCourse("", editDescription, Constant.idCaptainWhenClickGroup!!, "", "")
                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                val call = dataClient?.putCourse(post, Constant.idGroupWhenClickGroup, Constant.idCaptainWhenClickGroup!!, 3)
                call?.enqueue(object : Callback<getTakeAttendance> {
                    override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                        if (response?.body()?.code == 0) {
                            context?.showAlertDialog("Sửa description thành công")
                            activity?.onBackPressed()
                        }
                    }

                })
            }
        }
    }

    private fun showDirector() {
        mListener?.startOpenImage()
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Constant.SELECT_IMAGE)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is InteractionListener) {
            //init the listener
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement InteractionListener")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        mListener?.closeImage()
        if (requestCode == Constant.SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    when (Constant.idImage) {
                        R.id.imgChangeBanner -> {
                            showImageAvt(data.data, imgChangeBanner)
                            onPostImageUserOnServer(data.data!!,context!!,0)
                            Toast.makeText(activity, getString(R.string.editbanner), Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        }
                        R.id.imgChangeCourse -> {
                            showImageAvt(data.data, imgChangeCourse)
                            onPostImageUserOnServer(data.data!!,context!!,1)
                            Toast.makeText(activity, getString(R.string.editAvatar), Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        }
                        else -> {

                        }
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImageAvt(uri: Uri?, idPicture: ImageView) {
        if (uri != null) {
            Log.e("msg", uri.toString())
            idPicture.setImageURI(uri)


        } else {


        }
    }
    fun onPostImageUserOnServer(linkFileImage: Uri, context: Context,code:Int) {
//        val file = linkFileImage.toImageFile(context)
        val id = DocumentsContract.getDocumentId(linkFileImage);
        val inputStream = context.contentResolver.openInputStream(linkFileImage);
        val bytes = inputStream.readBytes()
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes)

        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("file", UUID.randomUUID().toString(), requestFile)
        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call: Call<BaseDataResponseServer<String>>? = dataClient?.uploadImageCourse(Constant.idGroupWhenClickGroup, Constant.idCaptainWhenClickGroup!!, code, body) // user id = "" ah? co gia tri ma a
        call?.enqueue(object : retrofit2.Callback<BaseDataResponseServer<String>> {
            override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<BaseDataResponseServer<String>>?, response: Response<BaseDataResponseServer<String>>?) {




            }

        })

    }
}
