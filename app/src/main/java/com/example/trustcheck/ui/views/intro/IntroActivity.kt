package com.example.trustcheck.ui.views.intro


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.trustcheck.R
import com.example.trustcheck.ui.views.home.HomeActivity
import com.example.trustcheck.ui.views.report.ReportActivity
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure don't call setContentView!
        setupViews()
        setupPermissionAsk();
    }

    private fun setupViews() {
        addSlide(
            AppIntroFragment.createInstance(
                imageDrawable = R.drawable.logo,
                backgroundColorRes = R.color.color_blue,
                title = "Welcome to TrustCheck",
                description = "Always beside of you, Always keep you save!",
        ))

        addSlide(AppIntroFragment.createInstance(
            imageDrawable = R.drawable.undraw_calling,
            title = "Trust Phone Number",
            backgroundColorRes = R.color.color_blue,
            description = "Telesales, Scam, Missed call for fee...\nSetting for blocks or ignore..."
        ))

        addSlide(AppIntroFragment.createInstance(
            imageDrawable = R.drawable.undraw_ideas,
            title = "Trust SMS",
            backgroundColorRes = R.color.color_blue,
            description = "Ads message, Scam, Fake Brand \nSetting to auto delete or block..."
        ))

        addSlide(AppIntroFragment.createInstance(
            imageDrawable = R.drawable.undraw_the_search,
            title = "Search",
            backgroundColorRes = R.color.color_blue,
            description = "Search any scammers name including name, phone number, text messages, website..."
        ))

        addSlide(AppIntroFragment.createInstance(
            imageDrawable = R.drawable.undraw_environment,
            title = "Report and help others",
            backgroundColorRes = R.color.color_blue,
            description = "Let's share your known and cases to prevent that problem happen again!"
        ))

        //TODO: Change images

        isSkipButtonEnabled = false;
        isColorTransitionsEnabled = true
        isIndicatorEnabled = true

        setTransformer(AppIntroPageTransformerType.Fade )
        showStatusBar(false)
        setDoneText(R.string.understand)
        setColorDoneText(R.color.color_secondary)
        // Change Indicator Color
        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.color_red),
            unselectedIndicatorColor = getColor(R.color.color_blue)
        )
        // Switch from Dotted Indicator to Progress Indicator
        setProgressIndicator()
        // Fullscreen experience
        setImmersiveMode()
        setupPermissionAsk();
        AppIntroCustomLayoutFragment.newInstance(R.layout.activity_intro)
    }

    private fun setupPermissionAsk() {
        // Request permission (must declare in the manifest before asking)
        askForPermissions(arrayOf(Manifest.permission.CALL_PHONE),
            2, false)
        askForPermissions(arrayOf(Manifest.permission.READ_SMS),
            3, false)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }

    override fun finish() {
        showStatusBar(true)
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onUserDeniedPermission(permissionName: String) {
        // User pressed "Deny" on the permission dialog
        println("User pressed \"Deny\" on the permission dialog: $permissionName");
    }
    override fun onUserDisabledPermission(permissionName: String) {
        // User pressed "Deny" + "Don't ask again" on the permission dialog
        println("User pressed \"Deny\" + \"Don't ask again\" on the permission dialog: $permissionName");
    }
}