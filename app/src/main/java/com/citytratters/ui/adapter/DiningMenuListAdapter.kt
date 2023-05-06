package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.BarsDetailResponseModel
import kotlinx.android.synthetic.main.row_menu_list.view.*


class DiningMenuListAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Menu>?
) : ListAdapter<BarsDetailResponseModel.BarsDetailData.Menu, DiningMenuListAdapter.ViewHolder>(BaseListAdapterDiffCallBack<BarsDetailResponseModel.BarsDetailData.Menu>()) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: BarsDetailResponseModel.BarsDetailData.Menu?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Menu>?
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
        result: BarsDetailResponseModel.BarsDetailData.Menu?,
        adapterViewClickListener: AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Menu>?,
        adapterPosition: Int
    ) {
        itemView.tvTitle.text = result?.bars_url_name
        itemView.tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary));

        itemView.tvTitle.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
        }


    }
}