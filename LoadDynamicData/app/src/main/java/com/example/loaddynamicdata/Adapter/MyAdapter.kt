package com.example.loaddynamicdata.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loaddynamicdata.Interface.ILoadMore
import com.example.loaddynamicdata.MainActivity
import com.example.loaddynamicdata.Model.Item
import com.example.loaddynamicdata.R
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.loading_layout.view.*

internal class LoadingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    var progressBar = itemView.loadingProgressBar
}

internal class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    var nameTxt = itemView.nameTxt
    var lengthTxt = itemView.lengthTxt
}

class MyAdapter(recyclerView: RecyclerView, internal var activity: Activity, internal var items: MutableList<Item? >):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_ITEMTYPE = 0
    val VIEW_LOADINGTYPE = 1

    internal var loadMore:ILoadMore? = null
    internal var isLoading:Boolean = false
    internal var visibleTreshold = 5
    internal var lastVisibleItem:Int = 0
    internal var totalItemCount:Int = 0

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                //Toast.makeText(this, "isLoading: " + isLoading.toString() + "total: " + totalItemCount.toString() + "lastVisible: " + lastVisibleItem + "visibleTresHold: " + visibleTreshold,Toast.LENGTH_LONG).show()
                if(!isLoading && totalItemCount <= lastVisibleItem + visibleTreshold) {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == VIEW_ITEMTYPE) {
            val view = LayoutInflater.from(activity)
                .inflate(R.layout.item_layout, parent, false)

            return ItemViewHolder(view)

        } else if(viewType == VIEW_LOADINGTYPE) {
            val view = LayoutInflater.from(activity)
                .inflate(R.layout.loading_layout, parent, false)

            return LoadingViewHolder(view)
        }

        return null!!
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder) {
            val item = items[position]
            holder.nameTxt.text = items[position]!!.name
            holder.lengthTxt.text = items[position]!!.length.toString()
        } else if( holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(items[position] == null) VIEW_LOADINGTYPE else VIEW_ITEMTYPE
    }

    fun setLoaded() {
        isLoading = false
    }

    fun setLoadMore(iLoadMore: ILoadMore) {
        this.loadMore = iLoadMore
    }
}