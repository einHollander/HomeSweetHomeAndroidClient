package bedbrains.homesweethomeandroidclient

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bedbrains.homesweethomeandroidclient.rest.*
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.RuleValue
import bedbrains.shared.datatypes.update
import bedbrains.shared.datatypes.upsert

object DataRepository {
    var restClient: APIService? = buildRestClient()

    val handler = Handler(Looper.getMainLooper())
    var updateRunnable = buildUpdateRunnable(5000)

    val devices: MutableLiveData<MutableList<Device>> = MutableLiveData(mutableListOf())
    val rules: MutableLiveData<MutableList<Rule>> = MutableLiveData(mutableListOf())
    val values: MutableLiveData<MutableList<RuleValue>> = MutableLiveData(mutableListOf())

    init {
        fetchUpdates()
    }

    fun fetchUpdates(): LiveData<Resp> {
        return resultOf(fetchDevices(), fetchRules(), fetchValues())
    }


    fun fetchDevices() = RespCallback<List<Device>>().enqueue(restClient?.devices()) {
        devices.value = it?.toMutableList() ?: mutableListOf()
    }

    fun updateDevice(device: Device): LiveData<Resp> {
        devices.value = devices.value.apply { this?.update(device) { it.uid == device.uid } }
        return RespCallback<Unit>().enqueue(restClient?.putDevice(device))
    }


    fun fetchRules() = RespCallback<List<Rule>>().enqueue(restClient?.rules()) {
        rules.value = it?.toMutableList() ?: mutableListOf()
    }

    fun updateRule(rule: Rule): LiveData<Resp> {
        rules.value = rules.value.apply { this?.update(rule) { it.uid == rule.uid } }
        return RespCallback<Unit>().enqueue(restClient?.putRule(rule))
    }

    fun upsertRule(rule: Rule): LiveData<Resp> {
        rules.value = rules.value.apply { this?.upsert(rule) { it.uid == rule.uid } }
        return RespCallback<Unit>().enqueue(restClient?.postRule(rule))
    }

    fun removeRule(rule: Rule): LiveData<Resp> {
        rules.value = rules.value.apply { this?.remove(rule) }
        return RespCallback<Unit>().enqueue(restClient?.deleteRule(rule.uid))
    }


    fun fetchValues() = RespCallback<List<RuleValue>>().enqueue(restClient?.values()) {
        values.value = it?.toMutableList() ?: mutableListOf()
    }

    fun updateValue(value: RuleValue): LiveData<Resp> {
        values.value = values.value.apply { this?.update(value) { it.uid == value.uid } }
        return RespCallback<Unit>().enqueue(restClient?.putValue(value))
    }

    fun upsertValue(value: RuleValue): LiveData<Resp> {
        values.value = values.value.apply { this?.upsert(value) { it.uid == value.uid } }
        return RespCallback<Unit>().enqueue(restClient?.postValue(value))
    }

    fun removeValue(value: RuleValue): LiveData<Resp> {
        values.value = values.value.apply { this?.remove(value) }
        return RespCallback<Unit>().enqueue(restClient?.deleteValue(value.uid))
    }


    fun startAutomaticUpdate(delay: Long) {
        handler.removeCallbacks(updateRunnable)
        updateRunnable = buildUpdateRunnable(delay * 1000)
        handler.post(updateRunnable)
    }

    fun stopAutomaticUpdate() {
        handler.removeCallbacks(updateRunnable)
    }

    fun buildUpdateRunnable(delayMillis: Long) = object : Runnable {
        override fun run() {
            fetchUpdates()
            handler.postDelayed(this, delayMillis)
        }
    }

    fun buildRestClient(host: String? = null, port: String? = null): APIService? {
        val h = host ?: Res.preferences.getString(
            Res.resources.getString(R.string.pref_network_host_key),
            ""
        )

        val p = port ?: Res.preferences.getString(
            Res.resources.getString(R.string.pref_network_port_key),
            ""
        )

        return try {
            Controller.buildClient("http://${h?.trim()}:${p?.trim()}")
        } catch (e: Exception) {
            null
        }
    }
}