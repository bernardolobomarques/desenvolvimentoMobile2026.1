package com.example.helloworld

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalTime
import android.content.Intent


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var etNome = findViewById<EditText>(R.id.etNome)
        var btGerarSaudacao = findViewById<Button>(R.id.btGerarSaudacao)


        btGerarSaudacao.setOnClickListener {
            val nome = etNome.text.toString()
            val intent = Intent(this, MainActivity2::class.java)

            intent.putExtra("nome", nome)
            startActivity(intent)
        }
    }
}
