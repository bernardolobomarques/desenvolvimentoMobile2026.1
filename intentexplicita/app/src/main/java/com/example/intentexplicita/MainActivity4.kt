package com.example.intentexplicita

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent

class MainActivity4 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)
        var btSim = findViewById<Button>(R.id.btnTeiasSim)
        var btNao = findViewById<Button>(R.id.btnTeiasNao)
        val intent = Intent(this, MainActivity5::class.java)
        if (getIntent().hasExtra("heroiDc")) {
            intent.putExtra("heroiDc", "Batman")
        }
        if (getIntent().hasExtra("heroiEscudo")) {
            intent.putExtra("heroiEscudo", "Capitão América")
        }
        btSim.setOnClickListener {
            intent.putExtra("heroiTeia", "Homem-Aranha")
            startActivity(intent)
        }
        btNao.setOnClickListener {
            startActivity(intent)
        }
    }
}