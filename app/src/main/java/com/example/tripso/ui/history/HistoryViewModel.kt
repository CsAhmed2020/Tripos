package com.example.tripso.ui.history


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
class HistoryViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
):ViewModel() {

    init {
        getTrips()
    }

    private var _trips = MutableLiveData<List<Trip>?>()
    val trips : MutableLiveData<List<Trip>?> = _trips

    private var _lastTripsState =  MutableLiveData<DataStateResult<Any>>()
    val lastTripsState : MutableLiveData<DataStateResult<Any>> = _lastTripsState

    private fun getTrips(){
        viewModelScope.launch {
            firebaseRepository.getTrips(Constants.REFERENCE_LAST).collect{
                when(it){
                    is DataStateResult.Error -> {
                        _lastTripsState.postValue(DataStateResult.Error())
                    }
                    is DataStateResult.Loading -> {
                        _lastTripsState.postValue(DataStateResult.Loading())
                    }
                    is DataStateResult.Success ->{
                        _trips.postValue(it.data)
                        _lastTripsState.postValue(DataStateResult.Success())
                    }
                }
            }
        }
    }
}