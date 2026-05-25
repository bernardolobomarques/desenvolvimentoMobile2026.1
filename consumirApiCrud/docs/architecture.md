# EnergyMonitor App - Arquitetura (Mobile)

## Estrutura de arquivos (onde fica o que)

```
app/src/main/java/com/example/consumirapicrud/
  MainActivity.kt
  DeviceFormActivity.kt
  DeviceAdapter.kt
  api/
    Device.kt
    DeviceApi.kt
    RetrofitClient.kt

app/src/main/res/layout/
  activity_main.xml
  activity_device_form.xml
  item_device.xml
```

## Fluxo visual (quem chama o que)

```
MainActivity
  -> RetrofitClient.deviceApi.getDevices()  [GET /devices/]
  -> DeviceAdapter (RecyclerView)
  -> abre DeviceFormActivity
  -> deleteDevice() [DELETE /devices/{id}]

DeviceFormActivity
  -> getDevice(id)  [GET /devices/{id}] (modo edicao)
  -> createDevice() [POST /devices/]
  -> updateDevice() [PUT /devices/{id}]

DeviceAdapter
  -> renderiza item_device.xml
  -> dispara clique (editar) e delete
```

## Mermaid (diagrama)

Veja [docs/architecture.mmd](architecture.mmd).

## Explicacao resumida com destaques visuais

- **Camada UI**
  - **MainActivity**: lista dispositivos, dispara GET no `onResume()`, abre formulario e executa delete.
  - **DeviceFormActivity**: cria/edita dispositivo (POST/PUT) e carrega dados se tiver `id` (GET).
  - **DeviceAdapter**: conecta dados no `RecyclerView` e injeta o botao de delete do item.

- **Camada de Rede**
  - **RetrofitClient**: configura base URL e cria o `DeviceApi`.
  - **DeviceApi**: define contratos HTTP (GET/POST/PUT/DELETE).
  - **Device**: modelo com `id`, `name`, `type`, `is_active`.

- **Arquivos de Layout**
  - `activity_main.xml`: lista + FAB.
  - `activity_device_form.xml`: formulario.
  - `item_device.xml`: card do item com switch e lixeira.
