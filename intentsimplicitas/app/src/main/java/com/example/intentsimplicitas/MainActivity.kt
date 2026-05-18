package com.example.intentsimplicitas

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CallLog.Calls
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    val CALL_REQUEST_CODE = 101

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // LOG ADICIONADO AQUI
        Log.d("CicloDeVida", "MainActivity: onCreate")

        setupPermissions()

        val et_number = findViewById<EditText>(R.id.et_number)

        // Calling using intent
        val btn_call = findViewById<Button>(R.id.btn_call)
        btn_call.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + et_number.text)
            startActivity(intent)
        })

        // Call log
        val btn_call_log = findViewById<Button>(R.id.btn_call_log)
        btn_call_log.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.type = Calls.CONTENT_TYPE
            startActivity(intent)
        })

        //Contact
        val btn_contact = findViewById<Button>(R.id.btn_contact)
        btn_contact.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.Contacts.CONTENT_TYPE
            startActivity(intent)
        })

        // browser
        val btn_browser = findViewById<Button>(R.id.btn_browser)
        btn_browser.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://tutorial.eyehunts.com/")
            startActivity(intent)
        })

        // Gallery
        val btn_gallery = findViewById<Button>(R.id.btn_gallery)
        btn_gallery.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("content://media/external/images/media/")
            startActivity(intent)
        })

        // camera
        val btn_camera = findViewById<Button>(R.id.btn_camera)
        btn_camera.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        })

        // alarm
        val btn_alarm = findViewById<Button>(R.id.btn_alarm)
        btn_alarm.setOnClickListener(View.OnClickListener {
            val intent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
            startActivity(intent)
        })

        val btn_trocar = findViewById<Button>(R.id.btn_trocar)
        btn_trocar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        })
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("noone", "Permission to Call has denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CALL_PHONE),
            CALL_REQUEST_CODE)
    }

    // ==========================================
    // MÉTODOS DO CICLO DE VIDA ADICIONADOS ABAIXO
    // ==========================================

    override fun onStart() {
        super.onStart()
        Log.d("CicloDeVida", "MainActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("CicloDeVida", "MainActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("CicloDeVida", "MainActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("CicloDeVida", "MainActivity: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("CicloDeVida", "MainActivity: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CicloDeVida", "MainActivity: onDestroy")
    }
}