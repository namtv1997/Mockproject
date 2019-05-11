package com.example.mockproject.GroupFragment.model

import com.google.gson.annotations.SerializedName

class MemberSearchResponse(@SerializedName("usersNotMemberResponse")
                           var usersNotMemberResponse: ArrayList<MemberSearch>)
