package com.metaquest.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.metaquest.databinding.ActivityWizardBinding
import com.metaquest.fragments.GoalWizardFragment
import com.metaquest.models.Goal
import com.metaquest.utils.DateUtils

class WizardActivity : AppCompatActivity(), GoalWizardFragment.OnWizardCompleteListener {

    private lateinit var binding: ActivityWizardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWizardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Assistente de Metas"

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, GoalWizardFragment())
                .commit()
        }
    }

    override fun onWizardComplete(tipo: String, dias: Int, prioridade: String, dificuldade: String) {
        val xp = when (dificuldade) { "desafiadora" -> 30; "moderada" -> 20; else -> 10 }
        val goal = Goal(
            titulo = "Meta de $tipo",
            categoria = tipo,
            prioridade = prioridade,
            prazo = DateUtils.addDays(DateUtils.today(), dias),
            xp = xp,
            origem = "assistente"
        )
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("goal", goal)
            putExtra("dificuldade", dificuldade)
            putExtra("dias", dias)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
