package com.metaquest.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.metaquest.databinding.ActivityMainBinding
import com.metaquest.network.RetrofitClient
import com.metaquest.utils.GamificationUtils
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNovaMeta.setOnClickListener {
            startActivity(Intent(this, CreateGoalActivity::class.java))
        }
        binding.btnAssistente.setOnClickListener {
            startActivity(Intent(this, WizardActivity::class.java))
        }
        binding.btnVerMetas.setOnClickListener {
            startActivity(Intent(this, GoalsListActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        carregarResumo()
    }

    private fun carregarResumo() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.gamification.getSummary()
                if (response.isSuccessful) {
                    val summary = response.body()!!
                    binding.tvXp.text = "XP: ${summary.xp_total}"
                    binding.tvNivel.text = "Nível ${summary.nivel}"
                    binding.tvStreak.text = "🔥 ${summary.streak} dias"
                    binding.progressDia.progress = GamificationUtils.progressoNivel(summary.xp_total)
                    binding.tvSaudacao.text = "Olá, aventureiro! Nível ${summary.nivel}"
                }
            } catch (e: IOException) {
                Toast.makeText(this@MainActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
