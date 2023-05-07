package com.citytratters.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.constants.MyConfig
import com.citytratters.constants.MyConfig.APPSETTING.HOME_FRAGMENT_PATH
import com.citytratters.constants.MyConfig.APPSETTING.IS_BG_COLOR_IN_SIDE_MENU
import com.citytratters.constants.MyConfig.SCREEN.ISCARD
import com.citytratters.constants.MyConfig.SCREEN.SELECTEDSCREEN
import com.citytratters.constants.MyConfig.SharedPreferences.PREF_KEY_PROFILE_IMG_URL
import com.citytratters.myPreferance.MyPreference
import com.citytratters.ui.fragment.*
import com.citytratters.utils.AndroidUtils
import com.google.android.material.navigation.NavigationView
import com.telkomyellow.utils.UiUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_default.*
import kotlinx.android.synthetic.main.fragment_whatson.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.toolbar_center.*
import java.util.*


class MainActivity : BaseActivity() {
    var mView: NavigationView? = null
    var mParent: RelativeLayout? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    var isFrom:String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mView = findViewById<View>(R.id.navigationView) as NavigationView
        mParent = mView!!.getHeaderView(0) as RelativeLayout


        setSideMenu()
        setSideMenuBackgroudColor()
        isFrom = intent.extras?.getString("IS_FROM").toString()
        if (isFrom == "payment"){
            navigateToDigitalMembersCard()
            setSideMenuFontFamily(mParent!!.tvMembershipCard,mParent!!.ivMemberhipCard)

        }else{
            navigateToHome()
            setSideMenuFontFamily(mParent!!.tvHome,mParent!!.ivHome)

        }



        llHomeToolbar.setOnClickListener {
          navigateToHome()
        }
        if (MyPreference.getPreference(
                this@MainActivity,
                MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
            ) == "true"
        ) {
            mParent!!.llSignin.visibility = View.GONE
            //tvSetting.visibility = View.VISIBLE
            tvSignOut.visibility = View.VISIBLE
            mParent!!.llMembership.visibility = View.VISIBLE
            mParent!!.llEntertain.visibility = View.GONE
            //tvView.visibility = View.VISIBLE
            mParent!!.llUpdateDetail.visibility = View.VISIBLE
        } else {
            mParent!!.llSignin.visibility = View.VISIBLE
            //tvSetting.visibility = View.GONE
            tvSignOut.visibility = View.GONE
            mParent!!.llMembership.visibility = View.GONE

            mParent!!.tvMembership.text = "Membership Application"

            llIconSetting.visibility = View.GONE
            //tvView.visibility = View.GONE
            mParent!!.llUpdateDetail.visibility = View.GONE
            mParent!!.tvSideName.text = getString(R.string.app_name)
            mParent!!.tvSideMembershipNumber.text = "#GUEST"
            Glide
                .with(this@MainActivity)
                .load(R.drawable.svg_user_card)
                .centerCrop()
                .placeholder(R.drawable.svg_user_card)
                .error(R.drawable.default_img)
                .into(mParent!!.ivProfileImage)
        }

        llSideMenu.setOnClickListener {
            openCloseDrawer()
        }

        tvSignOut.setOnClickListener {
            if (MyPreference.getPreference(
                    this@MainActivity,
                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                ) == "true"
            ) {
                showAlertDialog(this@MainActivity, getString(R.string.logout_conformation_msg))

            } else {
                openCloseDrawer()
                tvTitle.text = getString(R.string.sign_in)
                setSideMenuFontFamily(tvSignin,mParent!!.ivSignin)
                replaceFragment(SignInFragment())
            }
        }

        mParent!!.llMembershipCard.setOnClickListener {
            if (MyPreference.getPreference(
                    this,
                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                ) == "true"
            ){
                openCloseDrawer()
                setSideMenuFontFamily(tvMembershipCard,mParent!!.ivMemberhipCard)
                ISCARD = "1"
                replaceFragment(DigitalMembersCardFragment())
            }else{
                openCloseDrawer()
                setSideMenuFontFamily(tvSignin,mParent!!.ivSignin)
                replaceFragment(SignInFragment())
            }

        }

        mParent!!.llDining.setOnClickListener {
            openCloseDrawer()
            tvTitle.text = getString(R.string.dining)
            setSideMenuFontFamily(tvDining,mParent!!.ivDining)
            replaceFragment(DiningFragment())
        }

        mParent!!.llWhatson.setOnClickListener {
            openCloseDrawer()
            tvTitle.text = getString(R.string.what_s_on)
            setSideMenuFontFamily(tvWhatsOn,mParent!!.ivWhatsOn)
            SELECTEDSCREEN = MyConfig.SCREEN.WHATSON
            replaceFragment(WhatsOnFragment())
        }

        mParent!!.llFunction.setOnClickListener {
            openCloseDrawer()
            tvTitle.text = getString(R.string.what_s_on)
            setSideMenuFontFamily(tvFunction,mParent!!.ivFunction)
            SELECTEDSCREEN = MyConfig.SCREEN.EVENT
            replaceFragment(WhatsOnFragment())
        }

        mParent!!.llEntertain.setOnClickListener {
            openCloseDrawer()
            tvTitle.text = getString(R.string.entertainment)
            setSideMenuFontFamily(tvEntertainment,mParent!!.ivEntertainment)
            SELECTEDSCREEN = MyConfig.SCREEN.ENTERTAINMENT
            replaceFragment(WhatsOnFragment())
        }

        mParent!!.llMembership.setOnClickListener {
            openCloseDrawer()
            if (MyPreference.getPreference(
                    this@MainActivity,
                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                ) == "true"
            ){
                ISCARD = "1"
                tvTitle.text = getString(R.string.entertainment)
                setSideMenuFontFamily(tvMembership,mParent!!.ivMemberhip)
                SELECTEDSCREEN = MyConfig.SCREEN.MEMBERSHIP
                replaceFragment(WhatsOnFragment())
            }else{
                ISCARD = "0"
                setSideMenuFontFamily(tvMembership,mParent!!.ivMemberhip)
                replaceFragment(DigitalMembersCardFragment())

            }

        }

        mParent!!.llPickABox.setOnClickListener {
            openCloseDrawer()
            tvTitle.text = getString(R.string.pick_a_box)
            setSideMenuFontFamily(tvPickBox,mParent!!.ivPickBox)
            replaceFragment(PickABoxFragment())
        }

        mParent!!.llOffers.setOnClickListener {
            if (MyPreference.getPreference(
                    this@MainActivity,
                    MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                ) == "true"
            ) {
                openCloseDrawer()
                tvTitle.text = getString(R.string.vouchers)
                setSideMenuFontFamily(tvOffers,mParent!!.ivOffers)
                replaceFragment(OfferFragment())
            } else {
                UiUtils.showAlertDialog(this@MainActivity,"Please login to use this feature.")
            }


        }

        mParent!!.llPayment.setOnClickListener {
            openCloseDrawer()
            tvTitle.text = getString(R.string.fees_detail)
            setSideMenuFontFamily(tvPayment,mParent!!.ivPayment)
           /* replaceFragment(PaymentFragment())*/
            val intent = Intent(this@MainActivity, FeesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
        }

        mParent!!.llSignin.setOnClickListener {
            openCloseDrawer()
            setSideMenuFontFamily(tvSignin,mParent!!.ivSignin)
            ivLogo.visibility = View.GONE
            tvTitle.text = getString(R.string.sign_in)
            replaceFragment(SignInFragment())
        }

        mParent!!.llContactUs.setOnClickListener {
            openCloseDrawer()
            setSideMenuFontFamily(tvContactUs,mParent!!.ivUpdateDetail)
            tvTitle.text = getString(R.string.contact_us)
        replaceFragment(ContactUsFragment())
        }

        mParent!!.llUpdateDetail.setOnClickListener {
            openCloseDrawer()
            setSideMenuFontFamily(tvUpdateDetail,mParent!!.ivUpdateDetail)
            replaceFragment(UpdateProfileFragment())
        }

        mParent!!.llHome.setOnClickListener {
            openCloseDrawer()
            setSideMenuFontFamily(tvHome,mParent!!.ivHome)
            llBottomMenu.visibility = View.GONE
            ivLogo.visibility = View.GONE
            replaceFragment(HomeFragment())
        }

        drawer!!.setViewScale(Gravity.START, 0.9f)
        drawer!!.setViewElevation(Gravity.START, 20f)
    }

    fun setUpToolbarForHomeIcon(llHomeToolbar: LinearLayout) {
        if (MyConfig.APPSETTING.IS_HOME_ICON_IN_TOOLBAR == "0") {
            llHomeToolbar.visibility = View.GONE
        } else {
            llHomeToolbar.visibility = View.VISIBLE
        }
    }

    fun getToolbarHomeLayout():LinearLayout{
        return llHomeToolbar
    }

    fun getToolbarHomeIcon():ImageView{
        return ivHomeToolbar
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun navigateToHome(){
        llBottomMenu.visibility = View.GONE
        setSideMenuFontFamily(mParent!!.tvHome,mParent!!.ivHome)
        replaceFragment(HomeFragment())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun navigateToDigitalMembersCard(){
        llBottomMenu.visibility = View.GONE
        setSideMenuFontFamily(mParent!!.tvMembershipCard,mParent!!.ivMemberhipCard)
        replaceFragment(DigitalMembersCardFragment())
    }

    fun hideToolbarHomeIcon() {
        llHomeToolbar.visibility = View.GONE
    }


    @RequiresApi(Build.VERSION_CODES.O)
    public fun setSideMenuFontFamily(textView: TextView,imageView: ImageView) {
        val typefaceBold = resources.getFont(R.font.source_sans_pro_bold)
        val typefaceRegular = resources.getFont(R.font.source_sans_pro_bold)

        mParent!!.tvHome.typeface = typefaceRegular
        mParent!!.tvHome.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivHome!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvMembershipCard.typeface = typefaceRegular
        mParent!!.tvMembershipCard.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivMemberhipCard!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvDining.typeface = typefaceRegular
        mParent!!.tvDining.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
      //  mParent!!.ivDining!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));


        mParent!!.tvWhatsOn.typeface = typefaceRegular
        mParent!!.tvWhatsOn.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivWhatsOn!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvFunction.typeface = typefaceRegular
        mParent!!.tvFunction.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivFunction!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvEntertainment.typeface = typefaceRegular
        mParent!!.tvEntertainment.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivEntertainment!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvMembership.typeface = typefaceRegular
        mParent!!.tvMembership.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
     //   mParent!!.ivMemberhip!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvSignin.typeface = typefaceRegular
        mParent!!.tvSignin.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivSignin!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvUpdateDetail.typeface = typefaceRegular
        mParent!!.tvUpdateDetail.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivUpdateDetail!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvOffers.typeface = typefaceRegular
        mParent!!.tvOffers.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
        //mParent!!.ivOffers!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvPickBox.typeface = typefaceRegular
        mParent!!.tvPickBox.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
        //mParent!!.ivPickBox!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));

        mParent!!.tvContactUs.typeface = typefaceRegular
        mParent!!.tvContactUs.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
       // mParent!!.ivContactus!!.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.black));


        textView.typeface = typefaceBold
        textView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
        //imageView.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary));
    }

    public fun getDiningTextView(): TextView {
        return mParent!!.tvDining
    }

    public fun getDiningImageView(): ImageView {
        return mParent!!.ivDining
    }

    public fun getMembershipCardTextView(): TextView {
        return mParent!!.tvMembershipCard
    }
    public fun getMembershipCardImageView(): ImageView {
        return mParent!!.ivMemberhipCard
    }
    public fun getSiginImageView(): ImageView {
        return mParent!!.ivSignin
    }

    public fun getUpdateDetailsTextView(): TextView {
        return mParent!!.tvUpdateDetail
    }

    public fun getUpdateDetailsImageView(): ImageView {
        return mParent!!.ivUpdateDetail
    }

    public fun getSigninTextView(): TextView {
        return mParent!!.tvSignin
    }

    public fun getOffersTextView(): TextView {
        return mParent!!.tvOffers
    }

    public fun getOffersImageView(): ImageView {
        return mParent!!.ivOffers
    }

    public fun getWhatsOnTextView(): TextView {
        return mParent!!.tvWhatsOn
    }
    public fun getWhatsOnImageView(): ImageView {
        return mParent!!.ivWhatsOn
    }

    public fun getFunctionTextView(): TextView {
        return mParent!!.tvFunction
    }

    public fun getFunctionImageView(): ImageView {
        return mParent!!.ivFunction
    }

    public fun getEntertainmentTextView(): TextView {
        return mParent!!.tvEntertainment
    }

    public fun getMembershipTextView(): TextView {
        return mParent!!.tvMembership
    }
    public fun getMembershipImageView(): ImageView {
        return mParent!!.ivMemberhip
    }

    public fun getPickABoxTextView(): TextView {
        return mParent!!.tvPickBox
    }
    public fun getPickABoxImageView(): ImageView {
        return mParent!!.ivPickBox
    }

    public fun getContactusTextView(): TextView {
        return mParent!!.tvContactUs
    }

    public fun getContactusImageView(): ImageView {
        return mParent!!.ivContactus
    }

    public fun setToolbarWithWhiteLogo() {
        val params: ViewGroup.LayoutParams = llToolbarCenterMain.layoutParams
        params.height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 76f, resources.displayMetrics)
                .toInt()
        llToolbarCenterMain.layoutParams = params
        ivLogo.visibility = View.VISIBLE
        llToolbarCenterMain.setBackgroundColor(Color.TRANSPARENT)
        ivSideMenu.setImageResource(R.drawable.svg_menu)
    }

    public fun setToolbarWithOutLogo() {
        val params: ViewGroup.LayoutParams = llToolbarCenterMain.layoutParams
        params.height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 76f, resources.displayMetrics)
                .toInt()
        llToolbarCenterMain.layoutParams = params
        ivLogo.visibility = View.GONE
        tvTitle.visibility = View.GONE
        llToolbarCenterMain.setBackgroundColor(Color.TRANSPARENT)
        ivSideMenu.setImageResource(R.drawable.svg_menu)
    }


    fun setCofeeSideMenu() {
        ivSideMenu.setImageResource(R.drawable.svg_menu_cofee)
    }

    private fun openCloseDrawer() {

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT)
        } else {
            drawer.openDrawer(Gravity.LEFT)
        }
        setToolbarWithWhiteLogo()
    }

    override fun onBackPressed() {
        val myFragment: HomeFragment? =
            supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_PATH) as HomeFragment?
        if (myFragment != null && myFragment.isVisible) {

            showAlertDialogForExitApp(this@MainActivity, getString(R.string.exit_msg))
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setSideMenuFontFamily(tvHome,mParent!!.ivHome)
            }
            AndroidUtils.replaceFragment(
                supportFragmentManager,
                R.id.frameContainer,
                HomeFragment()
            )
        }
    }

    private fun showAlertDialogForExitApp(
        context: Context?,
        message: String?
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(
            ContextThemeWrapper(
                context,
                R.style.DialogTheme
            )
        )
        builder.setMessage(message).setCancelable(false)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                finish()
            }.setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun setSideMenu() {
        if (mView != null) {
            val mParent = mView!!.getHeaderView(0) as RelativeLayout
            if (mParent != null) {

                mParent.tvSideName.text = MyPreference.getPreference(
                                this@MainActivity,
                                MyConfig.SharedPreferences.PREF_KEY_FIRST_NAME
                            ).toUpperCase(Locale.getDefault()) + " " + MyPreference.getPreference(
                                this@MainActivity,
                                MyConfig.SharedPreferences.PREF_KEY_SURNAME
                            ).toUpperCase(Locale.getDefault())

                mParent.tvSideMembershipNumber.text = "Member #" + MyPreference.getPreference(
                    this@MainActivity,
                    MyConfig.SharedPreferences.PREF_KEY_MEMBERSHIP_NUMBER
                )

                if (MyPreference.getPreference(this@MainActivity, PREF_KEY_PROFILE_IMG_URL)
                        .toString() != null && MyPreference.getPreference(
                        this@MainActivity,
                        PREF_KEY_PROFILE_IMG_URL
                    )
                        .toString() != "null" && MyPreference.getPreference(
                        this@MainActivity,
                        PREF_KEY_PROFILE_IMG_URL
                    )
                        .toString() != ""
                ) {
                    val imageBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        java.util.Base64.getDecoder().decode(
                            MyPreference.getPreference(this@MainActivity, PREF_KEY_PROFILE_IMG_URL)
                                .toString()
                        )
                    } else {
                        android.util.Base64.decode(
                            MyPreference.getPreference(this@MainActivity, PREF_KEY_PROFILE_IMG_URL)
                                .toString(), android.util.Base64.DEFAULT
                        )
                    }
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    mParent.ivProfileImage.setImageBitmap(decodedImage)
                } else {
                    Glide
                        .with(this@MainActivity)
                        .load(R.drawable.svg_user_card)
                        .centerCrop()
                        .placeholder(R.drawable.svg_user_card)
                        .error(R.drawable.svg_user_card)
                        .into(mParent.ivProfileImage)
                }

            }
        }
    }

    private fun setSideMenuBackgroudColor() {
        if (mView != null) {
            val mParent = mView!!.getHeaderView(0) as RelativeLayout
            if (mParent != null) {
                if (IS_BG_COLOR_IN_SIDE_MENU == "1") {
                    mParent.llIconHome.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconDigitalMemberCard.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconDining.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconWhatsOn.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconFunction.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconEntertainment.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconMembership.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconSignin.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconUpdateDetail.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconOffer.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconPickABox.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)
                    mParent.llIconContactUs.background = ContextCompat.getDrawable(this, R.drawable.svg_circle_cofee)

                } else {
                    mParent.llIconHome.background = null
                    mParent.llIconDigitalMemberCard.background = null
                    mParent.llIconDining.background = null
                    mParent.llIconWhatsOn.background = null
                    mParent.llIconFunction.background = null
                    mParent.llIconEntertainment.background = null
                    mParent.llIconMembership.background = null
                    mParent.llIconSignin.background = null
                    mParent.llIconUpdateDetail.background = null
                    mParent.llIconOffer.background = null
                    mParent.llIconPickABox.background = null
                    mParent.llIconContactUs.background = null

                }
            }
        }

    }

    fun showAlertDialog(
        context: Context?,
        message: String?
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(
            androidx.appcompat.view.ContextThemeWrapper(
                context,
                R.style.DialogTheme
            )
        )
        builder.setMessage(message)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                MyPreference.removeAllPreference(this@MainActivity)
                startMainActivity()
            }
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, which -> }
            .show()
    }

    public fun replaceFragment(fragment: Fragment) {
        AndroidUtils.replaceFragment(
            supportFragmentManager,
            R.id.frameContainer,
            fragment
        )
    }

    public fun setTitle(string: String) {
        tvTitle.text = string

    }

}
