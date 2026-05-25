package com.example.intentexplicita

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import androidx.core.net.toUri

class MainActivity5 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)

        var btnVoltar = findViewById<Button>(R.id.btnVoltar)
        var btnGitHub = findViewById<Button>(R.id.btnGitHub)

        var tvHeroiFinal = findViewById<TextView>(R.id.tvHeroiFinal)
        if (getIntent().hasExtra("heroiDc")) {
            tvHeroiFinal.text = getIntent().getStringExtra("heroiDc")
        }
        if (getIntent().hasExtra("heroiEscudo")) {
            tvHeroiFinal.text = getIntent().getStringExtra("heroiEscudo")
        }
        if (getIntent().hasExtra("heroiTeia")) {
            tvHeroiFinal.text = getIntent().getStringExtra("heroiTeia")
        }
        else {
            tvHeroiFinal.text = "Vc nao é um heroi :("
        }

        btnVoltar.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnGitHub.setOnClickListener {
            intent = Intent(
                Intent.ACTION_VIEW,
                "https://github.com/bernardolobomarques/desenvolvimentoMobile2026.1/tree/main/intentexplicita".toUri()
            )
            startActivity(intent)
        }
    }
}
