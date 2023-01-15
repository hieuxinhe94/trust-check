package com.example.trustcheck.ui.views.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.PhoneData
import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.data.remote.ApiService
import com.example.trustcheck.data.repository.TrustCheckRepository
import com.example.trustcheck.di.NetworkApiModule
import com.example.trustcheck.ui.adapter.WarningAdapter
import com.example.trustcheck.ui.views.home.HomeActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.appbar.MaterialToolbar
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import kotlin.coroutines.CoroutineContext


class ReportActivity : AppCompatActivity(), CoroutineScope {
    private var recyclerCommentsView: RecyclerView? = null
    private var warningAdapter: WarningAdapter? = null
    private var liveChart: LiveChart? = null
    private var topBackScreen: MaterialToolbar? = null
    private var adView: AdView? = null
    private var adRequest: AdRequest? = null
    private var phoneDataRepository: TrustCheckRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        recyclerCommentsView = findViewById(R.id.recycler_view_comment)
        liveChart = findViewById(R.id.live_chart)
        topBackScreen = findViewById(R.id.top_back_screen)
        displayWarning()
        launch {
            val result =  phoneDataRepository?.getPhoneData("0967356524")
            onResult("result.") // onResult is called on the main thread
        }
        init()
        adView = findViewById(R.id.adView)
        initAdView()

    }

    private fun initAdView() {
        adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private suspend fun loadRemotePhoneProfile(phoneNumber: String) {
        val repos  = phoneDataRepository?.getPhoneData(phoneNumber)
    }

    private fun onResult(result: String) {


    }


    private fun displayWarning() {
        recyclerCommentsView?.layoutManager = LinearLayoutManager(this)

        val listWarning: MutableList<RecentWarning> = ArrayList()
        listWarning.add(RecentWarning("Lừa đảo đất nền", "18/10/2020 10:30"))
        listWarning.add(RecentWarning("Lừa đảo đất nền", "18/10/2020 10:30"))
        listWarning.add(RecentWarning("Lừa đảo đất nền", "18/10/2020 10:30"))
        listWarning.add(RecentWarning("Lừa đảo đất nền", "18/10/2020 10:30"))
        listWarning.add(RecentWarning("Lừa đảo đất nền", "18/10/2020 10:30"))


        warningAdapter = WarningAdapter(listWarning) {
            null
        }
        recyclerCommentsView?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerCommentsView?.adapter = warningAdapter
    }

    private fun init() {
        val dataset = Dataset(
            mutableListOf(
                DataPoint(0f, 1f),
                DataPoint(1f, 3f),
                DataPoint(2f, 6f)
            )
        )
        liveChart?.setDataset(dataset)?.drawYBounds()
            ?.drawBaseline()
            ?.drawFill()
            ?.drawDataset()

        topBackScreen?.setOnClickListener {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override val coroutineContext: CoroutineContext
        get() =   CoroutineScope(Dispatchers.Default).coroutineContext
}