package com.citytratters.ui.activity


import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.citytratters.R
import com.citytratters.base.BaseForSplashActivity
import com.citytratters.constants.MyConfig
import com.citytratters.myPreferance.MyPreference
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseForSplashActivity() {

    private fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())


        Handler(Looper.getMainLooper()).postDelayed({
            if (MyPreference.getPreference(
                    this@SplashActivity,
                    MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID
                ) != null && MyPreference.getPreference(
                    this@SplashActivity,
                    MyConfig.SharedPreferences.PREF_KEY_LOGIN_ID
                ) != ""
            ) {
                val mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                if (!mKeyguardManager.isKeyguardSecure) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    if (MyPreference.getPreference(
                            this@SplashActivity,
                            MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                        ) != null && MyPreference.getPreference(
                            this@SplashActivity,
                            MyConfig.SharedPreferences.PREF_KEY_IS_OTP_VERIFIED
                        ) != "true"){
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@SplashActivity, WelcomeBackActivity::class.java)
                        startActivity(intent)
                    }

                }
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }

        }, 1000)

    }
}