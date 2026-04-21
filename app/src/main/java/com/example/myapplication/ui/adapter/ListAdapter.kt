package com.example.myapplication.ui.adapter
import com.example.myapplication.R
import com.example.myapplication.ui.fragment.ItemData

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemListBinding

class ListAdapter(
    private var items: MutableList<ItemData>,   // 改为可变列表，以便删除时更新
    private val onMenuItemClick: (action: String, position: Int) -> Unit
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvSubtitle.text = item.subtitle

        holder.binding.btnMore.setOnClickListener { view ->
            showPopupMenu(view, position)
        }
    }

    override fun getItemCount() = items.size

    private fun showPopupMenu(anchorView: View, position: Int) {
        val context = anchorView.context
        val popView = LayoutInflater.from(context).inflate(R.layout.layout_discover_menu, null, false)
        val popupWindow = PopupWindow(
            popView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 测量弹窗尺寸
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = popView.measuredHeight
        val popupWidth = popView.measuredWidth

        // 获取锚点位置
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val anchorY = location[1]
        val anchorHeight = anchorView.height
        val screenHeight = context.resources.displayMetrics.heightPixels

        // 垂直偏移（避免超出屏幕）
        val spaceBelow = screenHeight - (anchorY + anchorHeight)
        val yOffset = if (spaceBelow >= popupHeight) 0 else -(anchorHeight + popupHeight)

        // 水平偏移（右对齐）
        val xOffset = anchorView.width - popupWidth

        popupWindow.showAsDropDown(anchorView, xOffset, yOffset)

        // 菜单项点击
        popView.findViewById<TextView>(R.id.tv1).setOnClickListener {
            onMenuItemClick("share", position)
            popupWindow.dismiss()
        }
        popView.findViewById<TextView>(R.id.tv2).setOnClickListener {
            onMenuItemClick("favorite", position)
            popupWindow.dismiss()
        }
        popView.findViewById<TextView>(R.id.tv3).setOnClickListener {
            onMenuItemClick("delete", position)
            popupWindow.dismiss()
        }
    }


}