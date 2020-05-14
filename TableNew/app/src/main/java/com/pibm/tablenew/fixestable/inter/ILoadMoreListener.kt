package com.pibm.tablenew.fixestable.inter

import android.os.Message

interface ILoadMoreListener {
    fun loadMoreData(message: Message?)
}