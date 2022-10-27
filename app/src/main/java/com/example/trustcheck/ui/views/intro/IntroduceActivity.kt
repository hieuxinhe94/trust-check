package com.example.trustcheck.ui.views.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.trustcheck.R
import com.example.trustcheck.data.models.OnBoardingItem
import com.example.trustcheck.ui.adapter.OnBoardingAdapter
import com.example.trustcheck.ui.utils.Common
import com.example.trustcheck.ui.views.home.HomeActivity
import com.google.android.material.button.MaterialButton

class IntroduceActivity : AppCompatActivity() {
    private var onBoardingAdapter: OnBoardingAdapter? = null
    private var layoutOnBoardingIndicator: LinearLayout? = null
    private var buttonOnBoardingAction: MaterialButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduce)

        layoutOnBoardingIndicator = findViewById(R.id.layoutOnboardingIndicators)
        buttonOnBoardingAction = findViewById(R.id.buttonOnBoardingAction)

        setOnboardingItem()

        val onBoardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onBoardingViewPager.adapter = onBoardingAdapter

        setOnBoardingIndicator()
        setCurrentOnboardingIndicators(0)

        onBoardingViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicators(position)
            }
        })

        buttonOnBoardingAction?.setOnClickListener {
            if (onBoardingViewPager.currentItem + 1 < (onBoardingAdapter?.itemCount ?: 2)) {
                onBoardingViewPager.currentItem = onBoardingViewPager.currentItem + 1
            } else {
                Common().setFistInstall()
                val i = Intent(applicationContext, HomeActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                finish()
            }
        }

    }

    private fun setOnBoardingIndicator() {
        val indicators = onBoardingAdapter?.let { arrayOfNulls<ImageView>(it.itemCount) }
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        if (indicators != null) {
            for (i in indicators.indices) {
                indicators[i] = ImageView(applicationContext)
                indicators[i]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.onboarding_indicator_inactive
                    )
                )
                indicators[i]!!.layoutParams = layoutParams
                layoutOnBoardingIndicator?.addView(indicators[i])
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount: Int = layoutOnBoardingIndicator?.childCount ?: 1
        for (i in 0 until childCount) {
            val imageView = layoutOnBoardingIndicator?.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.onboarding_indicator_active_two
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.onboarding_indicator_inactive_two
                    )
                )
            }
        }
        if (index == (onBoardingAdapter?.itemCount ?: 1) - 1) {
            buttonOnBoardingAction?.text = getString(R.string.get_started)
        } else {
            buttonOnBoardingAction?.text = getString(R.string.next)
        }
    }

    private fun setOnboardingItem() {
        val onBoardingItems: MutableList<OnBoardingItem> = ArrayList<OnBoardingItem>()
        val itemFastFood = OnBoardingItem()
        itemFastFood.title = applicationContext.getString(R.string.block_call)
        itemFastFood.description = ""
        itemFastFood.image = R.drawable.choose_your_meal
        val itemPayOnline = OnBoardingItem()
        itemPayOnline.title = applicationContext.getString(R.string.block_message)
        itemPayOnline.description = ""
        itemPayOnline.image = R.drawable.choose_your_payment
        val itemEatTogether = OnBoardingItem()
        itemEatTogether.title = applicationContext.getString(R.string.warning_new_prototype)
        itemEatTogether.description = ""
        itemEatTogether.image = R.drawable.fast_delivery
        val itemDayAndNight = OnBoardingItem()
        itemDayAndNight.title = applicationContext.getString(R.string.commitment)
        itemDayAndNight.description = ""
        itemDayAndNight.image = R.drawable.day_and_night
        onBoardingItems.add(itemFastFood)
        onBoardingItems.add(itemPayOnline)
        onBoardingItems.add(itemEatTogether)
        onBoardingItems.add(itemDayAndNight)
        onBoardingAdapter = OnBoardingAdapter(onBoardingItems)
    }
}