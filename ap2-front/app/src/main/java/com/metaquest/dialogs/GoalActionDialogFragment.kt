package com.metaquest.dialogs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.metaquest.activities.GoalsListActivity
import com.metaquest.models.Goal
import com.metaquest.network.RetrofitClient
import com.metaquest.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.IOException

class GoalActionDialogFragment : DialogFragment() {

    private lateinit var goal: Goal

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(requireContext())
        .setTitle(goal.titulo)
        .setItems(arrayOf("Atualizar Progresso", "Concluir", "Duplicar", "Adiar +7 dias", "Excluir")) { _, which ->
            when (which) {
                0 -> abrirProgressoDialog()
                1 -> concluirMeta()
                2 -> duplicarMeta()
                3 -> adiarMeta()
                4 -> confirmarExclusao()
            }
        }
        .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        goal = requireArguments().getParcelable("goal")!!
    }

    private fun abrirProgressoDialog() {
        ProgressUpdateDialogFragment.newInstance(goal.id!!, goal.progresso)
            .show(parentFragmentManager, "progress_update")
        dismiss()
    }

    private fun concluirMeta() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.concludeGoal(goal.id!!)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Meta concluída! +20 XP 🎉", Toast.LENGTH_SHORT).show()
                    (activity as? GoalsListActivity)?.refreshList()
                } else {
                    Toast.makeText(requireContext(), "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Sem conexão", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    private fun duplicarMeta() {
        val copia = goal.copy(id = null, titulo = "${goal.titulo} (cópia)", concluida = false, progresso = 0, origem = "manual")
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.createGoal(copia)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Meta duplicada!", Toast.LENGTH_SHORT).show()
                    (activity as? GoalsListActivity)?.refreshList()
                }
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Sem conexão", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    private fun adiarMeta() {
        val novoPrazo = DateUtils.addDays(goal.prazo ?: DateUtils.today(), 7)
        val atualizado = goal.copy(prazo = novoPrazo)
        lifecycleScope.launch {
            try {
                RetrofitClient.goals.updateGoal(goal.id!!, atualizado)
                Toast.makeText(requireContext(), "Meta adiada 7 dias", Toast.LENGTH_SHORT).show()
                (activity as? GoalsListActivity)?.refreshList()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Sem conexão", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    private fun confirmarExclusao() {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir meta?")
            .setMessage("Esta ação não pode ser desfeita.")
            .setPositiveButton("Excluir") { _, _ -> excluirMeta() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun excluirMeta() {
        lifecycleScope.launch {
            try {
                RetrofitClient.goals.deleteGoal(goal.id!!)
                Toast.makeText(requireContext(), "Meta excluída", Toast.LENGTH_SHORT).show()
                (activity as? GoalsListActivity)?.refreshList()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Sem conexão", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    companion object {
        fun newInstance(goal: Goal) = GoalActionDialogFragment().apply {
            arguments = Bundle().apply { putParcelable("goal", goal) }
        }
    }
}
