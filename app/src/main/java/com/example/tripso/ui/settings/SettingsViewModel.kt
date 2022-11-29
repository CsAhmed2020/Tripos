package com.example.tripso.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.repository.FirebaseRepository
import com.example.tripso.domain.util.DataStoreUtils
import com.example.tripso.domain.util.PrefsConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
):ViewModel() {

    private var _logOutState =  MutableLiveData<DataStateResult<Any>>()
    val logOutState : MutableLiveData<DataStateResult<Any>> = _logOutState


    fun logOut(){
        viewModelScope.launch {
            when(firebaseRepository.logout()){
                is DataStateResult.Error -> _logOutState.postValue(DataStateResult.Error())
                is DataStateResult.Loading -> _logOutState.postValue(DataStateResult.Loading())
                is DataStateResult.Success -> _logOutState.postValue(DataStateResult.Success())
            }
        }
    }

    fun resetPassword(){
        val userEmail = DataStoreUtils.readPreference(PrefsConstants.MY_USER_EMAIL,"")
        viewModelScope.launch {
            if (userEmail.isNotEmpty()){
                firebaseRepository.resetPassword(userEmail)
            }
        }
    }
}