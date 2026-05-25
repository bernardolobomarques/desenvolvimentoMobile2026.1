package com.example.consumirapicrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.consumirapicrud.api.Device
import com.google.android.material.switchmaterial.SwitchMaterial

class DeviceAdapter(
    private var items: List<Device>,
    private val onItemClick: (Device) -> Unit,
    private val onDeleteClick: (Device, Int) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textType: TextView = itemView.findViewById(R.id.textType)
        val switchActive: SwitchMaterial = itemView.findViewById(R.id.switchActive)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = items[position]
        holder.textName.text = device.name
        holder.textType.text = device.type
        holder.switchActive.isChecked = device.isActive
        holder.itemView.setOnClickListener { onItemClick(device) }
        holder.buttonDelete.setOnClickListener { onDeleteClick(device, holder.bindingAdapterPosition) }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<Device>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        if (position !in items.indices) return
        val mutableItems = items.toMutableList()
        mutableItems.removeAt(position)
        items = mutableItems
        notifyItemRemoved(position)
    }
}
