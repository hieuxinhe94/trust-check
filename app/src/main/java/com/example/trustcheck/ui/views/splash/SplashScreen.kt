package com.example.trustcheck.ui.views.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.trustcheck.R
import com.example.trustcheck.ui.utils.Common
import com.example.trustcheck.ui.views.home.HomeActivity
import com.example.trustcheck.ui.views.intro.IntroduceActivity

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent = if (Common().getFirstInstall()) {
                Intent(this, HomeActivity::class.java)

            } else {
                Intent(this, IntroduceActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}