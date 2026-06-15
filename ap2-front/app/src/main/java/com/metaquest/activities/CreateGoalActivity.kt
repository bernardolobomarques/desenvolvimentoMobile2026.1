package com.metaquest.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.metaquest.databinding.ActivityCreateGoalBinding
import com.metaquest.models.Goal
import com.metaquest.network.RetrofitClient
import com.metaquest.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.IOException

class CreateGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGoalBinding

    private val categorias = listOf("estudos", "saúde", "finanças", "pessoal")
    private val prioridades = listOf("baixa", "media", "alta")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.spinnerCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerPrioridade.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val goalId = intent.getLongExtra("goal_id", -1L)
        if (goalId != -1L) carregarMeta(goalId)

        binding.btnSalvar.setOnClickListener { salvarMeta(goalId) }
    }

    private fun carregarMeta(id: Long) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.getGoal(id)
                if (response.isSuccessful) {
                    val goal = response.body()!!
                    binding.etTitulo.setText(goal.titulo)
                    binding.etDescricao.setText(goal.descricao ?: "")
                    val catIdx = categorias.indexOf(goal.categoria).coerceAtLeast(0)
                    val prioIdx = prioridades.indexOf(goal.prioridade).coerceAtLeast(0)
                    binding.spinnerCategoria.setSelection(catIdx)
                    binding.spinnerPrioridade.setSelection(prioIdx)
                }
            } catch (e: IOException) {
                Toast.makeText(this@CreateGoalActivity, "Sem conexão", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun salvarMeta(existingId: Long) {
        val titulo = binding.etTitulo.text.toString().trim()
        if (titulo.isEmpty()) {
            binding.tilTitulo.error = "Título obrigatório"
            return
        }
        binding.tilTitulo.error = null

        val goal = Goal(
            titulo = titulo,
            descricao = binding.etDescricao.text.toString().trim().ifEmpty { null },
            categoria = categorias[binding.spinnerCategoria.selectedItemPosition],
            prioridade = prioridades[binding.spinnerPrioridade.selectedItemPosition],
            prazo = DateUtils.today(),
            origem = "manual"
        )

        lifecycleScope.launch {
            try {
                val response = if (existingId != -1L) {
                    RetrofitClient.goals.updateGoal(existingId, goal)
                } else {
                    RetrofitClient.goals.createGoal(goal)
                }
                if (response.isSuccessful) {
                    val xp = response.body()?.xp ?: 10
                    Toast.makeText(this@CreateGoalActivity, "Meta salva! +$xp XP", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateGoalActivity, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@CreateGoalActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
