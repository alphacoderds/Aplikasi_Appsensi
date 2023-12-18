package com.example.aplikasiappsensi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val SPLASH_TIME_OUT: Long = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToLogin()
        }, SPLASH_TIME_OUT)
    }
private fun navigateToLogin() {
    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
    startActivity(intent)
    finish()
    }
}
