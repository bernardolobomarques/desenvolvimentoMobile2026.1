package com.metaquest.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.metaquest.databinding.ActivityResultBinding
import com.metaquest.models.Goal
import com.metaquest.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var goal: Goal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Resultado"

        @Suppress("DEPRECATION")
        goal = intent.getParcelableExtra("goal") ?: run { finish(); return }
        val dificuldade = intent.getStringExtra("dificuldade") ?: "moderada"
        val dias = intent.getIntExtra("dias", 30)

        val classificacao = when {
            dias <= 7  -> "Meta curta (até 7 dias)"
            dias <= 30 -> "Meta média (até 30 dias)"
            else       -> "Meta longa (mais de 30 dias)"
        }

        binding.tvClassificacao.text = classificacao
        binding.tvDificuldade.text = "Dificuldade: $dificuldade"
        binding.tvXpSugerido.text = "XP sugerido: ${goal.xp}"
        binding.tvTituloMeta.text = goal.titulo

        val badgeRes = when (dificuldade) {
            "desafiadora" -> com.metaquest.R.drawable.ic_badge_gold
            "moderada"    -> com.metaquest.R.drawable.ic_badge_silver
            else          -> com.metaquest.R.drawable.ic_badge_bronze
        }
        binding.ivBadge.setImageResource(badgeRes)

        binding.btnSalvarMeta.setOnClickListener { salvarMeta() }
        binding.btnCompartilhar.setOnClickListener { compartilhar() }
    }

    private fun salvarMeta() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.createGoal(goal)
                if (response.isSuccessful) {
                    Toast.makeText(this@ResultActivity, "Meta criada! +${goal.xp} XP 🎯", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ResultActivity, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@ResultActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun compartilhar() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Criei a meta '${goal.titulo}' no MetaQuest! 🎯 Vou ganhar ${goal.xp} XP ao concluir!")
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
