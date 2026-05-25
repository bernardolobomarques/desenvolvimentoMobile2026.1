package com.example.consumirapicrud

import com.example.consumirapicrud.api.Device
import com.example.consumirapicrud.api.DeviceApi
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeviceApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: DeviceApi
    private val gson = Gson()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(DeviceApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getDevices_parsesList() {
        val responseBody = """
            [
              {"id":1,"name":"Sensor A","type":"meter","is_active":true},
              {"id":2,"name":"Sensor B","type":"switch","is_active":false}
            ]
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val response = api.getDevices().execute()

        assertTrue(response.isSuccessful)
        val devices = response.body() ?: emptyList()
        assertEquals(2, devices.size)
        assertEquals(1, devices[0].id)
        assertEquals("Sensor A", devices[0].name)
        assertEquals("meter", devices[0].type)
        assertTrue(devices[0].isActive)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/devices/", request.path)
    }

    @Test
    fun getDevice_parsesItem() {
        val responseBody = """
            {"id":10,"name":"Gateway","type":"hub","is_active":true}
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val response = api.getDevice(10).execute()

        assertTrue(response.isSuccessful)
        val device = response.body()
        assertEquals(10, device?.id)
        assertEquals("Gateway", device?.name)
        assertEquals("hub", device?.type)
        assertTrue(device?.isActive == true)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/devices/10", request.path)
    }

    @Test
    fun createDevice_sendsPostAndParses() {
        val responseBody = """
            {"id":3,"name":"Lamp","type":"light","is_active":false}
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setResponseCode(201).setBody(responseBody))

        val payload = Device(name = "Lamp", type = "light", isActive = false)
        val response = api.createDevice(payload).execute()

        assertTrue(response.isSuccessful)
        val device = response.body()
        assertEquals(3, device?.id)
        assertEquals("Lamp", device?.name)
        assertEquals("light", device?.type)
        assertTrue(device?.isActive == false)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/devices/", request.path)
        val bodyJson = request.body.readUtf8()
        val sentDevice = gson.fromJson(bodyJson, Device::class.java)
        assertEquals("Lamp", sentDevice.name)
        assertEquals("light", sentDevice.type)
        assertEquals(false, sentDevice.isActive)
    }

    @Test
    fun updateDevice_sendsPutAndParses() {
        val responseBody = """
            {"id":4,"name":"Panel","type":"solar","is_active":true}
        """.trimIndent()
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val payload = Device(name = "Panel", type = "solar", isActive = true)
        val response = api.updateDevice(4, payload).execute()

        assertTrue(response.isSuccessful)
        val device = response.body()
        assertEquals(4, device?.id)
        assertEquals("Panel", device?.name)
        assertEquals("solar", device?.type)
        assertTrue(device?.isActive == true)

        val request = mockWebServer.takeRequest()
        assertEquals("PUT", request.method)
        assertEquals("/devices/4", request.path)
    }

    @Test
    fun deleteDevice_sendsDelete() {
        mockWebServer.enqueue(MockResponse().setResponseCode(204))

        val response = api.deleteDevice(7).execute()

        assertTrue(response.isSuccessful)
        val request = mockWebServer.takeRequest()
        assertEquals("DELETE", request.method)
        assertEquals("/devices/7", request.path)
    }
}
