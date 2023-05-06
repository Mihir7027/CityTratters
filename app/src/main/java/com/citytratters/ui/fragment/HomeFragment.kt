package com.citytratters.ui.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.citytratters.R
import com.citytratters.base.BaseFragment
import com.citytratters.constants.MyConfig
import com.citytratters.constants.MyConfig.SCREEN.ISCARD
import com.citytratters.myPreferance.MyPreference
import com.citytratters.ui.activity.MainActivity
import com.citytratters.utils.AndroidUtils
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {
    private var i = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setMenuBackgroundColor()
        (activity as MainActivity?)!!.setToolbarWithOutLogo()
        (activity as MainActivity?)!!.hideToolbarHomeIcon()

        llDining.setOnClickListener {
            (activity as MainActivity?)!!.setTitle(getString(R.string.dining))
            (activity as MainActivity?)!!.setSideMenuFontFamily(
                (activity as MainActivity?)!!.getDiningTextView(),
                (activity as MainActivity?)!!.getDiningImageView()
            )
            (activity as MainActivity?)!!.replaceFragment(DiningFragment())

        }

        llMembersCard.setOnClickListener {
            if (MyPreference.getPreference(
                    requireActivity(),
                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                ) == "true"
            ) {
                ISCARD = "1"
                (activity as MainActivity?)!!.setTitle(getString(R.string.member_s_card))
                (activity as MainActivity?)!!.setSideMenuFontFamily(
                    (activity as MainActivity?)!!.getMembershipCardTextView(),
                    (activity as MainActivity?)!!.getMembershipCardImageView()
                )
                (activity as MainActivity?)!!.replaceFragment(DigitalMembersCardFragment())
            } else {
                ISCARD = "0"
                (activity as MainActivity?)!!.setTitle(getString(R.string.sign_in))
                (activity as MainActivity?)!!.setSideMenuFontFamily(
                    (activity as MainActivity?)!!.getSigninTextView(),
                    (activity as MainActivity?)!!.getSiginImageView()
                )
                (activity as MainActivity?)!!.replaceFragment(SignInFragment())
            }



        }

        llFunction.setOnClickListener {
            (activity as MainActivity?)!!.setTitle(getString(R.string.function_amd_events))
            (activity as MainActivity?)!!.setSideMenuFontFamily(
                (activity as MainActivity?)!!.getFunctionTextView(),
                (activity as MainActivity?)!!.getFunctionImageView()
            )
            MyConfig.SCREEN.SELECTEDSCREEN = MyConfig.SCREEN.EVENT
            (activity as MainActivity?)!!.replaceFragment(WhatsOnFragment())

        }

        ivTable.setOnClickListener {
            (activity as MainActivity?)!!.setTitle(getString(R.string.table_ordering))
            (activity as MainActivity?)!!.replaceFragment(TableOrderingFragment())

        }

        llContactUs.setOnClickListener {
            (activity as MainActivity?)!!.setTitle(getString(R.string.contact_us))
            (activity as MainActivity?)!!.setSideMenuFontFamily(
                (activity as MainActivity?)!!.getContactusTextView(),
                (activity as MainActivity?)!!.getContactusImageView()
            )
            (activity as MainActivity?)!!.replaceFragment(ContactUsFragment())
        }

        llPickABox.setOnClickListener {
            (activity as MainActivity?)!!.setTitle(getString(R.string.pick_a_box))
            (activity as MainActivity?)!!.setSideMenuFontFamily(
                (activity as MainActivity?)!!.getPickABoxTextView(),
                (activity as MainActivity?)!!.getPickABoxImageView()
            )
            (activity as MainActivity?)!!.replaceFragment(PickABoxFragment())
        }

        llOffers.setOnClickListener {
            if (MyPreference.getPreference(
                    requireActivity(),
                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                ) == "true"
            ) {
                (activity as MainActivity?)!!.setTitle(getString(R.string.offers))
                (activity as MainActivity?)!!.setSideMenuFontFamily(
                    (activity as MainActivity?)!!.getOffersTextView(),
                    (activity as MainActivity?)!!.getOffersImageView()
                )
                (activity as MainActivity?)!!.replaceFragment(OfferFragment())

            } else {
                UiUtils.showAlertDialog(requireActivity(), "Please login to use this feature.")
            }

        }
        llWhatson.setOnClickListener {
            (activity as MainActivity?)!!.setTitle(getString(R.string.what_s_on))
            (activity as MainActivity?)!!.setSideMenuFontFamily(
                (activity as MainActivity?)!!.getWhatsOnTextView(),
                (activity as MainActivity?)!!.getWhatsOnImageView()
            )
            MyConfig.SCREEN.SELECTEDSCREEN = MyConfig.SCREEN.WHATSON
            (activity as MainActivity?)!!.replaceFragment(WhatsOnFragment())

        }

        ivFrndsLogo.setOnClickListener {
            i = i+1;
            Handler(Looper.getMainLooper()).postDelayed({
                i = 0;
            },1200)
            if (i == 4){
                Toast.makeText(requireActivity(),"App Version = " + AndroidUtils.getAppVersion(requireActivity()),Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setMenuBackgroundColor() {

        if (MyConfig.APPSETTING.IS_BG_COLOR_IN_HOME_SCREEN_MENU == "1") {
            ivOffer.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivMembersCard.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivDining.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivWhatsOn.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivFunction.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivTable.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivPickupBox.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)
            ivContactUs.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.svg_circle_cofee)


        } else {
            ivOffer.background = null
            ivMembersCard.background = null
            ivDining.background = null
            ivWhatsOn.background = null
            ivFunction.background = null
            ivTable.background = null
            ivPickupBox.background = null
            ivContactUs.background = null
        }
    }

}
