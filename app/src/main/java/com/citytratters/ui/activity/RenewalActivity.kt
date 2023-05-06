package com.citytratters.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.citytratters.R
import com.citytratters.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar_center.*


class RenewalActivity : BaseActivity() {

    companion object {
        fun getIntent(ctx: Context): Intent {
            return Intent(ctx, RenewalActivity::class.java)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_renewal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE
        llBack.setOnClickListener {
            finish()
        }
        setUpHomeInToolbar(llHomeToolbar)


    }


}
