package com.example.aplikasiappsensi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var session: SessionLogin
    private lateinit var strName: String
    private lateinit var strPassword: String
    private var REQ_PERMISSION = 101
    private lateinit var btnLogin: Button
    private lateinit var inputNama: EditText
    private lateinit var inputPassword: EditText
    private lateinit var tvCreateAccount: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin = findViewById(R.id.btnLogin)
        inputNama = findViewById(R.id.inputNama)
        inputPassword = findViewById(R.id.inputPassword)
        tvCreateAccount = findViewById(R.id.tvCreateAccount)

        auth = FirebaseAuth.getInstance()
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
                Toast.makeText(this@LoginActivity, "Form tidak boleh kosong!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    loginUser(strName, strPassword)
                }
            }
        }
        tvCreateAccountClickListener()
    }

    private fun tvCreateAccountClickListener() {
        tvCreateAccount.setOnClickListener {
            val intent = (Intent(this@LoginActivity, RegisterActivity::class.java))
            startActivity(intent)
        }
    }

    private suspend fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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

