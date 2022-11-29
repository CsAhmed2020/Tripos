package com.example.tripso.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.model.Trip
import com.example.tripso.domain.repository.FirebaseRepository
import com.example.tripso.domain.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
):ViewModel() {

    init {
        getTrips()
    }

    var tripName = MutableLiveData<String>()
    var tripFrom = MutableLiveData<String>()
    var tripTo = MutableLiveData<String>()
    var tripStart = MutableLiveData<String>()
    var tripEnd = MutableLiveData<String>()

    private var _trips = MutableLiveData<List<Trip>?>()
    val trips : MutableLiveData<List<Trip>?> = _trips

    private var _upcomingTripsState =  MutableLiveData<DataStateResult<Any>>()
    val upcomingTripsState : MutableLiveData<DataStateResult<Any>> = _upcomingTripsState

     var saved =MutableLiveData<Boolean>()
    private fun getTrips(){
        viewModelScope.launch {
            firebaseRepository.getTrips(Constants.REFERENCE_UPCOMING).collect{
                when(it){
                    is DataStateResult.Error -> {
                        _upcomingTripsState.postValue(DataStateResult.Error())
                    }
                    is DataStateResult.Loading -> {
                        _upcomingTripsState.postValue(DataStateResult.Loading())
                    }
                    is DataStateResult.Success ->{
                        _trips.postValue(it.data)
                        _upcomingTripsState.postValue(DataStateResult.Success())
                    }
                }
            }
        }
    }

    fun saveTrip(){
        viewModelScope.launch {
            firebaseRepository.addTrip(
                trip = Trip(
                    tripName = tripName.value,
                    tripFrom =  tripFrom.value,
                    tripTo = tripTo.value,
                    startDate  = tripStart.value,
                    endDate = tripEnd.value
                ),
                reference = Constants.REFERENCE_UPCOMING
            )
        }
        updateSaveState(true)
    }

    fun updateDate(startDate:String,endDate:String){
        tripStart.postValue(startDate)
        tripEnd.postValue(endDate)
    }

    fun updateSaveState(state:Boolean){
        saved.postValue(state)
    }
}