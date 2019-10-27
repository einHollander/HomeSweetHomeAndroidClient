package saecki.homesweethomeandroidclient.ui.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Device
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.datatypes.devices.Lamp

class DeviceListAdapter(var devices: List<Device>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            Heating.type -> {
                val heatingView = inflater.inflate(R.layout.device_heating, parent, false)
                return HeatingViewHolder(heatingView)
            }
            Lamp.type -> {
                val lampView = inflater.inflate(R.layout.device_lamp, parent, false)
                return LampViewHolder(lampView)
            }
            else -> {
                val dummyView = inflater.inflate(R.layout.dummy, parent, false)
                return DummyViewHolder(dummyView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return devices.get(position).type
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val device = devices.get(position)

        when (device.type) {
            Heating.type -> {
                val heatingViewHolder = holder as HeatingViewHolder
                val heating = device as Heating
                heatingViewHolder.bindView(heating)
            }
            Lamp.type -> {
                val lampViewHolder = holder as LampViewHolder
                val lamp = device as Lamp
                lampViewHolder.bindView(lamp)
            }
        }
    }

}