package com.example.mockproject.homeScreen.listGroup


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mockproject.R
import com.example.mockproject.common.Constant
import com.example.mockproject.homeScreen.listGroup.model.GroupModelOfGetGroups
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_group.view.*


class ListGroupAdapter(var listGroup: ArrayList<GroupModelOfGetGroups>,
                       var holderListGroup: IpmCallBackItemClickListGroup) :
        RecyclerView.Adapter<ListGroupAdapter.ViewHolderGroup>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderGroup {
        context = p0.context
        var view = LayoutInflater.from(p0.context).inflate(R.layout.item_list_group, p0, false)
        return ViewHolderGroup(view)

    }

    override fun getItemCount(): Int = listGroup.size

    override fun onBindViewHolder(viewHolder: ViewHolderGroup, position: Int) {

        viewHolder.itemView.title_group.text = listGroup[position].titleGroup
        if (listGroup[position].banner == "") {
            viewHolder.itemView.imgCourse.setImageResource(R.mipmap.ic_launcher)
        } else {
            Picasso.with(context).load(listGroup[position].banner).into(viewHolder.itemView.imgCourse)
        }

        when (listGroup[position].status.toUpperCase()) {
            "UNJOIN" -> viewHolder.itemView.iconStatus.visibility = View.INVISIBLE
            "WAIT" -> viewHolder.itemView.iconStatus.visibility = View.INVISIBLE
            "JOINED" -> viewHolder.itemView.iconStatus.setImageResource(R.drawable.grop_ip_black)
        }
        if (listGroup[position].idCaption.equals(Constant.id)) {
            viewHolder.itemView.iconStatus.setImageResource(R.drawable.icon_captain)
        }
        viewHolder.itemView.itemListGroupFragment.setOnClickListener {
            holderListGroup.listenceOnClickItem(listGroup[position].id)
        }
    }

    class ViewHolderGroup(itemView: View) : RecyclerView.ViewHolder(itemView)
}
