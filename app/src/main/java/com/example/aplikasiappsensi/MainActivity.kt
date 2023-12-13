package com.example.aplikasiappsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    lateinit var strTitle: String
    private lateinit var cvAbsenMasuk: CardView
    private lateinit var cvAbsenKeluar: CardView
    private lateinit var cvHistory: CardView
    private lateinit var cvPerizinan: CardView
    private lateinit var imageLogout: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cvAbsenMasuk = findViewById(R.id.cvAbsenMasuk)
        cvAbsenKeluar = findViewById(R.id.cvAbsenKeluar)
        cvHistory = findViewById(R.id.cvHistory)
        cvPerizinan = findViewById(R.id.cvPerizinan)
        imageLogout = findViewById(R.id.imageLogout)

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

