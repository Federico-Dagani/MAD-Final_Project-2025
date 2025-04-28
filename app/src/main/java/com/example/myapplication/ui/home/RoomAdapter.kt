package com.example.myapplication.ui.home
import com.example.myapplication.data.RoomEntity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomAdapter(private var rooms: List<RoomEntity>) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.text.text = "${room.roomId} • ${room.type} • Floor ${room.floor}"
    }

    override fun getItemCount() = rooms.size

    fun update(newRooms: List<RoomEntity>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}
