package com.example.consumirapicrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.consumirapicrud.api.Device
import com.example.consumirapicrud.api.RetrofitClient
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceFormActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editType: EditText
    private lateinit var switchActive: SwitchMaterial
    private lateinit var buttonSave: Button

    private var deviceId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_form)

        editName = findViewById(R.id.editName)
        editType = findViewById(R.id.editType)
        switchActive = findViewById(R.id.switchActiveForm)
        buttonSave = findViewById(R.id.buttonSave)

        deviceId = intent.getIntExtra(EXTRA_DEVICE_ID, -1).takeIf { it != -1 }

        if (deviceId != null) {
            loadDevice(deviceId!!)
        }

        buttonSave.setOnClickListener {
            val name = editName.text.toString().trim()
            val type = editType.text.toString().trim()
            val isActive = switchActive.isChecked

            if (name.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_required_fields), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val payload = Device(name = name, type = type, isActive = isActive)
            if (deviceId == null) {
                createDevice(payload)
            } else {
                updateDevice(deviceId!!, payload)
            }
        }
    }

    private fun loadDevice(id: Int) {
        RetrofitClient.deviceApi.getDevice(id).enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                if (response.isSuccessful) {
                    val device = response.body()
                    if (device != null) {
                        editName.setText(device.name)
                        editType.setText(device.type)
                        switchActive.isChecked = device.isActive
                    }
                } else {
                    Toast.makeText(
                        this@DeviceFormActivity,
                        getString(R.string.error_loading_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                Toast.makeText(
                    this@DeviceFormActivity,
                    t.message ?: getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun createDevice(device: Device) {
        RetrofitClient.deviceApi.createDevice(device).enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(
                        this@DeviceFormActivity,
                        getString(R.string.error_saving_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                Toast.makeText(
                    this@DeviceFormActivity,
                    t.message ?: getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateDevice(id: Int, device: Device) {
        RetrofitClient.deviceApi.updateDevice(id, device).enqueue(object : Callback<Device> {
            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(
                        this@DeviceFormActivity,
                        getString(R.string.error_saving_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                Toast.makeText(
                    this@DeviceFormActivity,
                    t.message ?: getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    companion object {
        const val EXTRA_DEVICE_ID = "device_id"
    }
}
