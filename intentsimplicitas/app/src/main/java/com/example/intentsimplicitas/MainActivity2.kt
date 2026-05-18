package com.example.intentsimplicitas

import android.content.Intent
import android.os.Bundle
import android.util.Log // IMPORT ADICIONADO AQUI
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // LOG ADICIONADO AQUI
        Log.d("CicloDeVida", "MainActivity2: onCreate")

        val btn_voltar = findViewById<Button>(R.id.btn_voltar)
        btn_voltar.setOnClickListener(View.OnClickListener {
            // DICA: Em vez de criar um Intent novo para a MainActivity,
            // basta chamar "finish()" para destruir a MainActivity2 e voltar para a anterior.
            // Deixei o seu código original comentado abaixo caso prefira usar o Intent.

//            finish()

             val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)
        })
    }

    // ==========================================
    // MÉTODOS DO CICLO DE VIDA ADICIONADOS ABAIXO
    // ==========================================

    override fun onStart() {
        super.onStart()
        Log.d("CicloDeVida", "MainActivity2: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("CicloDeVida", "MainActivity2: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("CicloDeVida", "MainActivity2: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("CicloDeVida", "MainActivity2: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("CicloDeVida", "MainActivity2: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CicloDeVida", "MainActivity2: onDestroy")
    }
}