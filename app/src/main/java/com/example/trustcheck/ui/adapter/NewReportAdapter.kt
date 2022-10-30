package com.example.trustcheck.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.RecentWarning

class NewReportAdapter(private val listItem: List<RecentWarning>) :
    RecyclerView.Adapter<NewReportAdapter.NewReportAdapterViewHolder>() {


    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewReportAdapterViewHolder {
        return NewReportAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_warning, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewReportAdapterViewHolder, position: Int) {
        listItem.let { holder.setDataView(it[position]) }
    }

    class NewReportAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView

        init {
            textTitle = itemView.findViewById(R.id.textTitle)
        }

        fun setDataView(recentWarning: RecentWarning) {
            textTitle.text = recentWarning.name
        }
    }


}