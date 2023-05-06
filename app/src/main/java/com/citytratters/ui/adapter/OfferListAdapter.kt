package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.model.response.OfferListResponseModel
import com.citytratters.utils.AndroidUtils
import kotlinx.android.synthetic.main.row_offer_list.view.*


class OfferListAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<OfferListResponseModel.OfferListData>?
) : ListAdapter<OfferListResponseModel.OfferListData, OfferListAdapter.ViewHolder>(
    BaseListAdapterDiffCallBack<OfferListResponseModel.OfferListData>()
) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: OfferListResponseModel.OfferListData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<OfferListResponseModel.OfferListData>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.row_offer_list
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: OfferListResponseModel.OfferListData?,
        adapterViewClickListener: AdapterViewClickListener<OfferListResponseModel.OfferListData>?,
        adapterPosition: Int
    ) {
        itemView.tvOfferName.text = result?.title
        itemView.tvOfferSubName.text = AndroidUtils.toCamelCase(result?.description)

        Glide
            .with(itemView.context)
            .load(result!!.image)
            .centerCrop()
            .placeholder(R.drawable.default_img)
            .error(R.drawable.default_img)
            .into(itemView.ivBannerImage);

        if (result?.is_locked.toString() == "1") {
            itemView.llOfferLock.visibility = View.VISIBLE
        } else {
            itemView.llOfferLock.visibility = View.GONE
        }

        if (result?.is_redeemed.toString() == "1") {
            itemView.ivRedeemed.visibility = View.VISIBLE
        } else {
            itemView.ivRedeemed.visibility = View.GONE
        }

        itemView.llMain.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
        }


        itemView.tvBookNow.setOnClickListener {
            adapterViewClickListener!!.onClickAdapterView(result, 1, adapterPosition)
        }


    }
}