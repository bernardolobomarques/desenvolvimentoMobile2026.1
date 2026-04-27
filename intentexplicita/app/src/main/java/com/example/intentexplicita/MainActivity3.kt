package com.example.intentexplicita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        var btSim = findViewById<Button>(R.id.btnEscudoSim)
        var btNao = findViewById<Button>(R.id.btnEscudoNao)
        val intent = Intent(this, MainActivity4::class.java)
        if (getIntent().hasExtra("heroiDc")) {
            intent.putExtra("heroiDc", "Batman")
        }
        btSim.setOnClickListener {
            intent.putExtra("heroiEscudo", "Capitão América")
            startActivity(intent)
        }
        btNao.setOnClickListener {
            startActivity(intent)
        }
    }
}

