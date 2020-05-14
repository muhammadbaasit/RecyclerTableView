package com.pibm.tablenew.fixestable.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pibm.tablenew.fixestable.adapter.TableAdapter.LeftViewAdapter.LeftViewHolder
import com.pibm.tablenew.fixestable.adapter.TableAdapter.TableViewHolder
import com.pibm.tablenew.fixestable.inter.IDataAdapter
import com.pibm.tablenew.fixestable.widget.SingleLineLinearLayout
import com.pibm.tablenew.fixestable.widget.TextViewUtils
import java.util.*

class TableAdapter private constructor(
        private val titleView: HorizontalScrollView?, private val leftViews: RecyclerView?, private val left_top_view: TextView?,
        private val parametersHolder: ParametersHolder?, private val dataAdapter: IDataAdapter?) : RecyclerView.Adapter<TableViewHolder>() {
    private fun initViews() {
        left_top_view!!.setBackgroundColor(parametersHolder!!.title_color)
        TextViewUtils.setTitleTextView(left_top_view, "", parametersHolder.item_gravity,
                parametersHolder.item_width, parametersHolder.item_padding)
        leftViews!!.adapter = LeftViewAdapter()
        val titleChild = titleView!!.getChildAt(0) as SingleLineLinearLayout
        for (i in 0 until dataAdapter!!.titleCount) {
            val textView = TextViewUtils.generateTitleTextView(titleChild.context,
                    dataAdapter.getTitleAt(i),
                    parametersHolder.item_gravity,
                    parametersHolder.item_width,
                    parametersHolder.item_padding)
            titleChild.addView(textView, i)
        }
        titleChild.setBackgroundColor(parametersHolder.title_color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val singleLineLinearLayout = SingleLineLinearLayout(
                parent.context)
        for (i in 0 until dataAdapter!!.titleCount) {
            val textView = TextViewUtils.generateTextView(singleLineLinearLayout.context, " ",
                    parametersHolder!!.item_gravity,
                    parametersHolder.item_width,
                    parametersHolder.item_padding)
            singleLineLinearLayout.addView(textView, i)
        }
        return TableViewHolder(singleLineLinearLayout)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val ll_content = holder.itemView as SingleLineLinearLayout
        val bindViews: MutableList<TextView> = ArrayList()
        for (i in 0 until dataAdapter!!.titleCount) {
            val textView = ll_content.getChildAt(i) as TextView
            bindViews.add(textView)
        }
        setBackgrandForItem(position, ll_content)
        dataAdapter.convertData(position, bindViews)
    }

    private fun setBackgrandForItem(position: Int, ll_content: SingleLineLinearLayout) {
        if (position % 2 != 0) {
            ll_content.setBackgroundColor(parametersHolder!!.col_1_color)
        } else {
            ll_content.setBackgroundColor(parametersHolder!!.col_2_color)
        }
    }

    override fun getItemCount(): Int {
        return dataAdapter!!.itemCount
    }

    internal inner class LeftViewAdapter : RecyclerView.Adapter<LeftViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeftViewHolder {
            val singleLineLinearLayout = SingleLineLinearLayout(
                    parent.context)

            /*TextView textView = TextViewUtils.generateTextView(singleLineLinearLayout.getContext(), " ",
                    parametersHolder.item_gravity,
                    parametersHolder.item_width,
                    parametersHolder.item_padding);*/
            val imageView = TextViewUtils.generateImageView(singleLineLinearLayout.context, 0,
                    parametersHolder!!.item_gravity,
                    parametersHolder.imageViewMaxHeight,
                    parametersHolder.item_padding)
            singleLineLinearLayout.addView(imageView)
            return LeftViewHolder(singleLineLinearLayout)
        }

        override fun onBindViewHolder(holder: LeftViewHolder, position: Int) {
            val ll_content = holder.itemView as SingleLineLinearLayout
            val child = ll_content.getChildAt(0) as ImageView
            setBackgrandForItem(position, ll_content)
            dataAdapter!!.convertLeftData(position, child)
        }

        override fun getItemCount(): Int {
            return dataAdapter!!.itemCount
        }

        internal inner class LeftViewHolder(itemView: View?) : ViewHolder(itemView!!)
    }

    inner class TableViewHolder(itemView: View?) : ViewHolder(itemView!!)
    class ParametersHolder(var col_1_color: Int, var col_2_color: Int, var title_color: Int,
                           var item_width: Int, var item_padding: Int, var item_gravity: Int, imageMaxHeight: Int) {
        var imageViewMaxHeight = 0

    }

    class Builder {
        var titleView: HorizontalScrollView? = null
        var leftViews: RecyclerView? = null
        var left_top_view: TextView? = null
        var parametersHolder: ParametersHolder? = null
        var dataAdapter: IDataAdapter? = null
        fun setTitleView(titleView: HorizontalScrollView?): Builder {
            this.titleView = titleView
            return this
        }

        fun setLeftViews(leftViews: RecyclerView?): Builder {
            this.leftViews = leftViews
            return this
        }

        fun setLeft_top_view(left_top_view: TextView?): Builder {
            this.left_top_view = left_top_view
            return this
        }

        fun setParametersHolder(
                parametersHolder: ParametersHolder?): Builder {
            this.parametersHolder = parametersHolder
            return this
        }

        fun setDataAdapter(dataAdapter: IDataAdapter?): Builder {
            this.dataAdapter = dataAdapter
            return this
        }

        fun create(): TableAdapter {
            return TableAdapter(titleView, leftViews, left_top_view, parametersHolder, dataAdapter)
        }
    }

    fun notifyLoadData() {
        notifyDataSetChanged()
        leftViews!!.adapter!!.notifyDataSetChanged()
    }

    init {
        initViews()
    }
}