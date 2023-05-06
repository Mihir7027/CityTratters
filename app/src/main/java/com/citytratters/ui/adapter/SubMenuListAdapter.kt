package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.SubMenuListResponseModel
import kotlinx.android.synthetic.main.row_menu_list.view.*


class SubMenuListAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<SubMenuListResponseModel.SubMenuData>?
) : ListAdapter<SubMenuListResponseModel.SubMenuData, SubMenuListAdapter.ViewHolder>(BaseListAdapterDiffCallBack<SubMenuListResponseModel.SubMenuData>()) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: SubMenuListResponseModel.SubMenuData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<SubMenuListResponseModel.SubMenuData>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.row_menu_list
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: SubMenuListResponseModel.SubMenuData?,
        adapterViewClickListener: AdapterViewClickListener<SubMenuListResponseModel.SubMenuData>?,
        adapterPosition: Int
    ) {
        itemView.tvTitle.text = result?.title

        itemView.tvTitle.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
        }


    }
}