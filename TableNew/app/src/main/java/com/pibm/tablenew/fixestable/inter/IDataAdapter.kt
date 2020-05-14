package com.pibm.tablenew.fixestable.inter

import android.widget.ImageView
import android.widget.TextView

interface IDataAdapter {
    fun getTitleAt(pos: Int): String
    val titleCount: Int
    val itemCount: Int
    fun convertData(position: Int, bindViews: List<TextView>)
    fun convertLeftData(position: Int, bindView: ImageView)
}