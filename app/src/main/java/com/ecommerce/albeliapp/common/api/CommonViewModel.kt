package com.ecommerce.albeliapp.common.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.albeliapp.common.api.model.RegisterConfigurationResponse
import com.ecommerce.albeliapp.common.utils.traceErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.util.concurrent.CancellationException

class CommonViewModel(val commonRepository: CommonRepository) :
    ViewModel() {
    val registerConfigurationResponse = MutableLiveData<RegisterConfigurationResponse>()

    fun companyResource() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = commonRepository.companyResource()
                withContext(Dispatchers.Main) {
                    registerConfigurationResponse.value = response
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