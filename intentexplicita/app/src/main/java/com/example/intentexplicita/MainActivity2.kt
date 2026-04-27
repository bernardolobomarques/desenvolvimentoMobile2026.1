package com.example.intentexplicita

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        var btSim = findViewById<Button>(R.id.btnDcSim)
        var btNao = findViewById<Button>(R.id.btnDcNao)
        val intent = Intent(this, MainActivity3::class.java)
        btSim.setOnClickListener {
            intent.putExtra("heroiDc", "Batman")
            startActivity(intent)
        }
        btNao.setOnClickListener {
            startActivity(intent)
        }
    }
}
