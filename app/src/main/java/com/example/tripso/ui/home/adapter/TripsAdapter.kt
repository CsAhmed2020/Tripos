package com.example.tripso.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripso.databinding.TripItemBinding
import com.example.tripso.domain.model.Trip

class TripsAdapter(
    private val tripsList : List<Trip>
) :RecyclerView.Adapter<TripsViewHolder>(){

    private lateinit var binding: TripItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsViewHolder {
        binding = TripItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TripsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripsViewHolder, position: Int) {
        val trip = tripsList[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int = tripsList.size
}