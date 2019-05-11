package com.example.mockproject


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.listContent.ListContentAdapter
import com.example.mockproject.model.modelGetCousre.Content
import com.example.mockproject.retrofit2.DataClient

class ListContentFragment : Fragment() {
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