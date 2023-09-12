package com.ecommerce.albeliapp.authentication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecommerce.albeliapp.authentication.data.model.*
import com.ecommerce.albeliapp.authentication.data.repository.AuthenticationRepository

class AuthenticationViewModel(val authenticationRepository: AuthenticationRepository) :
    ViewModel() {
    val loginResponse = MutableLiveData<UserResponse>()



}