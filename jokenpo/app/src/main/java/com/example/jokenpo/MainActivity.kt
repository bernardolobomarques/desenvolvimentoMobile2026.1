package com.example.jokenpo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var btnPedra: ImageButton
    private lateinit var btnPapel: ImageButton
    private lateinit var btnTesoura: ImageButton
    private lateinit var ivComp: ImageView
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa os componentes da interface com findViewById
        btnPedra = findViewById(R.id.btnPedra)
        btnPapel = findViewById(R.id.btnPapel)
        btnTesoura = findViewById(R.id.btnTesoura)
        ivComp = findViewById(R.id.ivComp)
        tvResult = findViewById(R.id.tvResult)

        ivComp.visibility = View.GONE

        btnPedra.setOnClickListener {
            tvResult.text = joga(0)
        }

        btnPapel.setOnClickListener {
            tvResult.text = joga(1)
        }

        btnTesoura.setOnClickListener {
            tvResult.text = joga(2)
        }
    }

    private fun joga(num: Int): String {
        ivComp.visibility = View.VISIBLE
        val comp = Random.nextInt(3)
        val resultado: String

        when (comp) {
            0 -> ivComp.setImageResource(R.drawable.jkp_pedra)
            1 -> ivComp.setImageResource(R.drawable.jkp_papel)
            2 -> ivComp.setImageResource(R.drawable.jkp_tesoura)
        }

        resultado = when {
            comp == num -> {
                tvResult.setTextColor(Color.GRAY)
                "Empatou"
            }
            (comp == 0 && num == 1) || (comp == 1 && num == 2) || (comp == 2 && num == 0) -> {
                tvResult.setTextColor(Color.GREEN)
                "Você ganhou!"
            }
            else -> {
                tvResult.setTextColor(Color.RED)
                "Perdeu"
            }
        }

        return resultado
    }
}
