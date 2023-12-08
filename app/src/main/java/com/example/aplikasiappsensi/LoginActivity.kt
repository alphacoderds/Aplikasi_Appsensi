package com.example.aplikasiappsensi

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest

class LoginActivity : AppCompatActivity() {
    lateinit var session: SessionLogin
    lateinit var strName: String
    lateinit var strPassword: String
    var REQ_PERMISSION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setPermission()
        setInitLayout()
    }

    private fun setPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_PERMISSION
            )
        }
    }

    private fun setInitLayout() {
        session = SessionLogin(applicationContext)

        if (session.isLoggedIn()){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            strName = inputNama.text.toString()
            strPassword = inputPassword.text.toString()

            if (strName.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                session.createLoginSession(strName)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permission: Array<String>,
        grantResult: IntArray) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult)
        for (grantResult in grantResult) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}

