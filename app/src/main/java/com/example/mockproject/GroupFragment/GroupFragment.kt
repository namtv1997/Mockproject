package com.example.mockproject.GroupFragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.mockproject.Dialog.DescriptionGroupDialogFragment
import com.example.mockproject.GroupFragment.Pending.Pending
import com.example.mockproject.GroupFragment.Pending.PendingResponse
import com.example.mockproject.GroupFragment.adapter.ContentRequestCreateAdapter
import com.example.mockproject.GroupFragment.adapter.RequestJoinAdapter
import com.example.mockproject.GroupFragment.model.*
import com.example.mockproject.ListAttendance.AttendanceFragment
import com.example.mockproject.ListAttendance.ModelTakeAttendance.getTakeAttendance
import com.example.mockproject.ListAttendance.ipmCallBackAttendanceClick
import com.example.mockproject.ListMemberFragment

import com.example.mockproject.R
import com.example.mockproject.Sprin7.CreateGroupFragment
import com.example.mockproject.Sprin7.SettingFragment
import com.example.mockproject.Sprint5.createEventScreen.view.CreateEventFragment
import com.example.mockproject.Sprint5.createEventScreen.view.IpmCallCreateEventFragment
import com.example.mockproject.Sprint5.calendarScreen.view.CalendarFragment
import com.example.mockproject.common.Constant
import com.example.mockproject.contentScreen.CreateContentFragment
import com.example.mockproject.contentScreen.EditContentFragment
import com.example.mockproject.getmember.ResponeMembers
import com.example.mockproject.homeScreen.listGroup.createQueue.DataPostServerCreateQueue
import com.example.mockproject.listContent.IpmCallBackEditContentOnClick
import com.example.mockproject.listContent.ListContentAdapter
import com.example.mockproject.model.modelCreateCourse.CreateCourse
import com.example.mockproject.model.modelGetCousre.Content
import com.example.mockproject.model.modelMember.MemberCourse
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.showAlertDialog
import com.example.mockproject.util.swichFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_group.*
import kotlinx.android.synthetic.main.header_item_list_content.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class GroupFragment : Fragment(), IpmCallBackEditContentOnClick, ipmCallBackAttendanceClick,
        IpmCallCreateEventFragment {
    override fun btnCreateEventOnClick(idContent: String) {
        var bundle = Bundle()
        bundle.putString(getString(R.string.idContentToCreateFragment), idContent)
        var createEventFragment = CreateEventFragment()
        createEventFragment.arguments = bundle
        swichFragment(createEventFragment)
    }

    interface InteractionListener {
        fun startOpenImage()
        fun closeImage()
    }

    var mListener: InteractionListener? = null

    var listMember = ArrayList<MemberCourse>()
    var listRequestCreateContent = ArrayList<Pending>()
    var listRequestJoinToGroup = ArrayList<DataRequestJoinToGroup>()

    var idGroup: String = ""
    private var dataClient: DataClient? = null
    var listConten = ArrayList<Content>()
    var isCheck = Constant.showDetailGroup
    var checkShow = false
    private lateinit var adapter: ListContentAdapter
    private lateinit var adapterRequestJoinToGroup: RequestJoinAdapter
    private lateinit var adapterRequestCreateContent: ContentRequestCreateAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle = arguments
        idGroup = bundle!!.getString("idGroup")
        Constant.idGroupWhenClickGroup = idGroup

        getProfileGroup()
        getListMemeber(idGroup)


        btnEditCourse.setOnClickListener {
            val title = titleGroup.text.toString().trim()
            val post = CreateCourse(title, "", Constant.idCaptainWhenClickGroup!!, "", "")
            dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
            val call = dataClient?.putCourse(post, idGroup, Constant.idCaptainWhenClickGroup!!, 2)
            call?.enqueue(object : Callback<getTakeAttendance> {
                override fun onFailure(call: Call<getTakeAttendance>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<getTakeAttendance>?, response: Response<getTakeAttendance>?) {
                    if (response?.body()?.code == 0) {
                        context?.showAlertDialog("Sửa title thành công")

                    }
                }

            })
        }
        btnSetting.setOnClickListener {
            swichFragment(SettingFragment())
        }
        btnCalendar.setOnClickListener {
            val calendarFragment = CalendarFragment()
            calendarFragment.arguments = Bundle().apply { putString(getString(R.string.idGroupToCalendarFragment), idGroup) }
            swichFragment(calendarFragment)
        }
        btnListContent.setOnClickListener {
            onClickContent()
        }

        isCheck = if (checkShow) {
            tvDescriptionGroup.visibility = View.VISIBLE
            true
        } else {
            tvDescriptionGroup.visibility = View.GONE
            false
        }
        btnDescription.setOnClickListener {
            swichFragment(DescriptionGroupDialogFragment())
        }


        btnCreateContent.setOnClickListener {

            var bundleSendIdGroupToCreateContentFragment = Bundle()
            bundleSendIdGroupToCreateContentFragment.putString(getString(R.string.idGroupToCreateContentFragment), idGroup)
            var createContentFragment = CreateContentFragment()
            createContentFragment.arguments = bundleSendIdGroupToCreateContentFragment
            swichFragment(createContentFragment)

        }



        adapter = ListContentAdapter(listConten, this,
                this, this, Constant.idGroupWhenClickGroup)
        recycleListConten.layoutManager = LinearLayoutManager(context)

        recycleListConten.adapter = adapter
        adapter.notifyDataSetChanged()
        recycleListConten.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })


    }






    private fun onClickBellContent() {

        //Call API và gán listRequest tạo content trong onRespone
        recycleListConten.layoutManager = LinearLayoutManager(this.activity)

        recycleListConten.adapter = adapterRequestCreateContent
        adapterRequestCreateContent.notifyDataSetChanged()

    }

    fun getProfileGroup() {
        var dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.getGroupById(Constant.idGroupWhenClickGroup, Constant.id,0, 100)

        call?.enqueue(object : Callback<BaseDataResponseServer<DataResponseGetGroup>> {
            override fun onFailure(call: Call<BaseDataResponseServer<DataResponseGetGroup>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<BaseDataResponseServer<DataResponseGetGroup>>?, response:
            Response<BaseDataResponseServer<DataResponseGetGroup>>?) {

                if (response?.body()?.code == 0) {
                    when(response.body()?.data?.status){
                        "join" -> btnCreateContent.visibility = View.VISIBLE
                    }
                    listConten.clear()

                    if (response.body()?.data?.content?.listContent != null) {
                        listConten.addAll(response.body()?.data?.content?.listContent!!)
                        adapter.notifyDataSetChanged()

                        Constant.description = response.body().data?.descriptor!!
                        if (response?.body()?.data!!.avatar != null) {
                            Constant.imageAvatar = response.body()?.data?.avatar!!
                            Picasso.with(activity).load(Constant.imageAvatar).into(imgAvartarCourse)
                        }
                        if (response?.body().data!!.banner != null) {
                            Constant.imageBanner = response.body()?.data?.banner!!
                            Picasso.with(activity).load(Constant.imageBanner).into(imgBanner)
                        }
                        titleGroup.setText(response.body()?.data?.title)
                        Constant.idCaptainWhenClickGroup = response.body().data?.captainId

                        imvAvtMember4.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString(getString(R.string.idGroup), response.body().data?.id)
                            bundle.putString(getString(R.string.idCaptain), response.body().data?.captainId)
                            bundle.putString(getString(R.string.userId), Constant.id)
                            bundle.putBoolean(getString(R.string.isCaptain), response.body().data?.isCaption!!)
                            val listMemberFragment = ListMemberFragment()
                            listMemberFragment.arguments = bundle
                            swichFragment(listMemberFragment)

                        }
                        btnBellcontent.setOnClickListener {
                            onClickBellContent()
                        }
                        if (response.body().data?.isCaption == true) {
                            viewBell.visibility = View.VISIBLE
                            titleGroup.isEnabled = true
                            imgBanner.isClickable = true
                            imgAvartarCourse.isClickable = true

                            btnEditCourse.visibility = View.VISIBLE
                            btnSetting.visibility = View.VISIBLE
                            imgAvartarCourse.setOnClickListener {
                                Constant.idImage = R.id.imgAvartarCourse
                                showDirector()
                            }
                            imgBanner.setOnClickListener {
                                Constant.idImage = R.id.imgBanner
                                showDirector()
                            }
                            addMember.setOnClickListener {
                                val bundle = Bundle()
                                bundle.putString(getString(R.string.idGroupForFragmentSearchMember), idGroup)
                                val addMemberForCaptainFragment = AddMemberForCaptainFragment()
                                addMemberForCaptainFragment.arguments = bundle
                                swichFragment(addMemberForCaptainFragment)
                            }
                            btnBell.setOnClickListener {
                                onClickBell()
                            }


                        } else if (response.body().data?.status.toString().toUpperCase() == Constant.joned) {
                            addMember.visibility = View.GONE

                        } else if (response.body().data?.status.toString().toUpperCase() == Constant.wait) {
                            addMember.visibility = View.GONE

                        } else {
                            //Member thong thuong ngoai group
                            addMember.text = "Join"
                            addMember.setOnClickListener {
                                addMember.visibility = View.GONE
                                val dataPostServerCreateQueue = DataPostServerCreateQueue(idGroup, Constant.id)
                                dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                                val call = dataClient?.createQueue(dataPostServerCreateQueue)
                                call?.enqueue(object : Callback<DataResponseCreateQueue> {
                                    override fun onFailure(call: Call<DataResponseCreateQueue>?, t: Throwable?) {

                                    }

                                    override fun onResponse(call: Call<DataResponseCreateQueue>?, response: Response<DataResponseCreateQueue>?) {
                                        if (response?.body()?.code == 0) {
                                            context?.showAlertDialog(getString(R.string.MembershipRequest))
                                        }
                                    }
                                })
                            }
                            btnCreateContent.visibility = View.GONE
                        }
                        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                        val call = dataClient?.getListRequest(idGroup)
                        adapterRequestJoinToGroup = RequestJoinAdapter(listRequestJoinToGroup)
                        call?.enqueue(object : Callback<BaseDataResponseServer<ListMemberWantToJoinGroup>> {
                            override fun onFailure(call: Call<BaseDataResponseServer<ListMemberWantToJoinGroup>>?, t: Throwable?) {
                            }

                            override fun onResponse(call: Call<BaseDataResponseServer<ListMemberWantToJoinGroup>>?,
                                                    response: Response<BaseDataResponseServer<ListMemberWantToJoinGroup>>?) {
                                if (response?.body()?.code == 0) {
                                    listRequestJoinToGroup.clear()
                                    listRequestJoinToGroup.addAll(response?.body()?.data?.list!!)
                                    adapterRequestJoinToGroup.notifyDataSetChanged()

                                    tv_amountRequestJoin.text = response.body().data!!.list.size.toString()
                                }

                            }
                        })



                        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                        val call1 = dataClient?.getPending(idGroup, Constant.idCaptainWhenClickGroup!!)
                        adapterRequestCreateContent = ContentRequestCreateAdapter(listRequestCreateContent, this@GroupFragment)
                        call1?.enqueue(object : Callback<BaseDataResponseServer<PendingResponse>> {
                            override fun onFailure(call: Call<BaseDataResponseServer<PendingResponse>>?, t: Throwable?) {

                            }

                            override fun onResponse(call: Call<BaseDataResponseServer<PendingResponse>>?, response: Response<BaseDataResponseServer<PendingResponse>>?) {

                                if (response?.body()?.data?.listGetPendingResponses != null) {
                                    listRequestCreateContent.clear()
                                    adapterRequestCreateContent.notifyDataSetChanged()
                                    listRequestCreateContent.addAll(response.body()?.data?.listGetPendingResponses!!)

                                    adapterRequestCreateContent.notifyDataSetChanged()
                                    tv_amountRequestPending.text = response.body().data!!.listGetPendingResponses.size.toString()

                                }

                            }
                        })
                    }
                }
            }
        })
    }

    fun getListMemeber(idGroup: String) {

        dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
        val call = dataClient?.getMembersAPI(idGroup)
        call?.enqueue(object : Callback<ResponeMembers> {
            override fun onFailure(call: Call<ResponeMembers>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<ResponeMembers>?, response: Response<ResponeMembers>?) {
                listMember.clear()
                listMember.addAll(response?.body()?.data?.memberResponses!!)
                when (listMember.size) {
                    1 -> {
                        Picasso.with(activity).load(listMember[0].avatar).into(imvAvtMember1)
                        imvAvtMember2.visibility = View.INVISIBLE
                        imvAvtMember3.visibility = View.INVISIBLE
                        imvAvtMember4.text = "+0..."
                    }
                    2 -> {
                        Picasso.with(activity).load(listMember[0].avatar).into(imvAvtMember1)
                        Picasso.with(activity).load(listMember[1].avatar).into(imvAvtMember2)
                        imvAvtMember3.visibility = View.INVISIBLE
                        imvAvtMember4.text = "+0..."
                    }
                    3 -> {
                        Picasso.with(activity).load(listMember[0].avatar).into(imvAvtMember1)
                        Picasso.with(activity).load(listMember[1].avatar).into(imvAvtMember2)
                        Picasso.with(activity).load(listMember[2].avatar).into(imvAvtMember3)
                        imvAvtMember4.text = "+0..."
                    }
                }
                if (listMember.size > 3) {
                    Picasso.with(activity).load(listMember[0].avatar).into(imvAvtMember1)
                    Picasso.with(activity).load(listMember[1].avatar).into(imvAvtMember2)
                    Picasso.with(activity).load(listMember[3].avatar).into(imvAvtMember3)
                    imvAvtMember4.text = "+${listMember.size - 3}..."
                }

            }
        })
    }

    fun onClickBell() {
        recycleListConten.layoutManager = LinearLayoutManager(this.activity)
        recycleListConten.adapter = adapterRequestJoinToGroup
    }

    fun onClickContent() {
        recycleListConten.layoutManager = LinearLayoutManager(this.activity)
        recycleListConten.adapter = adapter
        adapter.notifyDataSetChanged()
        adapterRequestCreateContent.notifyDataSetChanged()

    }

    override fun btnEditContentOnClick(contentEdit: Content, idGroup: String) {
        val editFragment = EditContentFragment()
        val bundleSendContentAndIdGroup = Bundle()
        bundleSendContentAndIdGroup.putSerializable(getString(R.string.sendContentToEditFragment), contentEdit)
        bundleSendContentAndIdGroup.putString(getString(R.string.sendIdGroupFragment), idGroup)
        editFragment.arguments = bundleSendContentAndIdGroup
        swichFragment(editFragment)
    }

    override fun onClickBtnAttendance(contentId: String) {
        val bundle = Bundle()
        bundle.putString(getString(R.string.contentId), contentId)
        bundle.putString(getString(R.string.courseId), idGroup)
        Log.e("msg", "$idGroup, $contentId")
        val attendaceFragment = AttendanceFragment()
        attendaceFragment.arguments = bundle
        swichFragment(attendaceFragment)
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
            mListener = context as InteractionListener
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
                        R.id.imgAvartarCourse -> {
                            showImageAvt(data.data, imgAvartarCourse)
                            onPostImageUserOnServer(data.data!!,context!!,1)

                        }
                        R.id.imgBanner -> {
                            showImageAvt(data.data, imgBanner)
                            onPostImageUserOnServer(data.data!!,context!!,0)

                        }
                        else -> {
                            Log.e("msg", "run here 3")
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
        val call: Call<BaseDataResponseServer<String>>? = dataClient?.uploadImageCourse(idGroup, Constant.idCaptainWhenClickGroup!!, code, body) // user id = "" ah? co gia tri ma a
        call?.enqueue(object : retrofit2.Callback<BaseDataResponseServer<String>> {
            override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<BaseDataResponseServer<String>>?, response: Response<BaseDataResponseServer<String>>?) {

            }

        })

    }
}
