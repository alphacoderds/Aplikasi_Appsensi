package com.example.aplikasiappsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var strTitle: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setInitLayout()

    }
    private fun setInitLayout(){

        cvAbsenMasuk.setOnClickListener{
            strTitle = "Absen Masuk"
            startActivity(intent)
        }

        cvAbsenKeluar.setOnClickListener {
            strTitle = "Absen Keluar"
            startActivity(intent)
        }

        cvPerizinan.setOnClickListener {
            strTitle = "Perizinan"
            startActivity(intent)
        }

        cvHistory.setOnClickListener {
            startActivity(intent)
        }

        imageLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Apakah Anda yakin ingin logout?")
            builder.setCancelable(true)
            builder.setNegativeButton("Batal") {dialog, which -> dialog.cancel() }
        }
    }

}

