package saecki.homesweethomeandroidclient.ui.custom

import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Lamp

class LampViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)
    val state: Switch = view.findViewById(R.id.state)

    fun bindView(lamp: Lamp) {
        name.text = lamp.name
        state.isChecked = lamp.state
    }
}