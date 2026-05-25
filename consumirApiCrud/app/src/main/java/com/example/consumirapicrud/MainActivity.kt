package com.example.consumirapicrud

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumirapicrud.api.Device
import com.example.consumirapicrud.api.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerDevices)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DeviceAdapter(emptyList(), ::openDeviceForm, ::deleteDevice)
        recyclerView.adapter = adapter

        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, DeviceFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchDevices()
    }

    private fun fetchDevices() {
        RetrofitClient.deviceApi.getDevices().enqueue(object : Callback<List<Device>> {
            override fun onResponse(call: Call<List<Device>>, response: Response<List<Device>>) {
                if (response.isSuccessful) {
                    adapter.updateItems(response.body().orEmpty())
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_loading_devices),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Device>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    t.message ?: getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun openDeviceForm(device: Device) {
        val intent = Intent(this, DeviceFormActivity::class.java)
        val deviceId = device.id
        if (deviceId != null) {
            intent.putExtra(DeviceFormActivity.EXTRA_DEVICE_ID, deviceId)
        }
        startActivity(intent)
    }

    private fun deleteDevice(device: Device, position: Int) {
        val deviceId = device.id
        if (deviceId == null) {
            Toast.makeText(this, getString(R.string.error_deleting_device), Toast.LENGTH_SHORT)
                .show()
            return
        }

        RetrofitClient.deviceApi.deleteDevice(deviceId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    adapter.removeAt(position)
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.device_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_deleting_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    t.message ?: getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}