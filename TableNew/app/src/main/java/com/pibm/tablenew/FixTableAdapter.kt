package com.pibm.tablenew

import android.widget.ImageView
import android.widget.TextView
import com.pibm.tablenew.fixestable.inter.IDataAdapter

class FixTableAdapter(var titles: Array<String>, var mdata: List<DataBean>) : IDataAdapter {
    fun setData(data: List<DataBean>) {
        this.mdata = data
    }

    override fun getTitleAt(pos: Int): String {
        return titles[pos]
    }

    override val titleCount: Int
        get() = titles.size

    override val itemCount: Int
        get() = mdata.size

    override fun convertData(position: Int, bindViews: List<TextView>) {
        val dataBean = mdata[position]
        bindViews!![0].setText(dataBean.id)
        bindViews[1].setText(dataBean.data1)
        bindViews[2].setText(dataBean.data2)
        bindViews[3].setText(dataBean.data3)
        bindViews[4].setText(dataBean.data4)
    }

    override fun convertLeftData(position: Int, bindView: ImageView) {
        //bindView.setText(data.get(position).id);
        bindView!!.setImageResource(mdata[position].src)
    }

}