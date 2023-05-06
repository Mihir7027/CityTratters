package com.citytratters.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig
import com.citytratters.model.response.MemberFeesResponseModel
import com.citytratters.utils.AndroidUtils
import com.citytratters.R
import com.citytratters.callbacks.BaseListAdapterDiffCallBack
import com.citytratters.constants.MyConfig.SCREEN.SELECTEDFEESPOSITION
import kotlinx.android.synthetic.main.row_payment.view.*


class FeesListAdapter(
    private val adapterViewClickListener: AdapterViewClickListener<MemberFeesResponseModel.MemberFeesData>?
) : ListAdapter<MemberFeesResponseModel.MemberFeesData, FeesListAdapter.ViewHolder>(
    BaseListAdapterDiffCallBack<MemberFeesResponseModel.MemberFeesData>()
) {

    inner class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(
            result: MemberFeesResponseModel.MemberFeesData?,
            itemViewType: Int,
            adapterViewClickListener: AdapterViewClickListener<MemberFeesResponseModel.MemberFeesData>?
        ) {
            setDataOnAdapterView(itemView, result, adapterViewClickListener, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val layoutId = R.layout.row_payment
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), getItemViewType(position), adapterViewClickListener)
    }

    fun setDataOnAdapterView(
        itemView: View,
        result: MemberFeesResponseModel.MemberFeesData?,
        adapterViewClickListener: AdapterViewClickListener<MemberFeesResponseModel.MemberFeesData>?,
        adapterPosition: Int
    ) {
        itemView.radio.isChecked = result?.isSelected == true
        itemView.tvPlanType.text = result?.type.toString()
        itemView.tvGst.text = result?.amountGST.toString()
        itemView.tvAmount.text = (result?.amountGST?.plus(result?.amountDue!!)).toString()
        itemView.tvExpiry.text =  AndroidUtils.changeDateFormat(
            result?.expiryDate,
            MyConfig.DateFormat.yyyy_mm_ddTHHMM,
            MyConfig.DateFormat.dd_mmm_yyyy)

        itemView.radio.setOnClickListener {
            itemView.llMain.performClick()
        }
        itemView.llMain.setOnClickListener {

            for (i in currentList.indices){
                currentList[i].isSelected = false
            }
            currentList[adapterPosition].isSelected = true
            SELECTEDFEESPOSITION = adapterPosition
            notifyDataSetChanged()
            adapterViewClickListener!!.onClickAdapterView(result, 0, adapterPosition)
        }



    }
}