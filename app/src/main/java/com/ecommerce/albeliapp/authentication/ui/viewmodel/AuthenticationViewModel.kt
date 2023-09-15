package com.ecommerce.albeliapp.authentication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.albeliapp.authentication.data.model.*
import com.ecommerce.albeliapp.authentication.data.repository.AuthenticationRepository
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.common.utils.traceErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import org.json.JSONException
import java.util.concurrent.CancellationException

class AuthenticationViewModel(val authenticationRepository: AuthenticationRepository) :
    ViewModel() {
    val mUserResponse = MutableLiveData<UserResponse>()

    fun login(email: String, password: String) {
        val email: RequestBody = AppUtils.getRequestBody(email)
        val password: RequestBody = AppUtils.getRequestBody(password)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    authenticationRepository.login(email, password)
                withContext(Dispatchers.Main) {
                    mUserResponse.value = response
                }
            } catch (e: JSONException) {
                traceErrorException(e)
            } catch (e: CancellationException) {
                traceErrorException(e)
            } catch (e: Exception) {
                traceErrorException(e)
            }
        }
    }

    fun register(firstName: String, lastName: String, email: String, password: String) {
        val firstName: RequestBody = AppUtils.getRequestBody(firstName)
        val lastName: RequestBody = AppUtils.getRequestBody(lastName)
        val email: RequestBody = AppUtils.getRequestBody(email)
        val password: RequestBody = AppUtils.getRequestBody(password)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    authenticationRepository.register(firstName,lastName,email, password)
                withContext(Dispatchers.Main) {
                    mUserResponse.value = response
                }
            } catch (e: JSONException) {
                traceErrorException(e)
            } catch (e: CancellationException) {
                traceErrorException(e)
            } catch (e: Exception) {
                traceErrorException(e)
            }
        }
    }

}