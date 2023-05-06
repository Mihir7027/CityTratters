package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.BarsDetailResponseModel
import kotlinx.android.synthetic.main.row_bar_gallery.view.*


class BarsGalleryAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Gallery>?
) : ListAdapter<BarsDetailResponseModel.BarsDetailData.Gallery, BarsGalleryAdapter.ViewHolder>(BaseListAdapterDiffCallBack<BarsDetailResponseModel.BarsDetailData.Gallery>()) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: BarsDetailResponseModel.BarsDetailData.Gallery?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Gallery>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.row_bar_gallery
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: BarsDetailResponseModel.BarsDetailData.Gallery?,
        adapterViewClickListener: AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Gallery>?,
        adapterPosition: Int
    ) {

        Glide
            .with(itemView.context)
            .load(result?.bars_photo)
            .centerCrop()
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .into(itemView.ivGallery)
    }
}