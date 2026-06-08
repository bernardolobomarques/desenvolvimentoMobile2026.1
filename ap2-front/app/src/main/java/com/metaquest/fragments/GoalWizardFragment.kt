package com.metaquest.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.metaquest.databinding.FragmentGoalWizardBinding

class GoalWizardFragment : Fragment() {

    interface OnWizardCompleteListener {
        fun onWizardComplete(tipo: String, dias: Int, prioridade: String, dificuldade: String)
    }

    private var _binding: FragmentGoalWizardBinding? = null
    private val binding get() = _binding!!
    private var listener: OnWizardCompleteListener? = null

    private var etapa = 0
    private var tipoSelecionado = "estudos"
    private var diasSelecionado = 7
    private var prioridadeSelecionada = "media"
    private var dificuldadeSelecionada = "moderada"

    private val perguntas = listOf(
        "Qual tipo de meta você quer criar?",
        "Quanto tempo você tem disponível?",
        "Qual é a prioridade desta meta?",
        "Qual o nível de dificuldade?"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnWizardCompleteListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGoalWizardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        atualizarEtapa()

        binding.btnAvancar.setOnClickListener {
            coletarResposta()
            if (etapa < perguntas.size - 1) {
                etapa++
                atualizarEtapa()
            } else {
                listener?.onWizardComplete(tipoSelecionado, diasSelecionado, prioridadeSelecionada, dificuldadeSelecionada)
            }
        }
    }

    private fun atualizarEtapa() {
        binding.tvPergunta.text = perguntas[etapa]
        binding.progressWizard.progress = ((etapa + 1) * 100) / perguntas.size

        binding.rgOpcoes.removeAllViews()
        val opcoes = when (etapa) {
            0 -> listOf("estudos", "saúde", "finanças", "pessoal")
            1 -> listOf("1 dia", "7 dias", "30 dias")
            2 -> listOf("baixa", "media", "alta")
            3 -> listOf("fácil", "moderada", "desafiadora")
            else -> emptyList()
        }

        opcoes.forEachIndexed { idx, opcao ->
            val rb = android.widget.RadioButton(requireContext()).apply {
                id = idx
                text = opcao
                if (idx == 0) isChecked = true
            }
            binding.rgOpcoes.addView(rb)
        }

        binding.btnAvancar.text = if (etapa == perguntas.size - 1) "Finalizar" else "Próximo"
    }

    private fun coletarResposta() {
        val checkedId = binding.rgOpcoes.checkedRadioButtonId
        when (etapa) {
            0 -> {
                val tipos = listOf("estudos", "saúde", "finanças", "pessoal")
                tipoSelecionado = tipos.getOrElse(checkedId) { "estudos" }
            }
            1 -> {
                diasSelecionado = when (checkedId) { 0 -> 1; 2 -> 30; else -> 7 }
            }
            2 -> {
                val prios = listOf("baixa", "media", "alta")
                prioridadeSelecionada = prios.getOrElse(checkedId) { "media" }
            }
            3 -> {
                val difs = listOf("fácil", "moderada", "desafiadora")
                dificuldadeSelecionada = difs.getOrElse(checkedId) { "moderada" }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = GoalWizardFragment()
    }
}
