package com.example.loaddynamicdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loaddynamicdata.Adapter.MyAdapter
import com.example.loaddynamicdata.Interface.ILoadMore
import com.example.loaddynamicdata.Model.Item
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ILoadMore {
    var items: MutableList<Item?> = ArrayList()
    lateinit var adapter: MyAdapter

    override fun onLoadMore() {

        if(items!!.size < 50) { // max 50 item
            items!!.add(null)
            adapter.notifyItemInserted(items.size - 1)

            // run on thread
            Handler().postDelayed({
                items.removeAt(items.size - 1)
                adapter.notifyItemRemoved(items.size)

                // random  new data
                val index = items.size
                val end = index + 10

                for(i in index until end) {
                    //val name = "Mark " + UUID.randomUUID().toString()
                    val name = "Mark " + i.toString()
                    val item = Item(name, name.length)
                    items.add(item)
                }

                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }, 5000) // delay 5 second
        } else {
            Toast.makeText(this, "Max 50 record", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // random first 10 data
        random10Data()

        // init view
        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(itemRecyclerView, this, items)
        itemRecyclerView.adapter = adapter

        adapter.setLoadMore(this)
    }

    private fun random10Data() {
        for(i in 0..9) {
            //val name = "Start " + UUID.randomUUID().toString()
            val name = "Start " + i.toString()
            val item = Item(name, name.length)
            items.add(item)
        }
    }
}
