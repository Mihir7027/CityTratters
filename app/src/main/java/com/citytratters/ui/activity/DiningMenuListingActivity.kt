package com.citytratters.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.citytratters.R
import com.citytratters.base.BaseActivity
import com.citytratters.callbacks.AdapterViewClickListener
import com.citytratters.constants.MyConfig.Global.mBarsMenuGlobal
import com.citytratters.model.response.BarsDetailResponseModel
import com.citytratters.ui.adapter.DiningMenuListAdapter
import kotlinx.android.synthetic.main.activity_dining_menu_listing.*
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.activity_webview.tvPageTitle
import kotlinx.android.synthetic.main.fragment_whatson.*
import kotlinx.android.synthetic.main.toolbar_center.*
import java.util.ArrayList


class DiningMenuListingActivity : BaseActivity() {

    private var mBarsMenu: ArrayList<BarsDetailResponseModel.BarsDetailData.Menu>? = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.activity_dining_menu_listing
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        llSideMenu.visibility = View.GONE
        llBack.visibility = View.VISIBLE
        setUpHomeInToolbar(llHomeToolbar)

        tvPageTitle.text = intent.extras!!.get("pagetitle").toString()
        mBarsMenu =  mBarsMenuGlobal
        llBack.setOnClickListener {
            onBackPressed()
        }

        if(mBarsMenu!!.size > 0){
            tvNoDataFound.visibility = View.GONE
            rvMenuList.visibility = View.VISIBLE
            setAdapter(mBarsMenu)

        }else{
            tvNoDataFound.visibility = View.VISIBLE
            rvMenuList.visibility = View.GONE
        }


    }

    private fun setAdapter(list: List<BarsDetailResponseModel.BarsDetailData.Menu>?) {

        rvMenuList.visibility = View.VISIBLE

        rvMenuList.layoutManager =
            LinearLayoutManager(this@DiningMenuListingActivity, RecyclerView.VERTICAL, false)

        val subMenuListAdapter =
            DiningMenuListAdapter(object :
                AdapterViewClickListener<BarsDetailResponseModel.BarsDetailData.Menu> {

                override fun onClickAdapterView(
                    objectAtPosition:BarsDetailResponseModel.BarsDetailData.Menu?,
                    viewType: Int,
                    position: Int
                ) {
                    when (viewType) {
                        0 -> {
                            val intent = Intent(this@DiningMenuListingActivity, WebviewActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                            intent.putExtra("pagetitle", objectAtPosition!!.bars_url_name)
                            if ( objectAtPosition!!.bars_menu_type == "url"){
                                intent.putExtra("isUrl", "yes")
                            }else{
                                intent.putExtra("isUrl", "yes")
                            }
                            intent.putExtra("webUrl", objectAtPosition!!.bars_url)
                            startActivity(intent)
                        }
                    }
                }
            })

        rvMenuList.adapter = subMenuListAdapter
        subMenuListAdapter.submitList(list)
    }


}