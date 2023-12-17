package com.example.aplikasiappsensi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.startActivity

class SplashActivity : AppCompatActivity() {
    companion object {
        // Waktu tunda sebelum beralih ke LoginActivity (dalam milidetik)
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
