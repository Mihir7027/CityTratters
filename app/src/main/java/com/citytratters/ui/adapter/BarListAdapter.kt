package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.BarListResponseModel
import kotlinx.android.synthetic.main.row_bar_list.view.*


class BarListAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<BarListResponseModel.BarData>?
) : ListAdapter<BarListResponseModel.BarData, BarListAdapter.ViewHolder>(BaseListAdapterDiffCallBack<BarListResponseModel.BarData>()) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: BarListResponseModel.BarData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<BarListResponseModel.BarData>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.row_bar_list
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: BarListResponseModel.BarData?,
        adapterViewClickListener: AdapterViewClickListener<BarListResponseModel.BarData>?,
        adapterPosition: Int
    ) {
        itemView.tvBarName.text = result?.bars_title

        Glide
            .with(itemView.context)
            .load(result!!.bars_icon_photo)
            .centerCrop()
            .placeholder(R.drawable.default_img)
            .into(itemView.ivProfileImage);

        Glide
            .with(itemView.context)
            .load(result!!.bar_bg_image)
            .centerCrop()
            .placeholder(R.drawable.default_img)
            .into(itemView.ivBannerImage);

        if (result?.booking_url != ""){
            itemView.tvBookNow.visibility = View.VISIBLE
        }else{
            itemView.tvBookNow.visibility = View.GONE
        }


        itemView.llMain.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
        }


        itemView.tvBookNow.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 1, adapterPosition)
        }


    }
}