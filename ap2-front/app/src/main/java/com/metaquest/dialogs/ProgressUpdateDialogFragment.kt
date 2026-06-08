package com.metaquest.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.metaquest.activities.GoalsListActivity
import com.metaquest.databinding.DialogProgressUpdateBinding
import com.metaquest.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class ProgressUpdateDialogFragment : DialogFragment() {

    private var _binding: DialogProgressUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogProgressUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val goalId = requireArguments().getLong("goal_id")
        val progressoAtual = requireArguments().getInt("progresso_atual")

        binding.seekBar.progress = progressoAtual
        binding.tvProgresso.text = "$progressoAtual%"

        binding.seekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvProgresso.text = "$progress%"
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        binding.btnMais10.setOnClickListener {
            binding.seekBar.progress = (binding.seekBar.progress + 10).coerceAtMost(100)
        }
        binding.btnMais25.setOnClickListener {
            binding.seekBar.progress = (binding.seekBar.progress + 25).coerceAtMost(100)
        }

        binding.btnSalvar.setOnClickListener {
            val novoProgresso = binding.seekBar.progress
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.goals.updateProgress(goalId, mapOf("progresso" to novoProgresso))
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Progresso atualizado! +5 XP", Toast.LENGTH_SHORT).show()
                        (activity as? GoalsListActivity)?.refreshList()
                    }
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Sem conexão", Toast.LENGTH_SHORT).show()
                }
                dismiss()
            }
        }

        binding.btnCancelar.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(goalId: Long, progressoAtual: Int) = ProgressUpdateDialogFragment().apply {
            arguments = Bundle().apply {
                putLong("goal_id", goalId)
                putInt("progresso_atual", progressoAtual)
            }
        }
    }
}
