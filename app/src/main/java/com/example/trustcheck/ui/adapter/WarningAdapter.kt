package com.example.trustcheck.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.RecentWarning

class WarningAdapter(private val listItem: List<RecentWarning>) :
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
        listItem.let { holder.setDataView(it[position]) }
    }

    class WarningAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView

        init {
            textTitle = itemView.findViewById(R.id.textTitle)
        }

        fun setDataView(recentWarning: RecentWarning) {
            textTitle.text = recentWarning.name
        }
    }


}