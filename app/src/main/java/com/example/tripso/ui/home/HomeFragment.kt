package com.example.tripso.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripso.R
import com.example.tripso.data.DataStateResult
import com.example.tripso.databinding.FragmentHomeBinding
import com.example.tripso.ui.home.adapter.TripsAdapter
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!

    private val homeViewModel : HomeViewModel by activityViewModels()

    private lateinit var adapter: TripsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tripsRecyclerView = binding.tripsRv
        tripsRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        tripsRecyclerView.setHasFixedSize(true)

        homeViewModel.upcomingTripsState.observe(viewLifecycleOwner){
            when(it){
                is DataStateResult.Error -> {
                    binding.loading.visibility = View.GONE
                    Snackbar.make(this.requireView(),R.string.error_retrieve_data, Snackbar.LENGTH_LONG).show()
                }
                is DataStateResult.Loading -> binding.loading.visibility = View.VISIBLE
                is DataStateResult.Success -> {
                    val trips = homeViewModel.trips
                    binding.loading.visibility = View.GONE
                    if (trips.value!!.isEmpty()){
                        binding.emptyListView.visibility = View.VISIBLE
                    }else{
                        adapter = TripsAdapter(trips.value!!)
                        tripsRecyclerView.adapter = adapter
                    }

                }
            }
        }
        binding.addTripFab.setOnClickListener {
           findNavController().navigate(HomeFragmentDirections.actionToAddEditTripFragment())

        }
    }

}