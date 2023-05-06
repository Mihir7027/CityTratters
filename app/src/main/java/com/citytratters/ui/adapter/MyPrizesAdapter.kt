package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.ResponseMyPrize

import kotlinx.android.synthetic.main.item_voucher_recycler_view.view.*
import java.util.*

class MyPrizesAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<ResponseMyPrize.MyPrizeData>?
) : ListAdapter<ResponseMyPrize.MyPrizeData, MyPrizesAdapter.ViewHolder>(BaseListAdapterDiffCallBack<ResponseMyPrize.MyPrizeData>()) {

    var couponNotAvailable = 0

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: ResponseMyPrize.MyPrizeData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<ResponseMyPrize.MyPrizeData>?
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
        result: ResponseMyPrize.MyPrizeData?,
        adapterViewClickListener: AdapterViewClickListener<ResponseMyPrize.MyPrizeData>?,
        adapterPosition: Int
    ) {


        itemView.txtVoucherTitle.text = result?.title
        itemView.txtVoucherDescription.text = result?.description
        itemView.rlRedeemed.visibility = View.GONE

        val date = result?.won_timestamp?.let { Date(it) }
        val calendar = Calendar.getInstance()
        calendar.time = date
        result?.redeem_hours?.let { calendar.add(Calendar.HOUR, it) }


        if (result?.is_redeemed == 1) {
            itemView.rlRedeemed.visibility = View.VISIBLE
            couponNotAvailable++
            result.couponNotAvailable = couponNotAvailable
        } else if (System.currentTimeMillis()/1000 > calendar.timeInMillis) {
            itemView.rlRedeemed.setVisibility(View.VISIBLE)
            itemView.ivRibbon.setImageResource(R.drawable.ic_exribon)
            couponNotAvailable++
            result?.couponNotAvailable = couponNotAvailable
        }

        Glide.with(itemView.context).load(result?.image).error(R.drawable.default_img).placeholder(R.drawable.default_img).into(itemView.imageVoucher)
        itemView.rlItemVoucher.setOnClickListener {
            adapterViewClickListener?.onClickAdapterView(result, 0, adapterPosition)
        }
    }
}