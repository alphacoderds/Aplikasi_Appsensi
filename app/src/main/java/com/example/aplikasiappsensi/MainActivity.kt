package com.example.aplikasiappsensi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra("title", strTitle)
            startActivity(intent)
        }

        cvAbsenKeluar.setOnClickListener {
            strTitle = "Absen Keluar"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra("title", strTitle)
            startActivity(intent)
        }

        cvPerizinan.setOnClickListener {
            strTitle = "Perizinan"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra("title", strTitle)
            startActivity(intent)
        }

        cvHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        imageLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Apakah Anda yakin ingin logout?")
            builder.setCancelable(true)
            builder.setPositiveButton("Ya") { dialog, which ->
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("Batal") {dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}

