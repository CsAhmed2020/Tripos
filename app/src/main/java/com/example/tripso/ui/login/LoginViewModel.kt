package com.example.tripso.ui.login


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.repository.FirebaseRepository
import com.example.tripso.domain.util.Constants
import com.example.tripso.domain.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
    ) : ViewModel() {


    private val _dataStateResult = MutableLiveData<DataStateResult<Any>>()
    val dataStateResult:MutableLiveData<DataStateResult<Any>> = _dataStateResult

    private val _errorMessageResId = MutableLiveData<Int>()
    val errorMessageResId:MutableLiveData<Int> = _errorMessageResId

    val userName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val userPhone = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()


    fun signUp() {
        viewModelScope.launch {
            if (Utils.validateSignUp(
                    name = userName.value.toString(), email = userEmail.value.toString(),
                    phone = userPhone.value.toString(), password = userPassword.value.toString()
                )
            ) {
                _dataStateResult.postValue(DataStateResult.Loading())
                val data = firebaseRepository.signUp(
                    userName = userName.value.toString(), email = userEmail.value.toString(),
                    phone = userPhone.value.toString(), password = userPassword.value.toString()
                )
                when (data) {
                    is DataStateResult.Error -> {
                        Log.d("AhmedVM", "ERROR: $data")
                        _errorMessageResId.postValue(data.errorMessageResId)
                        _dataStateResult.postValue(DataStateResult.Error())
                    }
                    is DataStateResult.Success -> {
                        Log.d("AhmedVM", "Success")
                        _dataStateResult.postValue(DataStateResult.Success())
                    }
                    else -> {}
                }
            }
        }
    }

    fun logIn() {
        viewModelScope.launch {
            if (Utils.validateLogIn(
                    email = userEmail.value.toString(),
                    password = userPassword.value.toString()
                ){errorMessageResId ->
                    _errorMessageResId.postValue(errorMessageResId)
                }
            ){
                _dataStateResult.postValue(DataStateResult.Loading())
                when (val data = firebaseRepository.logIn(
                    email = userEmail.value.toString(),
                    password = userPassword.value.toString()
                )) {
                    is DataStateResult.Error -> {
                        _errorMessageResId.postValue(data.errorMessageResId)
                        _dataStateResult.postValue(DataStateResult.Error())
                    }
                    is DataStateResult.Success -> {
                        Constants.LogOutTag = false
                        _dataStateResult.postValue(DataStateResult.Success())
                    }
                    else -> {}
                }
            }
        }
    }

    //not used yet
    fun resetPassword(email: String){
        viewModelScope.launch {
            _dataStateResult.postValue(DataStateResult.Loading())

            when(firebaseRepository.resetPassword(email)){
                is DataStateResult.Error -> {
                    _dataStateResult.postValue(DataStateResult.Error())
                }
                is DataStateResult.Success -> {
                    _dataStateResult.postValue(DataStateResult.Success())
                }else ->{}
            }
        }
    }
}