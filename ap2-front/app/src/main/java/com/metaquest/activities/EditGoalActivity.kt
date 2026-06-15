package com.metaquest.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.metaquest.databinding.ActivityEditGoalBinding
import com.metaquest.models.Goal
import com.metaquest.network.RetrofitClient
import com.metaquest.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Calendar

class EditGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGoalBinding

    private val categorias = listOf("estudos", "saúde", "finanças", "pessoal")
    private val prioridades = listOf("baixa", "media", "alta")
    private var selectedPrazo: String? = null
    private var goalId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        goalId = intent.getLongExtra("goal_id", -1L)
        if (goalId == -1L) { finish(); return }

        binding.spinnerCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerPrioridade.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.etPrazo.setOnClickListener { mostrarDatePicker() }
        binding.tilPrazo.setOnClickListener { mostrarDatePicker() }
        binding.tilPrazo.setEndIconOnClickListener {
            selectedPrazo = null
            binding.etPrazo.setText("")
            binding.chipGroupPrazo.clearCheck()
        }

        binding.chipGroupPrazo.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val dias = when (checkedIds[0]) {
                    com.metaquest.R.id.chip7dias  -> 7
                    com.metaquest.R.id.chip30dias -> 30
                    else                          -> 90
                }
                selectedPrazo = DateUtils.addDays(DateUtils.today(), dias)
                binding.etPrazo.setText(DateUtils.toDisplay(selectedPrazo))
            }
        }

        binding.btnSalvar.setOnClickListener { salvarAlteracoes() }

        carregarMeta()
    }

    private fun carregarMeta() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.getGoal(goalId)
                if (response.isSuccessful) {
                    val goal = response.body()!!
                    preencherFormulario(goal)
                } else {
                    Toast.makeText(this@EditGoalActivity, "Erro ao carregar meta", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: IOException) {
                Toast.makeText(this@EditGoalActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun preencherFormulario(goal: Goal) {
        binding.etTitulo.setText(goal.titulo)
        binding.etDescricao.setText(goal.descricao ?: "")

        selectedPrazo = goal.prazo
        binding.etPrazo.setText(if (goal.prazo != null) DateUtils.toDisplay(goal.prazo) else "")

        val catIdx = categorias.indexOf(goal.categoria).coerceAtLeast(0)
        val prioIdx = prioridades.indexOf(goal.prioridade).coerceAtLeast(0)
        binding.spinnerCategoria.setSelection(catIdx)
        binding.spinnerPrioridade.setSelection(prioIdx)
    }

    private fun mostrarDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedPrazo = DateUtils.fromCalendar(year, month, day)
                binding.etPrazo.setText(DateUtils.toDisplay(selectedPrazo))
                binding.chipGroupPrazo.clearCheck()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun salvarAlteracoes() {
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
            prazo = selectedPrazo,
            origem = "manual"
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.updateGoal(goalId, goal)
                if (response.isSuccessful) {
                    Toast.makeText(this@EditGoalActivity, "Meta atualizada!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditGoalActivity, "Erro ${response.code()}: verifique os dados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@EditGoalActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
