package com.example.aplikasiappsensi

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import android.util.Base64
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AbsenActivity : AppCompatActivity() {
    var REQ_CAMERA = 101
    var strCurrentLatitude = 0.0
    var strCurrentLongitude = 0.0
    var strFilePath: String = ""
    var strLatitude = "0"
    var strLongitude = "0"
    lateinit var fileDirectoty: File
    lateinit var imageFilename: File
    lateinit var exifInterface: ExifInterface
    lateinit var strBase64Photo: String
    lateinit var strCurrentLocation: String
    lateinit var strTitle: String
    lateinit var strTimeStamp: String
    lateinit var strImageName: String
    lateinit var progressDialog: ProgressDialog
    lateinit var inputNama: EditText
    lateinit var inputLokasi: EditText
    lateinit var inputTanggal: EditText
    lateinit var inputKeterangan: EditText
    lateinit var btnAbsen: Button
    lateinit var imageSelfie: ImageView
    lateinit var layoutImage: LinearLayout
    lateinit var tvTitle: TextView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)
        tvTitle = findViewById(R.id.tvTitle)
        inputNama = findViewById(R.id.inputNama)
        inputTanggal = findViewById(R.id.inputTanggal)
        inputKeterangan = findViewById(R.id.inputKeterangan)
        inputLokasi = findViewById(R.id.inputlokasiBaru)
        btnAbsen = findViewById(R.id.btnAbsen)
        imageSelfie = findViewById(R.id.imageSelfie)
        layoutImage = findViewById(R.id.layoutImage)
        firestore = FirebaseFirestore.getInstance()


        setInitLayout()
        setUploadData()
    }

    private fun setCurrentLocation() {
        progressDialog.show()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location ->
                progressDialog.dismiss()
                if (location != null) {
                    strCurrentLatitude = location.latitude
                    strCurrentLongitude = location.longitude
                    val geocoder = Geocoder(this@AbsenActivity, Locale.getDefault())
                    try {
                        val addressList =
                            geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1)
                        if (addressList != null && addressList.size > 0) {
                            strCurrentLocation = addressList[0].getAddressLine(0)
                            inputLokasi.setText(strCurrentLocation)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this@AbsenActivity,
                        "Ups, gagal mendapatkan lokasi. Silahkan periksa GPS atau koneksi internet Anda!",
                        Toast.LENGTH_SHORT).show()
                    strLatitude = "0"
                    strLongitude = "0"
                }
            }
    }

    private fun setInitLayout() {
        progressDialog = ProgressDialog(this)
        strTitle = intent.extras?.getString(DATA_TITLE).toString()
        tvTitle = findViewById(R.id.tvTitle)

        if (strTitle.isNotEmpty()) {
            tvTitle.text = strTitle
        }

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        inputTanggal.setOnClickListener {
            val tanggalAbsen = Calendar.getInstance()
            val date =
                OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    tanggalAbsen[Calendar.YEAR] = year
                    tanggalAbsen[Calendar.MONTH] = monthOfYear
                    tanggalAbsen[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val strFormatDefault = "dd MMMM yyyy HH:mm"
                    val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                    inputTanggal.setText(simpleDateFormat.format(tanggalAbsen.time))
                }
            DatePickerDialog(
                this@AbsenActivity, date,
                tanggalAbsen[Calendar.YEAR],
                tanggalAbsen[Calendar.MONTH],
                tanggalAbsen[Calendar.DAY_OF_MONTH]
            ).show()
        }

        imageSelfie.setOnClickListener {
//            Dexter.withContext(this@AbsenActivity)
//                .withPermissions(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//                .withListener(object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                        if (report.areAllPermissionsGranted()) {
//                            try {
//                                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                                cameraIntent.putExtra(
//                                    "com.google.assistant.extra.USE_FRONT_CAMERA",
//                                    true
//                                )
//                                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
//                                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
//                                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
//
//                                // Samsung
//                                cameraIntent.putExtra("camerafacing", "front")
//                                cameraIntent.putExtra("previous_mode", "front")
//
//                                // Huawei
//                                cameraIntent.putExtra("default_camera", "1")
//                                cameraIntent.putExtra(
//                                    "default_mode",
//                                    "com.huawei.camera2.mode.photo.PhotoMode")
//                                cameraIntent.putExtra(
//                                    MediaStore.EXTRA_OUTPUT,
//                                    FileProvider.getUriForFile(
//                                        this@AbsenActivity,
//                                        BuildConfig.APPLICATION_ID + ".provider",
//                                        createImageFile()
//                                    )
//                                )
//                                startActivityForResult(cameraIntent, REQ_CAMERA)
//                            } catch (ex: IOException) {
//                                Toast.makeText(this@AbsenActivity,
//                                    "Ups, gagal membuka kamera", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(
//                        permissions: List<PermissionRequest>,
//                        token: PermissionToken) {
//                        token.continuePermissionRequest()
//                    }
//                }).check()
        }
    }



    private fun setUploadData() {
        btnAbsen.setOnClickListener {
            val strNama = inputNama.text.toString()
            val strTanggal = inputTanggal.text.toString()
            val strKeterangan = inputKeterangan.text.toString()
            val lokasi = inputLokasi.text.toString()
            if (strFilePath.equals(null) || strNama.isEmpty() 
                || strTanggal.isEmpty() || strKeterangan.isEmpty() || lokasi.isEmpty()) {
                Toast.makeText(this@AbsenActivity,
                    "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show()
            }else{
                val mappData = hashMapOf(
                    "nama" to strNama,
                    "tanggal" to strTanggal,
                    "alamat" to lokasi,
                    "keterangan" to strKeterangan
                )
                firestore.collection("user").add(mappData).addOnSuccessListener {
                    Toast.makeText(this, "Tambah Data Berhasil", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        strTimeStamp = SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(Date())
        strImageName = "IMG_"
        fileDirectoty = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "")
        imageFilename = File.createTempFile(strImageName, ".jpg", fileDirectoty)
        strFilePath = imageFilename.getAbsolutePath()
        return imageFilename
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        convertImage(strFilePath)
    }

    private fun convertImage(imageFilePath: String?) {
        val imageFile = File(imageFilePath)
        if (imageFile.exists()) {
            val options = BitmapFactory.Options()
            var bitmapImage = BitmapFactory.decodeFile(strFilePath, options)

            try {
                exifInterface = ExifInterface(imageFile.absolutePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }

            bitmapImage = Bitmap.createBitmap(
                bitmapImage,
                0,
                0,
                bitmapImage.width,
                bitmapImage.height,
                matrix,
                true
            )

            if (bitmapImage == null) {
                Toast.makeText(this@AbsenActivity,
                    "Ups, foto kamu belum ada!", Toast.LENGTH_LONG).show()
            } else {
                val resizeImage = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
                val scaledBitmap = Bitmap.createScaledBitmap(bitmapImage, 512, resizeImage, true)
                Glide.with(this)
                    .load(scaledBitmap)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_photo_camera)
                    .into(imageSelfie)
                strBase64Photo = bitmapToBase64(scaledBitmap)
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA_TITLE = "TITLE"
    }
}