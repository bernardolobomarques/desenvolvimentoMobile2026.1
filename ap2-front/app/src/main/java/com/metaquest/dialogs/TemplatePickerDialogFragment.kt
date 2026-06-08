package com.metaquest.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.metaquest.activities.GoalsListActivity
import com.metaquest.databinding.DialogTemplatePickerBinding
import com.metaquest.models.Goal
import com.metaquest.network.RetrofitClient
import com.metaquest.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.IOException

class TemplatePickerDialogFragment : DialogFragment() {

    private var _binding: DialogTemplatePickerBinding? = null
    private val binding get() = _binding!!

    data class Template(val titulo: String, val categoria: String, val dias: Int, val xp: Int)

    private val templates = listOf(
        Template("Estudar 1h por dia", "estudos", 30, 20),
        Template("Beber 2L de água", "saúde", 7, 10),
        Template("Treinar 3x por semana", "saúde", 30, 25),
        Template("Ler 20 páginas/dia", "estudos", 14, 15),
        Template("Economizar R\$100", "finanças", 30, 20),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogTemplatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvTemplates.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTemplates.adapter = TemplateAdapter(templates) { template ->
            usarTemplate(template)
        }
    }

    private fun usarTemplate(template: Template) {
        val goal = Goal(
            titulo = template.titulo,
            categoria = template.categoria,
            prioridade = "media",
            prazo = DateUtils.addDays(DateUtils.today(), template.dias),
            xp = template.xp,
            origem = "template"
        )
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.createGoal(goal)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Meta criada! +${template.xp} XP", Toast.LENGTH_SHORT).show()
                    (activity as? GoalsListActivity)?.refreshList()
                }
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Sem conexão", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class TemplateAdapter(
        private val items: List<Template>,
        private val onSelect: (Template) -> Unit
    ) : androidx.recyclerview.widget.RecyclerView.Adapter<TemplateAdapter.VH>() {

        inner class VH(val view: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val tv = android.widget.TextView(parent.context).apply {
                setPadding(48, 32, 48, 32)
                textSize = 16f
            }
            return VH(tv)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val t = items[position]
            (holder.view as android.widget.TextView).text = "${t.titulo} — ${t.dias} dias — ${t.xp} XP"
            holder.view.setOnClickListener { onSelect(t) }
        }

        override fun getItemCount() = items.size
    }

    companion object {
        fun newInstance() = TemplatePickerDialogFragment()
    }
}
