package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.SubMenuDetailResponseModel
import com.citytratters.utils.AndroidUtils
import kotlinx.android.synthetic.main.row_sub_menu_list_for_type_one.view.*


class SubMenuDetailAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<SubMenuDetailResponseModel.SubMenuDetailData>?,
    private val template:String
) : ListAdapter<SubMenuDetailResponseModel.SubMenuDetailData, SubMenuDetailAdapter.ViewHolder>(BaseListAdapterDiffCallBack<SubMenuDetailResponseModel.SubMenuDetailData>()) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: SubMenuDetailResponseModel.SubMenuDetailData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<SubMenuDetailResponseModel.SubMenuDetailData>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.row_sub_menu_list_for_type_one
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: SubMenuDetailResponseModel.SubMenuDetailData?,
        adapterViewClickListener: AdapterViewClickListener<SubMenuDetailResponseModel.SubMenuDetailData>?,
        adapterPosition: Int
    ) {
        itemView.tvTitle.text = result?.title
        //changed
        if (result?.sub_title != ""){
             itemView.tvSubTitle.text = AndroidUtils.toCamelCase(result?.sub_title)
        }else{
            itemView.tvSubTitle.text =  AndroidUtils.toCamelCase(result?.heading)
        }

       // itemView.tvSubTitle.text = result?.sub_title

        if (result?.template!!.toLowerCase() == itemView.context.getString(R.string.entertainment_condition)){
            itemView.tvMoreInfo.text = itemView.context.getString(R.string.book_now)
        }else{
            itemView.tvMoreInfo.text = itemView.context.getString(R.string.more_info)
        }

        if (result?.link == null || result?.link == ""){
            itemView.tvMoreInfo.visibility = View.GONE
        }

        Glide
            .with(itemView.context)
            .load(result?.photo)
            .centerCrop()
            .placeholder(R.drawable.default_img)
            .into(itemView.ivBannerImage)

        itemView.llMain.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
        }

        itemView.tvMoreInfo.setOnClickListener {
            if (itemView.tvMoreInfo.text == itemView.context.getString(R.string.more_info)){
                adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
            }else{
                adapterViewClickListener!!.onClickAdapterView(result, 1, adapterPosition)
            }
        }



    }
}