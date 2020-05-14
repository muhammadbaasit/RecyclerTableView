package com.pibm.tablenew

import android.os.Bundle
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pibm.tablenew.fixestable.FixTableLayout
import com.pibm.tablenew.fixestable.inter.ILoadMoreListener
import java.util.*

class MainActivity : AppCompatActivity() {
    var title = arrayOf("Sr.No", "First Name", "Last Name", "Phone", "City")
    var data: MutableList<DataBean> = ArrayList()
    var currentPage = 1
    var totalPage = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..79) {
            data.add(DataBean("101", "Niraj", "Naware", "8888444434", "pune", R.drawable.ic_edit))
        }
        val fixTableLayout = findViewById<View>(R.id.fixTableLayout) as FixTableLayout
        val fixTableAdapter = FixTableAdapter(title, data)
        fixTableLayout.setAdapter(fixTableAdapter)
        fixTableLayout.enableLoadMoreData()
        fixTableLayout.setLoadMoreListener(object : ILoadMoreListener {
            override fun loadMoreData(message: Message?) {
                Thread(Runnable {
                    if (currentPage <= totalPage) {
                        for (i in 0..49) {
                            data.add(DataBean("102", "Niraj", "Naware", "8888444434", "pune", R.drawable.ic_edit))
                        }
                        currentPage++
                        message!!.arg1 = 50
                    } else {
                        message!!.arg1 = 0
                    }
                    message.sendToTarget()
                }).start()
            }
        })
    }
}