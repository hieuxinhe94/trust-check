package com.example.trustcheck.ui.views.home


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trustcheck.R
import com.example.trustcheck.data.models.PhoneData
import com.example.trustcheck.data.models.RecentWarning
import com.example.trustcheck.ui.adapter.WarningAdapter
import com.example.trustcheck.ui.helper.DBHelper
import com.example.trustcheck.ui.views.language.MultiLanguageScreen
import com.example.trustcheck.ui.views.menu.MenuActivity
import com.example.trustcheck.ui.views.report.ReportActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class HomeActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var warningAdapter: WarningAdapter? = null
    private var settingView: ImageView? = null
    private var editSearch: EditText? = null
    private var adView: AdView? = null
    private var adRequest: AdRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        displayWarning()


        clickAction()
    }

    private fun initView() {
        MobileAds.initialize(this)
        recyclerView = findViewById(R.id.recycler_view)
        settingView = findViewById(R.id.setting_icon)
        editSearch = findViewById(R.id.edtSearch)
        adView = findViewById(R.id.adView)
        initAdView()
        requestPermissions()
    }

    private fun initAdView() {
        adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)

    }

    private fun clickAction() {
        settingView?.setOnClickListener {

            startActivity(Intent(this, MenuActivity::class.java))
        }
        editSearch?.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus) {
                    hideSoftKeyboard()
                }
            }
        }
        editSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (editSearch!!.text.isNotEmpty() && isValidPhoneNumber(editSearch!!.text.toString())) {
                    var intent = Intent(applicationContext, ReportActivity::class.java)
                    startActivity(intent)
                }
            }

        })
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return if (phone.trim { it <= ' ' } != "" && phone.length >= 10) {
            Patterns.PHONE.matcher(phone).matches()
        } else false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.report -> {

                var intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var ok = true
        if (grantResults.isNotEmpty()) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    ok = false
                    break
                }
            }
        } else {
            ok = false
        }
        if (!ok) {
            requestPermissions()
        }
    }

    private fun displayResultPopup() {
        layoutInflater.inflate(R.layout.activity_result, null);
    }

    private fun displayWarning() {
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val listWarning: MutableList<RecentWarning> = ArrayList()
        val w1 = RecentWarning("ứng dụng lừa đảo")
        val w2 = RecentWarning("ứng dụng scam")
        val w3 = RecentWarning("ứng dụng vớ vẩn")
        val w4 = RecentWarning("ứng dụng gây hại")
        val w5 = RecentWarning("ứng dụng nghe lén")
        listWarning.add(w1)
        listWarning.add(w2)
        listWarning.add(w3)
        listWarning.add(w4)
        listWarning.add(w5)
        warningAdapter = WarningAdapter(listWarning)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView?.adapter = warningAdapter
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)
        val editLayout: LinearLayout = dialog.findViewById(R.id.layoutEdit)
        val shareLayout: LinearLayout = dialog.findViewById(R.id.layoutShare)
        val update: LinearLayout = dialog.findViewById(R.id.id_upgrade)
        val chooseLanguage: LinearLayout = dialog.findViewById(R.id.ic_language)
        editLayout.setOnClickListener {
            dialog.dismiss()
        }
        shareLayout.setOnClickListener {
            makeDefaultSMSAppButtonClick(it)
            dialog.dismiss()
        }
        update.setOnClickListener {
            dialog.dismiss()
        }
        chooseLanguage.setOnClickListener {
            save()
            startActivity(Intent(this, MultiLanguageScreen::class.java))
            dialog.dismiss()
        }
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }


    private fun requestPermissions() {
        val requiredPermissions: MutableList<String> = java.util.ArrayList()
        requiredPermissions.add(Manifest.permission.CALL_PHONE)
        requiredPermissions.add(Manifest.permission.READ_PHONE_STATE)
        requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requiredPermissions.add(Manifest.permission.READ_CONTACTS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            requiredPermissions.add(Manifest.permission.READ_CALL_LOG)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requiredPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
        }
        val missingPermissions: MutableList<String> = java.util.ArrayList()
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions.add(permission)
            }
        }
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(), 0
            )
        }

    }


    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun save() {
        val phoneData = PhoneData("0982160365", "", "", "", "", "", "", "")
        val dbHelper = DBHelper(this)
        dbHelper.addPhoneData(phoneData)
        dbHelper.addMessageData(phoneData)
    }

    fun makeDefaultSMSAppButtonClick(v: View?) {
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.packageName)
        startActivity(intent)
    }

}