package com.example.trustcheck.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.OnBoardingItem

class OnBoardingAdapter(onBoardingItems: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    private var onBoardingItems: List<OnBoardingItem>? = null

    init {
        this.onBoardingItems = onBoardingItems
    }

    override fun getItemCount(): Int {
        return onBoardingItems?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_boarding_one, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        onBoardingItems?.let { holder.setOnBoardingData(it[position]) }
    }

    class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView
        private val textDescription: TextView
        private val imageOnBoarding: ImageView

        init {
            textTitle = itemView.findViewById(R.id.textTitle)
            textDescription = itemView.findViewById(R.id.textDescription)
            imageOnBoarding = itemView.findViewById(R.id.imageOnboarding)
        }

        fun setOnBoardingData(onBoardingItem: OnBoardingItem) {
            textTitle.text = onBoardingItem.title
            textDescription.text = onBoardingItem.description
            imageOnBoarding.setImageResource(onBoardingItem.image)
        }
    }


}
