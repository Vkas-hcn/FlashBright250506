package com.dead.ship.even.sank.flashbright250506.ui.launch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.dead.ship.even.sank.flashbright250506.R


typealias OnItemClickListener = (position: Int) -> Unit
typealias OnItemLongClickListener = (position: Int) -> Unit
class AppListAdapter(
    private val items: MutableList<AppInfo>,
    private val onClick: OnItemClickListener,
    private val onLongClick: OnItemLongClickListener
) : RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon = itemView.findViewById<AppCompatImageView>(R.id.ivIcon)
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = items[position]
        holder.ivIcon.setImageDrawable(app.icon)
        holder.tvName.text = app.name

        holder.itemView.setOnClickListener {
            onClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }

    override fun getItemCount() = items.size
}
