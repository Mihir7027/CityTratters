package com.citytratters.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.ResponseGetAllPrice
import kotlinx.android.synthetic.main.item_voucher_recycler_view.view.*

class AllPrizesAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<ResponseGetAllPrice.AllPriceData>?
) : ListAdapter<ResponseGetAllPrice.AllPriceData, AllPrizesAdapter.ViewHolder>(BaseListAdapterDiffCallBack<ResponseGetAllPrice.AllPriceData>()) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: ResponseGetAllPrice.AllPriceData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<ResponseGetAllPrice.AllPriceData>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.item_voucher_recycler_view
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: ResponseGetAllPrice.AllPriceData?,
        adapterViewClickListener: AdapterViewClickListener<ResponseGetAllPrice.AllPriceData>?,
        adapterPosition: Int
    ) {

        Glide.with(itemView.context).load(result?.image).placeholder(ContextCompat.getDrawable(itemView.context,R.drawable.default_img)).error(ContextCompat.getDrawable(itemView.context,R.drawable.default_img)).into(itemView.imageVoucher)

        itemView.txtVoucherTitle.text = result?.title
        itemView.txtVoucherTitle.setBackgroundColor(Color.parseColor("#FFFFFF"))
        itemView.txtVoucherDescription.text = result?.description

        itemView.rlItemVoucher.setBackgroundColor(Color.parseColor("#FFFFFF"))
        itemView.rlText.setBackgroundColor(Color.parseColor("#FFFFFF"))
        itemView.txtVoucherDescription.setBackgroundColor(Color.parseColor("#FFFFFF"))
        itemView.rlItemVoucher.setOnClickListener {
         //   adapterViewClickListener?.onClickAdapterView(result, 0, adapterPosition)
        }
    }
}