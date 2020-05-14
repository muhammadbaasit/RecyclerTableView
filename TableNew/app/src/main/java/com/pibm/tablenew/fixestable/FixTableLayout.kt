package com.pibm.tablenew.fixestable

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pibm.tablenew.R
import com.pibm.tablenew.fixestable.adapter.TableAdapter
import com.pibm.tablenew.fixestable.inter.IDataAdapter
import com.pibm.tablenew.fixestable.inter.ILoadMoreListener
import com.pibm.tablenew.fixestable.widget.SingleLineItemDecoration
import com.pibm.tablenew.fixestable.widget.TableLayoutManager
import java.lang.ref.WeakReference

class FixTableLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    var recyclerView: RecyclerView? = null
    var titleView: HorizontalScrollView? = null
    var leftViews: RecyclerView? = null
    var left_top_view: TextView? = null
    var leftViewShadow: View? = null
    var fl_load_mask: FrameLayout? = null
    var divider_height: Int
    var divider_color: Int
    var col_1_color: Int
    var col_2_color: Int
    var title_color = 0
    var item_width: Int
    var image_maxHeight = 0
    var item_padding: Int
    var item_gravity: Int
    var show_left_shadow = false
    private var dataAdapter: IDataAdapter? = null
    private var isLoading = false
    private var loadMoreListener: ILoadMoreListener? = null
    private var hasMoreData = true
    private fun init(view: View) {
        recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        titleView = view.findViewById<View>(R.id.titleView) as HorizontalScrollView
        leftViews = view.findViewById<View>(R.id.leftViews) as RecyclerView
        left_top_view = view.findViewById<View>(R.id.left_top_view) as TextView
        leftViewShadow = view.findViewById(R.id.leftView_shadows)
        fl_load_mask = view.findViewById<View>(R.id.load_mask) as FrameLayout
        val t1 = TableLayoutManager()
        val t2 = TableLayoutManager()
        recyclerView!!.layoutManager = t1
        leftViews!!.layoutManager = t2
        leftViews!!.setOnTouchListener { v, event ->
            recyclerView!!.onTouchEvent(event)
            true
        }
        titleView!!.setOnTouchListener { v, event ->
            recyclerView!!.onTouchEvent(event)
            true
        }
        if (show_left_shadow) {
            leftViewShadow!!.setVisibility(View.VISIBLE)
        } else {
            leftViewShadow!!.setVisibility(View.GONE)
        }
        val itemDecoration = SingleLineItemDecoration(divider_height, divider_color)
        leftViews!!.addItemDecoration(itemDecoration)
        recyclerView!!.addItemDecoration(itemDecoration)
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                titleView!!.scrollBy(dx, 0)
                leftViews!!.scrollBy(0, dy)
            }
        })
    }

    fun setAdapter(dataAdapter: IDataAdapter?) {
        this.dataAdapter = dataAdapter
        initRecyclerViewAdapter()
    }

    var lastVisablePos = -1
    var fixTableHandler: FixTableHandler? = null
    fun enableLoadMoreData() {
        fixTableHandler = FixTableHandler(this@FixTableLayout, recyclerView)
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isLoading && hasMoreData && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisablePos == recyclerView.adapter!!.itemCount - 1) {
                    isLoading = true
                    fl_load_mask!!.visibility = View.VISIBLE
                    if (loadMoreListener != null) {
                        loadMoreListener!!.loadMoreData(
                                fixTableHandler!!.obtainMessage(MESSAGE_FIX_TABLE_LOAD_COMPLETE))
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val bottomView = recyclerView.getChildAt(recyclerView.childCount - 1)
                lastVisablePos = recyclerView.getChildAdapterPosition(bottomView)
            }
        })
    }

    fun setLoadMoreListener(loadMoreListener: ILoadMoreListener?) {
        this.loadMoreListener = loadMoreListener
    }

    private fun initRecyclerViewAdapter() {
        val builder = TableAdapter.Builder()
        val tableAdapter = builder.setLeft_top_view(left_top_view)
                .setTitleView(titleView)
                .setParametersHolder(
                        TableAdapter.ParametersHolder(col_1_color, col_2_color, title_color,
                                item_width, 40, item_gravity, 30))
                .setLeftViews(leftViews)
                .setDataAdapter(dataAdapter)
                .create()
        recyclerView!!.adapter = tableAdapter
    }

    fun dataUpdate() {
        val tableAdapter = recyclerView!!.adapter as TableAdapter?
        tableAdapter!!.notifyLoadData()
    }

    class FixTableHandler internal constructor(fixTableLayout: FixTableLayout, recyclerView: RecyclerView?) : Handler() {
        var recyclerViewWeakReference: WeakReference<RecyclerView?>
        var fixTableLayoutWeakReference: WeakReference<FixTableLayout>
        override fun handleMessage(msg: Message) {
            if (msg.what == MESSAGE_FIX_TABLE_LOAD_COMPLETE) {
                val recyclerView = recyclerViewWeakReference.get()
                val fixTableLayout = fixTableLayoutWeakReference.get()
                val tableAdapter = recyclerView!!.adapter as TableAdapter?
                val startPos = tableAdapter!!.itemCount - 1
                val loadNum = msg.arg1
                if (loadNum > 0) {
                    tableAdapter.notifyLoadData()
                } else {
                    fixTableLayout!!.hasMoreData = false
                }
                fixTableLayout!!.fl_load_mask!!.visibility = View.GONE
                fixTableLayout.isLoading = false
            }
        }

        init {
            recyclerViewWeakReference = WeakReference(recyclerView)
            fixTableLayoutWeakReference = WeakReference(fixTableLayout)
        }
    }

    companion object {
        const val MESSAGE_FIX_TABLE_LOAD_COMPLETE = 1001
    }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.FixTableLayout)
        divider_height = array.getDimensionPixelOffset(
                R.styleable.FixTableLayout_fixtable_divider_height,
                resources.getDimensionPixelOffset(R.dimen.divider_default_value))
        divider_color = array.getColor(R.styleable.FixTableLayout_fixtable_divider_color,
                Color.BLACK)
        col_1_color = array.getColor(R.styleable.FixTableLayout_fixtable_column_1_color,
                Color.WHITE)
        col_2_color = array.getColor(R.styleable.FixTableLayout_fixtable_column_2_color,
                Color.WHITE)
        //  title_color = array.getColor(R.styleable.FixTableLayout_fixtable_title_color, getResources().getColor(R.color.colorAccent));
        item_width = array.getDimensionPixelOffset(R.styleable.FixTableLayout_fixtable_item_width,
                resources.getDimensionPixelOffset(R.dimen.item_width_default_value))
        item_padding = array.getDimensionPixelOffset(
                R.styleable.FixTableLayout_fixtable_item_top_bottom_padding, 0)
        item_gravity = array.getInteger(R.styleable.FixTableLayout_fixtable_item_gravity, 0)
        when (item_gravity) {
            0 -> item_gravity = Gravity.CENTER
            1 -> item_gravity = Gravity.START or Gravity.CENTER_VERTICAL
            2 -> item_gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        show_left_shadow = array.getBoolean(
                R.styleable.FixTableLayout_fixtable_show_left_view_shadow, false)
        array.recycle()
        val view = View.inflate(context, R.layout.table_view, null)
        init(view)
        addView(view)
    }
}