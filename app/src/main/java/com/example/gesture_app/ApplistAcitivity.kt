package com.example.gestureapp.UI.applist

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gesture_app.R

import com.example.gestureapp.UI.applist.adapter.ApplistAdapter
import com.example.gestureapp.UI.applist.creategestures.CreateGesuresActivity


class ApplistAcitivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var applistAdapter: ApplistAdapter? = null
    var applist = ArrayList<ApplicationInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applist_acitivity)
        recyclerView  = findViewById(R.id.rvList)
        getAppList()
        applistAdapter = ApplistAdapter(this@ApplistAcitivity,applist,object :ApplistAdapter.onitemClick{
            override fun onClick(posistion: Int, data: ApplicationInfo) {
//                Log.e("SELECTED", data.loadLabel(packageManager).toString())

//                startActivity( packageManager.getLaunchIntentForPackage(data.packageName))
                startActivity( Intent(this@ApplistAcitivity,CreateGesuresActivity::class.java))
            }

        })
        recyclerView!!.adapter = applistAdapter
    }

    private fun getAppList() {
        var list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA) as ArrayList<ApplicationInfo>

        list.forEach {

            val packInfo: ApplicationInfo? = it
            if (packInfo!!.flags and ApplicationInfo.FLAG_SYSTEM === 0) {
                val appName: String = packInfo!!.loadLabel(packageManager).toString()
                Log.e("APP_LIST", appName)
                applist.add(it)
            }
            applistAdapter?.notifyDataSetChanged()
        }


    }

}