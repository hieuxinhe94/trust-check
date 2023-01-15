package com.example.trustcheck.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.RecentWarning

class WarningAdapter(private val listItem: List<RecentWarning>, private val onItemClick: (String) -> Void?) :
    RecyclerView.Adapter<WarningAdapter.WarningAdapterViewHolder>() {

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarningAdapterViewHolder {
        return WarningAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_warning, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WarningAdapterViewHolder, position: Int) {
        listItem.let { holder.setDataView(it[position], onItemClick) }
    }

    class WarningAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView
        private val textShortDescription: TextView
        private val warning_row_item: LinearLayout

        init {
            textTitle = itemView.findViewById(R.id.textTitle)
            textShortDescription = itemView.findViewById(R.id.textShortDescription)
            warning_row_item = itemView.findViewById<LinearLayout>(R.id.warning_row_item);

        }

        fun setDataView(recentWarning: RecentWarning, onItemClick: (String) -> Void?) {
            textTitle.text = recentWarning.name
            textShortDescription.text = recentWarning.shortDescription

            warning_row_item.setOnClickListener{
                onItemClick?.invoke(recentWarning.name)
            }
        }
    }
}