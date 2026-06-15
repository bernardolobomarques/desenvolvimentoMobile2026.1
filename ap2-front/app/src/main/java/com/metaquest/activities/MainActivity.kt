package com.metaquest.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.metaquest.databinding.ActivityMainBinding
import com.metaquest.network.RetrofitClient
import com.metaquest.utils.GamificationUtils
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastFetchTime = 0L

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
        if (System.currentTimeMillis() - lastFetchTime > 5_000) {
            carregarResumo()
        }
    }

    private fun carregarResumo() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.gamification.getSummary()
                if (response.isSuccessful) {
                    val summary = response.body()!!
                    lastFetchTime = System.currentTimeMillis()
                    binding.tvSaudacao.text = "Olá, aventureiro!"
                    binding.tvNivel.text = "Nível ${summary.nivel}  ·  ${GamificationUtils.xpParaProximoNivel(summary.xp_total)} XP para o próximo nível"
                    binding.tvXp.text = "⚡ ${summary.xp_total} XP"
                    binding.tvStreak.text = "🔥 ${summary.streak} ${if (summary.streak == 1) "dia" else "dias"}"
                    binding.progressDia.progress = GamificationUtils.progressoNivel(summary.xp_total)
                }
            } catch (e: IOException) {
                binding.tvSaudacao.text = "Sem conexão com o servidor"
                binding.tvNivel.text = "Verifique sua conexão e tente novamente"
            }
        }
    }
}
