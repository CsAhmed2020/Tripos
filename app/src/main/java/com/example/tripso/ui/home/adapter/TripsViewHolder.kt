package com.example.tripso.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.tripso.databinding.TripItemBinding
import com.example.tripso.domain.model.Trip

class TripsViewHolder(
    private val binding : TripItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(trip: Trip){
        binding.trip = trip
    }
}