package com.metaquest.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.metaquest.R
import com.metaquest.adapters.GoalsAdapter
import com.metaquest.databinding.ActivityGoalsListBinding
import com.metaquest.dialogs.GoalActionDialogFragment
import com.metaquest.models.Goal
import com.metaquest.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class GoalsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsListBinding
    private lateinit var adapter: GoalsAdapter
    private var allGoals: List<Goal> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = GoalsAdapter(
            onItemClick = { openActionDialog(it) },
            onItemLongClick = { openActionDialog(it) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.chipGroupFiltro.setOnCheckedStateChangeListener { _, _ -> applyFilter() }
    }

    override fun onResume() {
        super.onResume()
        carregarMetas()
    }

    private fun carregarMetas() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.goals.listGoals()
                if (response.isSuccessful) {
                    allGoals = response.body() ?: emptyList()
                    applyFilter()
                } else {
                    Toast.makeText(this@GoalsListActivity, "Erro ao carregar metas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@GoalsListActivity, "Sem conexão com o servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyFilter() {
        val filtered = when (binding.chipGroupFiltro.checkedChipId) {
            R.id.chipAtivas     -> allGoals.filter { !it.concluida }
            R.id.chipConcluidas -> allGoals.filter { it.concluida }
            else                -> allGoals
        }
        adapter.submitList(filtered)

        val isEmpty = filtered.isEmpty()
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        if (isEmpty) {
            binding.tvEmptyTitle.text = when (binding.chipGroupFiltro.checkedChipId) {
                R.id.chipAtivas     -> "Nenhuma meta ativa."
                R.id.chipConcluidas -> "Nenhuma meta concluída ainda."
                else                -> "Nenhuma meta ainda."
            }
            binding.tvEmptySubtitle.text = when (binding.chipGroupFiltro.checkedChipId) {
                R.id.chipConcluidas -> "Conclua uma meta para vê-la aqui!"
                else                -> "Crie sua primeira meta!"
            }
        }
    }

    private fun openActionDialog(goal: Goal) {
        GoalActionDialogFragment.newInstance(goal).show(supportFragmentManager, "goal_action")
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }

    fun refreshList() = carregarMetas()
}
