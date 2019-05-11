package com.example.mockproject.listContent


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.contentScreen.EditContentFragment

import com.example.mockproject.model.modelGetCousre.Content
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.swichFragment

class ListContentFragment : Fragment(){
   

    private var dataClient: DataClient? = null
    private lateinit var listConten : ArrayList<Content>
    private lateinit var adapter: ListContentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
