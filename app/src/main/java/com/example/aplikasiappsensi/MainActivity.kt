package com.example.aplikasiappsensi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var strTitle : String = ""
    private lateinit var cvAbsenMasuk : CardView
    private lateinit var cvAbsenKeluar : CardView
    private lateinit var cvHistory : CardView
    private lateinit var cvPerizinan : CardView
    private lateinit var imageLogout : ImageView
    private lateinit var tvAbsenMasuk : TextView
    private lateinit var tvAbsenKeluar : TextView
    private lateinit var tvPerizinan : TextView
    private lateinit var tvRiwayatAbsen : TextView
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cvAbsenMasuk = findViewById(R.id.cvAbsenMasuk)
        cvAbsenKeluar = findViewById(R.id.cvAbsenKeluar)
        cvHistory = findViewById(R.id.cvHistory)
        cvPerizinan = findViewById(R.id.cvPerizinan)
        imageLogout = findViewById(R.id.imageLogout)
        firebaseAuth = FirebaseAuth.getInstance()
        tvAbsenMasuk = findViewById(R.id.tvAbsenMasuk)
        tvAbsenKeluar = findViewById(R.id.tvAbsenKeluar)
        tvPerizinan = findViewById(R.id.tvPerizinan)
        tvRiwayatAbsen = findViewById(R.id.tvRiwayatAbsen)

        setInitLayout()

    }
    private fun setInitLayout(){
        tvAbsenMasuk.setOnClickListener{
            strTitle = "Absen Masuk"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra("title", strTitle)
            startActivity(intent)
        }

        tvAbsenKeluar.setOnClickListener {
            strTitle = "Absen Keluar"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra("title", strTitle)
            startActivity(intent)
        }

        tvPerizinan.setOnClickListener {
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
                firebaseAuth.signOut()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("Batal") {dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}

