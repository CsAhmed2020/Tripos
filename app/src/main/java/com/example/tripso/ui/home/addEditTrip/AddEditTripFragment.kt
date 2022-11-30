package com.example.tripso.ui.home.addEditTrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tripso.R
import com.example.tripso.databinding.FragmentAddEditTripBinding
import com.example.tripso.domain.util.Utils.convertLongToTime
import com.example.tripso.domain.util.setAlarm
import com.example.tripso.ui.home.HomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar


class AddEditTripFragment : Fragment() {

    private var _binding : FragmentAddEditTripBinding ? = null

    val binding get() = _binding!!

    private val homeViewModel : HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTripBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        binding.tripDate.setOnClickListener {
            showDataRangePicker()
        }

        binding.saveTrip.setOnClickListener {
            findNavController().popBackStack()
        }

        homeViewModel.saved.observe(viewLifecycleOwner){
            if (it){
                Snackbar.make(this.requireView(),getString(R.string.saved),Snackbar.LENGTH_SHORT).show()
                this.requireContext().setAlarm(homeViewModel.tripStart.value!!.toLong(),
                title = getString(R.string.trip_ready), description = homeViewModel.tripName.value.toString())
                this.requireContext().setAlarm(homeViewModel.tripEnd.value!!.toLong(),
                    title = getString(R.string.trip_to_end), description = homeViewModel.tripName.value.toString())
                homeViewModel.updateSaveState(false)
            }
        }

    }

    private fun showDataRangePicker(){
        val dateRangePicker =
            MaterialDatePicker
                .Builder.dateRangePicker()
                .setTitleText("Select Date")
                .build()

        dateRangePicker.show(
            parentFragmentManager,
            "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->
            homeViewModel.updateDate(dateSelected.first.toString(),dateSelected.second.toString())
            binding.tripDate.setText("${convertLongToTime(dateSelected.first)} to ${convertLongToTime(dateSelected.second)}")
        }

    }


}