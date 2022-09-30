package com.example.gestureapp.UI.applist.adapter

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gesture_app.R


class ApplistAdapter(
    var activity: Activity,
    var arrayList: ArrayList<ApplicationInfo>,
    var listener: onitemClick
) :
    RecyclerView.Adapter<ApplistAdapter.MyViewHolder>() {
    interface onitemClick {
        fun onClick(posistion: Int, data: ApplicationInfo)
    }

    inner class MyViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var tvName: TextView = view!!.findViewById(R.id.tvName)
        var iv: ImageView = view!!.findViewById(R.id.iv)
        var tvPkgName: TextView = view!!.findViewById(R.id.tvPkgName)
        fun bind(position: Int, array: ApplicationInfo) {
            tvName.text = array.loadLabel(activity.packageManager).toString()
            tvPkgName.text = array.packageName
            Glide.with(activity)
                .load(array.loadIcon(activity.packageManager))
                .placeholder(R.drawable.ic_launcher_background)
                .into(iv)
            itemView.setOnClickListener { listener.onClick(position, array) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_applist_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val array = arrayList[position]
        holder.bind(position, array)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}