package com.metaquest.activities

import android.app.DatePickerDialog
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
import java.util.Calendar

class CreateGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGoalBinding

    private val categorias = listOf("estudos", "saúde", "finanças", "pessoal")
    private val prioridades = listOf("baixa", "media", "alta")
    private var selectedPrazo: String? = null

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

        binding.etPrazo.setOnClickListener { mostrarDatePicker() }
        binding.tilPrazo.setOnClickListener { mostrarDatePicker() }
        binding.tilPrazo.setEndIconOnClickListener {
            selectedPrazo = null
            binding.etPrazo.setText("")
        }

        binding.chip7dias.setOnClickListener { setDias(7) }
        binding.chip30dias.setOnClickListener { setDias(30) }
        binding.chip90dias.setOnClickListener { setDias(90) }

        binding.btnSalvar.setOnClickListener { salvarMeta() }
    }

    private fun setDias(dias: Int) {
        selectedPrazo = DateUtils.addDays(DateUtils.today(), dias)
        binding.etPrazo.setText(DateUtils.toDisplay(selectedPrazo))
    }

    private fun mostrarDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedPrazo = DateUtils.fromCalendar(year, month, day)
                binding.etPrazo.setText(DateUtils.toDisplay(selectedPrazo))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = System.currentTimeMillis() }.show()
    }

    private fun salvarMeta() {
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
                val response = RetrofitClient.goals.createGoal(goal)
                if (response.isSuccessful) {
                    val xp = response.body()?.xp ?: 10
                    Toast.makeText(this@CreateGoalActivity, "Meta criada! +$xp XP 🎯", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateGoalActivity, "Erro ${response.code()}: verifique os dados", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@CreateGoalActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
