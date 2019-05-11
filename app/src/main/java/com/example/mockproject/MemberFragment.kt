package com.example.mockproject


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_member_detail.*


class MemberFragment : Fragment() {
    var name : String? = ""
    var avt : String? = ""
    var email : String? = ""
    var phone: String? = ""
    var skype: String? = ""
    var joinDate: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_member_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bundle = arguments
        name = bundle?.getString("name")
        avt = bundle?.getString("avt")
        email = bundle?.getString("email")
        phone = bundle?.getString("phone")
        skype = bundle?.getString("skype")
        joinDate = bundle?.getString("join")

        Picasso.with(activity).load(avt).into(imvAvtMember)
        tvName.text = name
        tvEmail.text = email
        joinDateGroupUser.text = joinDate
        skypeUser.text =skype
        phoneUser.text = phone

    }


}
