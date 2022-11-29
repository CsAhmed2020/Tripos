package com.example.tripso.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripso.domain.repository.FirebaseRepository
import com.example.tripso.domain.util.DataStoreUtils
import com.example.tripso.domain.util.PrefsConstants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
):ViewModel() {


    var userName = MutableLiveData(DataStoreUtils.readPreference(PrefsConstants.MY_USER_NAME,
        FirebaseAuth.getInstance().currentUser!!.displayName.toString()
    ))
    var userEmail = MutableLiveData(DataStoreUtils.readPreference(PrefsConstants.MY_USER_EMAIL,
        FirebaseAuth.getInstance().currentUser!!.email.toString()
    ))
    var userPhone = MutableLiveData(DataStoreUtils.readPreference(PrefsConstants.MY_USER_PHONE,
        FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()))


    var saved =MutableLiveData<Boolean>()


    fun updateProfileData(){
        viewModelScope.launch {
            firebaseRepository.saveUserInDB(name = userName.value.toString() , phone = userPhone.value.toString())
        }
        updateSaveState(true)
    }

    fun updateSaveState(state:Boolean){
        saved.postValue(state)
    }

}