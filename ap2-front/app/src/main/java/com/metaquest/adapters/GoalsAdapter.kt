package com.metaquest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metaquest.databinding.ItemGoalBinding
import com.metaquest.models.Goal
import com.metaquest.utils.DateUtils

class GoalsAdapter(
    private val onItemClick: (Goal) -> Unit,
    private val onItemLongClick: (Goal) -> Unit
) : ListAdapter<Goal, GoalsAdapter.GoalViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Goal>() {
            override fun areItemsTheSame(old: Goal, new: Goal) = old.id == new.id
            override fun areContentsTheSame(old: Goal, new: Goal) = old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) =
        holder.bind(getItem(position))

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

            binding.root.alpha = if (goal.concluida) 0.5f else 1f

            binding.root.setOnClickListener { onItemClick(goal) }
            binding.root.setOnLongClickListener { onItemLongClick(goal); true }
        }
    }
}
