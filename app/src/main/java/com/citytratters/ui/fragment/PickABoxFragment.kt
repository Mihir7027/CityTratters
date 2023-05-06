package com.citytratters.ui.fragment

import android.app.Dialog
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.base.IResponseParser
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.model.response.ResponseBoxPrice
import com.citytratters.model.response.ResponseGetAllPrice
import com.citytratters.model.response.ResponseMyPrize
import com.citytratters.model.response.ResponseSuccess
import com.citytratters.network.RestResponse
import com.citytratters.ui.activity.MainActivity
import com.citytratters.ui.adapter.AllPrizesAdapter
import com.citytratters.ui.adapter.MyPrizesAdapter
import com.citytratters.utils.AndroidUtils
import com.citytratters.utils.CustomTypefaceSpan
import kotlinx.android.synthetic.main.fragment_pick_a_box.*
import java.util.*
import kotlin.collections.ArrayList


class PickABoxFragment : BaseFragment() {
    private var responses: ArrayList<ResponseBoxPrice.BoxData> = ArrayList()
    private var openedBox = 0
    private val imageViews: MutableList<ImageView> = java.util.ArrayList()
    private var prizeResponse: List<ResponseGetAllPrice.AllPriceData?> = ArrayList()
    private var myPrizeResponse: List<ResponseMyPrize.MyPrizeData?> = ArrayList()
    private var qrImage: String? = null
    private var couponNotAvailable = 0
    private var boldFont: Typeface? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_pick_a_box
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)!!.setToolbarWithWhiteLogo()

        (activity as MainActivity?)!!.setUpToolbarForHomeIcon((activity as MainActivity?)!!.getToolbarHomeLayout())
        (activity as MainActivity?)!!.getToolbarHomeLayout().setOnClickListener {
            (activity as MainActivity?)!!.navigateToHome()
        }

        tvPageTitle.text = getString(R.string.pick_a_box)
        llPickABox.visibility = View.VISIBLE
        llWhatYouWin.visibility = View.GONE
        llMyPrice.visibility = View.GONE

        tvTabPickABox.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        tvTabWhatYouWin.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        tvTabMyPrice.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));

        subscribe()
        apiCallForRegisterToken()
        setUpFirstData()

        tvTabPickABox.setOnClickListener {
            llPickABox.visibility = View.VISIBLE
            llWhatYouWin.visibility = View.GONE
            llMyPrice.visibility = View.GONE
            tvPageTitle.text = getString(R.string.pick_a_box)
            tvTabPickABox.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );

            setTAndCText(getString(R.string.t_and_c_apply))
            tvTabWhatYouWin.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            tvTabMyPrice.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));

        }
        tvTabWhatYouWin.setOnClickListener {
            llPickABox.visibility = View.GONE
            llWhatYouWin.visibility = View.VISIBLE
            llMyPrice.visibility = View.GONE
            tvPageTitle.text = getString(R.string.pick_a_box)

            tvTabPickABox.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            setTAndCText(getString(R.string.t_and_c_apply))
            apiCallForGetAllPrize()

            tvTabWhatYouWin.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
            tvTabMyPrice.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));

        }
        tvTabMyPrice.setOnClickListener {
            llPickABox.visibility = View.GONE
            llWhatYouWin.visibility = View.GONE
            llMyPrice.visibility = View.VISIBLE
            apiCallForGetMyPrize()
            setTAndCText(getString(R.string.must_be_over_one) + " "+ getString(R.string.app_name) + " "+ getString(R.string.must_be_over_two))
            tvPageTitle.text = getString(R.string.pick_a_box)

            tvTabPickABox.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            tvTabWhatYouWin.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            tvTabMyPrice.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.colorPrimary
                )
            );
        }


        ivBox1.setOnClickListener {
            setOnCLickLister(ivBox1,0)
        }
        ivBox2.setOnClickListener {
            setOnCLickLister(ivBox2,1)
        }
        ivBox3.setOnClickListener {
            setOnCLickLister(ivBox3,2)
        }
        ivBox4.setOnClickListener {
            setOnCLickLister(ivBox4,3)
        }
        ivBox5.setOnClickListener {
            setOnCLickLister(ivBox5,4)
        }
        ivBox6.setOnClickListener {
            setOnCLickLister(ivBox6,5)
        }
        ivBox7.setOnClickListener {
            setOnCLickLister(ivBox7,6)
        }
        ivBox8.setOnClickListener {
            setOnCLickLister(ivBox8,7)
        }
        ivBox9.setOnClickListener {
            setOnCLickLister(ivBox9,8)
        }
        ivBox10.setOnClickListener {
            setOnCLickLister(ivBox10,9)
        }
        ivBox11.setOnClickListener {
            setOnCLickLister(ivBox11,10)
        }
        ivBox12.setOnClickListener {
            setOnCLickLister(ivBox12,11)
        }
        ivBox13.setOnClickListener {
            setOnCLickLister(ivBox13,12)
        }
        ivBox14.setOnClickListener {
            setOnCLickLister(ivBox14,13)
        }
        ivBox15.setOnClickListener {
            setOnCLickLister(ivBox15,14)
        }
        ivBox16.setOnClickListener {
            setOnCLickLister(ivBox16,15)
        }
    }

    private fun setOnCLickLister(imageView: ImageView,position:Int){
        imageView.setImageResource(R.drawable.box_opened)
        getBox(position)
    }

    private fun getBox(position: Int) {
        for (i in responses.indices) {
            if (position == i) {
                if (responses[i]!!.id > 0) {
                    Log.e("Box", "Opened With gift")
                        dialogCongratulation(position)
                } else {
                    dialogTryAgain()
                }
            }
        }
        openedBox++
        if (openedBox >= 3) {
            setClickable(false)
            llPickBox.visibility = View.GONE
            msgError.setVisibility(View.VISIBLE)
        } else {
            setClickable(true)
        }
        if (responses.size > position) addPrices(position)
        //Log.e("openedBox", openedBox + "");
    }



    private fun addPrices(position: Int) {
        vmCommon.apiAddPrizes(
            AndroidUtils.getDeviceId(requireActivity()),
            responses[position]!!.id.toString(),
            (position + 1).toString()
        )
    }

    private fun dialogCongratulation(position: Int) {
        val dialog = Dialog(requireActivity(), R.style.Dialog)
        val layout: View =
            LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sucess, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window!!
                .setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.color_transparent)))
        }
        dialog.setContentView(layout)
        dialog.setCancelable(false)
        val ivWinner =
            dialog.findViewById<View>(R.id.ivWinner) as ImageView
        val tvContinue =
            dialog.findViewById<View>(R.id.tvContinue) as TextView
        val tvViewCart =
            dialog.findViewById<View>(R.id.tvViewCart) as TextView
        val tvYouWon = dialog.findViewById<View>(R.id.tvYouWon) as TextView
        val font = ResourcesCompat.getFont(requireActivity(), R.font.segoe_bold)
        val font2 = ResourcesCompat.getFont(requireActivity(), R.font.segoe_normal)
        val a = "You have won!"
        val b = " This Item has been\nadded to your cart to redeem"
        val builder = SpannableStringBuilder(a + b)
        builder.setSpan(CustomTypefaceSpan("", font), 0, a.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        builder.setSpan(
            CustomTypefaceSpan("", font2),
            a.length + 1,
            a.length + b.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        tvYouWon.setText(builder, TextView.BufferType.SPANNABLE)
        tvContinue.paintFlags = tvContinue.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvViewCart.paintFlags = tvViewCart.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        tvContinue.setOnClickListener {
            dialog.dismiss()
             setActiveVouchers(AndroidUtils.PRIZE_COUNT++)
        }

        tvViewCart.setOnClickListener {
            dialog.dismiss()
             setActiveVouchers(AndroidUtils.PRIZE_COUNT++)

        }
        Glide.with(this).load(responses[position]!!.image_main)
            .placeholder(R.drawable.default_img).error(R.drawable.default_img)
            .into(ivWinner)
        dialog.show()
    }

    private fun apiCallForGetBoxes() {
        vmCommon.apiGetBoxes(AndroidUtils.getDeviceId(requireActivity()))
    }

    private fun apiCallForRegisterToken() {
        vmCommon.apiRegisterToken(AndroidUtils.getDeviceId(requireActivity()),"","android")
    }

    private fun apiCallForGetAllPrize() {
        vmCommon.apiGetAllPrizes()
    }

    private fun apiCallForGetMyPrize() {
        vmCommon.apiGetMyPrizes( AndroidUtils.getDeviceId(requireActivity()))
       // vmCommon.apiGetMyPrizes( AndroidUtils.getDeviceId(requireActivity()))
    }

    private fun redeemVoucher(id: Int) {
        vmCommon.apiRedeemPrizes(AndroidUtils.getDeviceId(requireActivity()), id.toString())
    }

    private fun subscribe() {

        vmCommon.eventGetBoxes.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<ResponseBoxPrice>(this) {
                override fun onSuccess(it: RestResponse<ResponseBoxPrice>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {
                        responses = it.data!!.data

                        checkCount()
                    } else {
                        showSnackBar(it.data!!.message, true)
                    }
                }
            })
        })
        vmCommon.eventRegisterToken.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<ResponseSuccess>(this) {
                override fun onSuccess(it: RestResponse<ResponseSuccess>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {
                        apiCallForGetBoxes()
                    } else {
                        showSnackBar(it.data!!.message, true)
                    }
                }
            })
        })

        vmCommon.eventAddPrizes.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<ResponseSuccess>(this) {
                override fun onSuccess(it: RestResponse<ResponseSuccess>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {

                        } else {
                            showSnackBar(it.data!!.message, true)
                        }
                    } else {
                        showSnackBar(it.data!!.message, true)
                    }
                }

                override fun onError(it: RestResponse<ResponseSuccess>) {
                    super.onError(it)
                    showSnackBar("Error", false)
                }
            })
        })

        vmCommon.eventGetAllPrizes.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<ResponseGetAllPrice>(this) {
                override fun onSuccess(it: RestResponse<ResponseGetAllPrice>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {

                        prizeResponse = it.data!!.data
                        setAdapterForAllPrize()
                    } else {
                        showSnackBar(it.data!!.message, true)
                    }
                }
            })
        })


        vmCommon.eventGetMyPrizes.observe(requireActivity(), androidx.lifecycle.Observer {
            observationOfAPI(it, object : IResponseParser<ResponseMyPrize>(this) {
                override fun onSuccess(it: RestResponse<ResponseMyPrize>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {

                        myPrizeResponse = it.data!!.data
                        setAdapterForMyPrize()
                        getActiveVouchers()
                    } else {
                        showSnackBar(it.data!!.message, true)
                    }
                }
            })
        })

        vmCommon.eventRedeemPrizes.observe(requireActivity(), {
            observationOfAPI(it, object : IResponseParser<ResponseSuccess>(this) {
                override fun onSuccess(it: RestResponse<ResponseSuccess>) {
                    super.onSuccess(it)

                    if (it.status != RestResponse.Status.ERROR) {
                        if (it.data!!.status == 1) {
                            apiCallForGetMyPrize()
                        } else {
                            showSnackBar(it.data!!.message, true)
                        }
                    } else {
                        showSnackBar(it.data!!.message, true)
                    }
                }
            })
        })

    }


    private fun setUpFirstData() {
        setTAndCText(getString(R.string.t_and_c_apply))

        imageViews.clear()
        openedBox = 0
        imageViews.add(ivBox1)
        imageViews.add(ivBox2)
        imageViews.add(ivBox3)
        imageViews.add(ivBox4)
        imageViews.add(ivBox5)
        imageViews.add(ivBox6)
        imageViews.add(ivBox7)
        imageViews.add(ivBox8)
        imageViews.add(ivBox9)
        imageViews.add(ivBox10)
        imageViews.add(ivBox11)
        imageViews.add(ivBox12)
        imageViews.add(ivBox13)
        imageViews.add(ivBox14)
        imageViews.add(ivBox15)
        imageViews.add(ivBox16)
    }

  private fun setTAndCText(text:String){
      boldFont = ResourcesCompat.getFont(requireActivity(), R.font.segoe_normal)
      val font2 = ResourcesCompat.getFont(requireActivity(), R.font.segoe_normal)
      val builder = SpannableStringBuilder()
      val b = text

      val bSpannable = SpannableString(b)
      bSpannable.setSpan(
          CustomTypefaceSpan("", font2),
          0,
          b.length,
          Spanned.SPAN_EXCLUSIVE_INCLUSIVE
      )
      builder.append(bSpannable)
      tvTermsAndCondition.setText(builder, TextView.BufferType.SPANNABLE)
    }

    private fun checkCount() {
        for (i in responses.indices) {
            if (responses[i]!!.is_open > 0) {
                openedBox++
                Log.e("openedBox", openedBox.toString() + "")
                imageViews.get(i).setImageResource(R.drawable.box_opened)
            }
        }
        if (openedBox >= 3) {
            setClickable(false)
            llPickBox.visibility = View.GONE
            msgError.visibility = View.VISIBLE
        } else {
            setClickable(true)
        }
    }


    private fun setClickable(clickable: Boolean) {
        ivBox1.isClickable = clickable
        ivBox2.isClickable = clickable
        ivBox3.isClickable = clickable
        ivBox4.isClickable = clickable
        ivBox5.isClickable = clickable
        ivBox6.isClickable = clickable
        ivBox7.isClickable = clickable
        ivBox8.isClickable = clickable
        ivBox9.isClickable = clickable
        ivBox10.isClickable = clickable
        ivBox11.isClickable = clickable
        ivBox12.isClickable = clickable
        ivBox13.isClickable = clickable
        ivBox14.isClickable = clickable
        ivBox15.isClickable = clickable
        ivBox16.isClickable = clickable
    }

    private fun dialogTryAgain() {
        val dialog = Dialog(requireActivity(), R.style.Dialog)
        val layout: View =
            LayoutInflater.from(getActivity()).inflate(R.layout.dialog_try_again, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window!!
                .setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.stattus_bar_color)))
        }
        dialog.setContentView(layout)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(false)
        val ivClose =
            dialog.findViewById<View>(R.id.ivClose) as ImageView
        ivClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun setActiveVouchers(count: Int) {
        if (count > 0) {
            tvActivePrice.visibility = View.VISIBLE
            tvActivePrice.text = count.toString() + ""
        } else {
            tvActivePrice.visibility = View.GONE
        }
    }

    private fun setAdapterForAllPrize() {
        rvYouWin.visibility = View.VISIBLE

        rvYouWin.layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        val allPrizesAdapter =
            AllPrizesAdapter(object : AdapterViewClickListener<ResponseGetAllPrice.AllPriceData> {

                override fun onClickAdapterView(
                    objectAtPosition: ResponseGetAllPrice.AllPriceData?,
                    viewType: Int,
                    position: Int
                ) {
                    when(viewType){
                        0->{
                            redeemDialogForAllPrize(prizeResponse[position])
                        }
                    }
                }
            })

        rvYouWin.adapter = allPrizesAdapter
        allPrizesAdapter.submitList(prizeResponse)
    }

    fun redeemDialogForAllPrize(position: ResponseGetAllPrice.AllPriceData?) {
        tvTermsAndCondition.visibility = View.GONE
        val dialog = Dialog(requireActivity(), R.style.Dialog)
        val layout: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_reedem, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window!!
                .setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.color_transparent)))
        }
        dialog.setContentView(layout)
        dialog.setCancelable(false)
        val ivWinner =
            dialog.findViewById<View>(R.id.ivWinner) as ImageView
        val tvCancel = dialog.findViewById<View>(R.id.tvCancel) as TextView
        val tvRedeem = dialog.findViewById<View>(R.id.tvRedeem) as TextView
        tvCancel.paintFlags = tvCancel.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvRedeem.paintFlags = tvRedeem.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        qrImage = position!!.qr_image
        if (qrImage != null) {
            //ImageLoader.getInstance().displayImage(qrImage, ivWinner, Utility.getDisplayOption(R.drawable.ic_placeholder06));
            Glide.with(this).load(qrImage).placeholder(R.drawable.default_img)
                .error(R.drawable.default_img).into(ivWinner)
        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
            tvTermsAndCondition.visibility = View.VISIBLE
        }
        tvRedeem.setOnClickListener {
            redeemVoucher(position!!.id.toInt())
            dialog.dismiss()
            tvTermsAndCondition.visibility = View.VISIBLE
        }
        dialog.show()
    }

    private fun setAdapterForMyPrize() {
        rvMyPrizes.visibility = View.VISIBLE

        rvMyPrizes.layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )
        val myPrizesAdapter =
            MyPrizesAdapter(object : AdapterViewClickListener<ResponseMyPrize.MyPrizeData> {

                override fun onClickAdapterView(
                    objectAtPosition: ResponseMyPrize.MyPrizeData?,
                    viewType: Int,
                    position: Int
                ) {

                    if (myPrizeResponse[position]!!.is_redeemed == 0 && System.currentTimeMillis() / 1000 < Calendar.getInstance().timeInMillis) {
                        redeemDialog(position)
                    } else if (myPrizeResponse[position]!!.is_redeemed == 1) {
                        showAlert(activity!!.getString(R.string.already_redeemed_this_voucher))
                    } else if (System.currentTimeMillis() / 1000 > Calendar.getInstance().timeInMillis) {
                        showAlert(activity!!.getString(R.string.voucher_expired))
                    }

                }
            })

        rvMyPrizes.adapter = myPrizesAdapter
        myPrizesAdapter.submitList(myPrizeResponse)
    }


    fun redeemDialog(position: Int) {
        tvTermsAndCondition.visibility = View.GONE
        val dialog = Dialog(requireActivity(), R.style.Dialog)
        val layout: View =
            LayoutInflater.from(activity).inflate(R.layout.dialog_reedem, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window!!
                .setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.color_transparent)))
        }
        dialog.setContentView(layout)
        dialog.setCancelable(false)
        val ivWinner =
            dialog.findViewById<View>(R.id.ivWinner) as ImageView
        val tvCancel = dialog.findViewById<View>(R.id.tvCancel) as TextView
        val tvRedeem = dialog.findViewById<View>(R.id.tvRedeem) as TextView
        tvCancel.paintFlags = tvCancel.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        tvRedeem.paintFlags = tvRedeem.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        if (prizeResponse.size > position)
            qrImage = prizeResponse[position]!!.qr_image
        if (qrImage != null) {
            //ImageLoader.getInstance().displayImage(qrImage, ivWinner, Utility.getDisplayOption(R.drawable.ic_placeholder06));
            Glide.with(this).load(qrImage).placeholder(R.drawable.default_img)
                .error(R.drawable.default_img).into(ivWinner)
        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
            tvTermsAndCondition.visibility = View.VISIBLE
        }
        tvRedeem.setOnClickListener {
            redeemVoucher(prizeResponse[position]!!.id.toInt())
            dialog.dismiss()
            tvTermsAndCondition.visibility = View.VISIBLE
        }
        dialog.show()
    }



    fun getActiveVouchers() {
        for (i in myPrizeResponse.indices) {
            val date = Date(myPrizeResponse[i]!!.won_timestamp)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.HOUR, myPrizeResponse[i]!!.redeem_hours)
            if (myPrizeResponse[i]!!.is_redeemed == 1) {
                couponNotAvailable++
            } else if (System.currentTimeMillis() / 1000 > calendar.timeInMillis) {
                couponNotAvailable++
            }
        }
        if (myPrizeResponse.size - couponNotAvailable != 0 && myPrizeResponse.size - couponNotAvailable > 0) {
            val builder = SpannableStringBuilder()
            val a = "You have "
            val aSpannable = SpannableString(a)
            aSpannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                0,
                a.length,
                0
            )
            builder.append(aSpannable)
            val activePrice: String = (myPrizeResponse.size - couponNotAvailable).toString()
            val bSpannable = SpannableString(activePrice)
            bSpannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                0,
                activePrice.length,
                0
            )
            bSpannable.setSpan(CustomTypefaceSpan("", boldFont), 0, activePrice.length, 0)
            builder.append(bSpannable)
            setActiveVouchers(myPrizeResponse.size - couponNotAvailable)
            AndroidUtils.PRIZE_COUNT = myPrizeResponse.size - couponNotAvailable
            val c = " prize waiting in your cart.\n Redeem it before time runs out."
            val cSpannable = SpannableString(c)
            cSpannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)),
                0,
                c.length,
                0
            )
            builder.append(cSpannable)
            tvpricesYouWon.setText(builder, TextView.BufferType.SPANNABLE)
        } else {
            tvpricesYouWon.text = "You currently have no prizes available."
            setActiveVouchers(myPrizeResponse.size - couponNotAvailable)
        }
    }

    private fun showAlert(message: String) {
        val dialog = Dialog(requireActivity(), R.style.Dialog)
        val view: View =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_alert, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                   requireActivity().resources.getColor(R.color.dialog_bar_color)
                )
            )
        }
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(false)
        val tvMessage = dialog.findViewById<View>(R.id.tvMessage) as TextView
        val tvOk = dialog.findViewById<View>(R.id.tvOk) as TextView
        tvMessage.text = message
        tvOk.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}