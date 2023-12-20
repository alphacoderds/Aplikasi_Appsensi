package com.example.aplikasiappsensi

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasiappsensi.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var session: SessionLogin
    lateinit var strName: String
    lateinit var strUsername: String
    lateinit var strPassword: String

    var REQ_PERMISSION = 101
    private lateinit var btnCreate: Button
    private lateinit var inputNama: EditText
    private lateinit var inputUsername: EditText
    private lateinit var inputPassword: EditText
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var txtLogin: TextView
    private lateinit var tvSignIn: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_register)

        btnCreate = findViewById(R.id.btnCreate)
        inputNama = findViewById(R.id.inputNama)
        inputUsername = findViewById(R.id.inputUsername)
        inputPassword = findViewById(R.id.inputPassword)
        tvSignIn = findViewById(R.id.tvSignIn)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        txtLoginListener()
        setInitLayout()



    }

    private fun txtLoginListener() {
        val tvSignIn = findViewById<TextView>(R.id.tvSignIn)
        tvSignIn.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setInitLayout() {

        btnCreate.setOnClickListener {
            Log.e("error","test")
            strName = inputNama.text.toString()
            strUsername = inputUsername.text.toString()
            strPassword = inputPassword.text.toString()

            if (strName.isEmpty() || strUsername.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Form tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                firebaseAuth.createUserWithEmailAndPassword(strUsername, strPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi berhasil!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            session.createLoginSession(strName)
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi gagal: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permission: Array<String>,
            grantResult: IntArray
        ) {
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

