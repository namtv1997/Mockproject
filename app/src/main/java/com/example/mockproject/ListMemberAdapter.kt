package com.example.mockproject

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_member.view.*
import com.example.mockproject.LoadMore.ILoadMore
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.mockproject.common.Constant
import com.example.mockproject.model.modelMember.MemberCourse
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import kotlinx.android.synthetic.main.item_loading.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMemberAdapter(
        val context: Context,
        val idCaptatin: String,
        private val recyclerView: RecyclerView,
        private var items: List<MemberCourse>,
        private var isCaptain: Boolean,
        private var courseId: String,
        var onClickItem: IpmCallBackOnClickListMember,
        private val mListener: OnSwipeItem

) : RecyclerSwipeAdapter<RecyclerView.ViewHolder>() {

    private var userId: String = ""
    private var memberUserId: String = ""
    var dataClient: DataClient? = null
    private val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1
    var loadMore: ILoadMore? = null
    var isLoading: Boolean = false
    var visibleThreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0


    fun loadMore() {
        val linearLayoutManager = recyclerView.getLayoutManager() as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_member, parent, false)
            return SimpleViewHolder(view)
        } else {
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (viewHolder is SimpleViewHolder)
            viewHolder.bindItem(item, position, context)
        else if (viewHolder is LoadingViewHolder)
            viewHolder.bindItem()
        viewHolder.itemView.itemListMember.setOnClickListener {
            onClickItem.onClickItem(
                    items[position].fullName,
                    items[position].avatar,
                    items[position].email,
                    items[position].createDate,
                    items[position].skype,
                    items[position].phone
            )
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) {
            VIEW_TYPE_LOADING
        } else VIEW_TYPE_ITEM
    }

    fun setLoaded() {
        isLoading = false
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeSetCap
    }

    inner class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val swipeLayout: SwipeLayout = itemView.swipeSetCap

        init {
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, itemView.findViewById(R.id.layout_swipe_right_member))
        }

        fun close() {
            swipeLayout.close()
        }

        fun bindItem(item: MemberCourse, position: Int, context: Context) {

            // Set swipe style
            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut

            Picasso.with(context).load(item.avatar).into(itemView.avatarMemberLM)
            itemView.tvNameMember.text = item.fullName
            itemView.tvGmail.text = item.email

            if(isCaptain){
                if (item.userId == Constant.id){
                    itemView.imgLevelMemberLM.setImageResource(R.mipmap.star_24)
                    itemView.btnSetCaptain.visibility = View.GONE
                    itemView.btnRemoveMember.visibility = View.GONE
                } else {
                    itemView.imgLevelMemberLM.setImageResource(R.mipmap.customer_24)
                    itemView.btnSetCaptain.visibility = View.VISIBLE
                    itemView.btnRemoveMember.visibility = View.VISIBLE
                }
            }else {
                if (item.userId == Constant.id) {
                    itemView.imgLevelMemberLM.setImageResource(R.mipmap.customer_24)
                    itemView.btnRemoveMember.visibility = View.VISIBLE
                    itemView.btnSetCaptain.visibility = View.GONE
                }
                else if(item.userId == idCaptatin){
                    itemView.imgLevelMemberLM.setImageResource(R.mipmap.star_24)
                    itemView.btnSetCaptain.visibility = View.GONE
                    itemView.btnRemoveMember.visibility = View.GONE
                } else {
                    itemView.imgLevelMemberLM.setImageResource(R.mipmap.customer_24)
                    itemView.btnSetCaptain.visibility = View.GONE
                    itemView.btnRemoveMember.visibility = View.GONE
                }
            }

            itemView.btnRemoveMember.setOnClickListener { deleteMember(item, position) }
            itemView.btnSetCaptain.setOnClickListener {
                mListener.onSetCaptain(position)
            }

            // Set listener
            swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onClose(layout: SwipeLayout) {}

                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}

                override fun onStartOpen(layout: SwipeLayout) {}

                override fun onOpen(layout: SwipeLayout) {}

                override fun onStartClose(layout: SwipeLayout) {}

                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {
                    val edge = layout.dragEdge.name
                    if (layout.openStatus.toString() !== "Close") {
                        when (edge) {
                            SwipeLayout.DragEdge.Right.name -> {
                                // Drag RIGHT
                                mListener.onSwipeRight(item)
                            }
                            SwipeLayout.DragEdge.Left.name -> {
                                // Drag LEFT
                                mListener.onSwipeLeft(item)
                            }
                            SwipeLayout.DragEdge.Top.name -> {
                                // Drag TOP
                                mListener.onSwipeTop(item)
                            }
                            SwipeLayout.DragEdge.Bottom.name -> {
                                // Drag BOTTOM
                                mListener.onSwipeBottom(item)
                            }
                        }
                    }
                }
            })
        }

        // remove member
        fun deleteMember(item: MemberCourse, position: Int){
            dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
            memberUserId = items[position].userId
            userId = Constant.id
            val call = dataClient?.deleteMember(memberUserId, userId, courseId )
            call?.enqueue(object: Callback<BaseDataResponseServer<String>> {
                override fun onResponse(call: Call<BaseDataResponseServer<String>>?,
                                        response: Response<BaseDataResponseServer<String>>?) {
                    if(response?.isSuccessful!!) {
                        (items as ArrayList<MemberCourse>).remove(item)
                        notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<BaseDataResponseServer<String>>?, t: Throwable?) {

                }
            })
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem() {
            itemView.progressBar.setIndeterminate(true)
        }
    }

    interface OnSwipeItem {
        fun onSwipeLeft(item: MemberCourse)
        fun onSwipeRight(item: MemberCourse)
        fun onSwipeTop(item: MemberCourse)
        fun onSwipeBottom(item: MemberCourse)
        fun onClickItem(item: MemberCourse)
        fun onSetCaptain(position: Int)
    }
}