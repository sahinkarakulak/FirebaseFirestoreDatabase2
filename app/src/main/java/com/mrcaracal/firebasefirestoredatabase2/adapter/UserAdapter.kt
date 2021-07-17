package com.mrcaracal.firebasefirestoredatabase2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrcaracal.firebasefirestoredatabase2.databinding.RowDesignBinding
import com.mrcaracal.firebasefirestoredatabase2.model.UserData

class UserAdapter(private val userList: ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    class UserHolder(val binding: RowDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = RowDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.binding.rowName.text = userList.get(position).name
        holder.binding.rowSurname.text = userList.get(position).surname
        holder.binding.rowAge.text = userList.get(position).age
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}