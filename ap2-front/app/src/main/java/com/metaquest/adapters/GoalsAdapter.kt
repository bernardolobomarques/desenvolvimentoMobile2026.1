package com.metaquest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metaquest.databinding.ItemGoalBinding
import com.metaquest.models.Goal
import com.metaquest.utils.DateUtils

class GoalsAdapter(
    private val onItemClick: (Goal) -> Unit,
    private val onItemLongClick: (Goal) -> Unit
) : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private val items = mutableListOf<Goal>()

    fun submitList(goals: List<Goal>) {
        items.clear()
        items.addAll(goals)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    inner class GoalViewHolder(private val binding: ItemGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goal: Goal) {
            binding.tvGoalTitle.text = goal.titulo
            binding.tvGoalPrazo.text = DateUtils.toDisplay(goal.prazo)
            binding.tvGoalCategoria.text = goal.categoria ?: "—"
            binding.progressBar.progress = goal.progresso
            binding.tvProgresso.text = "${goal.progresso}%"

            val prioRes = when (goal.prioridade) {
                "alta"  -> com.metaquest.R.drawable.ic_priority_high
                "media" -> com.metaquest.R.drawable.ic_priority_medium
                else    -> com.metaquest.R.drawable.ic_priority_low
            }
            binding.ivPriority.setImageResource(prioRes)

            if (goal.concluida) {
                binding.root.alpha = 0.5f
            } else {
                binding.root.alpha = 1f
            }

            binding.root.setOnClickListener { onItemClick(goal) }
            binding.root.setOnLongClickListener { onItemLongClick(goal); true }
        }
    }
}
