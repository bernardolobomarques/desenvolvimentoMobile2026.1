package com.metaquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.metaquest.R
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
            val ctx = binding.root.context

            binding.tvGoalTitle.text = goal.titulo
            binding.tvGoalCategoria.text = goal.categoria ?: "—"
            binding.progressBar.progress = goal.progresso
            binding.tvProgresso.text = "${goal.progresso}%"

            // Badge concluída
            binding.tvConcluida.visibility = if (goal.concluida) View.VISIBLE else View.GONE

            // Prazo contextual com cor de urgência
            val prazoLabel = DateUtils.prazoLabel(goal.prazo)
            val daysLeft = DateUtils.daysRemaining(goal.prazo)
            binding.tvGoalPrazo.text = prazoLabel
            binding.tvGoalPrazo.setTextColor(
                ContextCompat.getColor(ctx, when {
                    daysLeft == null  -> R.color.on_surface_variant
                    daysLeft < 0     -> R.color.priority_high
                    daysLeft == 0L   -> R.color.priority_high
                    daysLeft <= 3    -> R.color.priority_medium
                    else             -> R.color.on_surface_variant
                })
            )

            val (prioIcon, prioColor) = when (goal.prioridade) {
                "alta"  -> R.drawable.ic_priority_high   to R.color.priority_high
                "media" -> R.drawable.ic_priority_medium to R.color.priority_medium
                else    -> R.drawable.ic_priority_low    to R.color.priority_low
            }
            binding.ivPriority.setImageResource(prioIcon)
            binding.priorityStrip.setBackgroundColor(ContextCompat.getColor(ctx, prioColor))

            binding.root.alpha = if (goal.concluida) 0.6f else 1f

            binding.root.setOnClickListener { onItemClick(goal) }
            binding.root.setOnLongClickListener { onItemLongClick(goal); true }
        }
    }
}
