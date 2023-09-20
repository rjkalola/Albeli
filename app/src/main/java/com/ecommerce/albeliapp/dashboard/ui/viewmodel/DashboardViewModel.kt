package com.ecommerce.albeliapp.dashboard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.common.utils.AppUtils
import com.ecommerce.albeliapp.common.utils.traceErrorException
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.model.ProductDetailsResponse
import com.ecommerce.albeliapp.dashboard.data.reposotory.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import org.json.JSONException
import java.util.concurrent.CancellationException

class DashboardViewModel(private val dashboardRepository: DashboardRepository) :
    ViewModel() {
    val dashboardResponse = MutableLiveData<DashboardResponse>()
    val categoryProductsResponse = MutableLiveData<CategoryProductsResponse>()
    val categoryResponse = MutableLiveData<CategoryResponse>()
    val baseResponse = MutableLiveData<BaseResponse>()
    val productDetailsResponse = MutableLiveData<ProductDetailsResponse>()

    fun getDashboardResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getDashboard()
                withContext(Dispatchers.Main) {
                    dashboardResponse.value = response
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

    fun getCategoryProductsResponse(categoryId: String, limit: Int, offset: Int) {
        val categoryIdBody: RequestBody = AppUtils.getRequestBody(categoryId.toString())
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getCategoryProducts(categoryIdBody, limitBody, offsetBody)
                withContext(Dispatchers.Main) {
                    categoryProductsResponse.value = response
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

    fun getCategoryProductsResponse(
        categoryId: String,
        limit: Int,
        offset: Int,
        filterIds: String,
        minAmount: String,
        maxAmount: String
    ) {
        val categoryIdBody: RequestBody = AppUtils.getRequestBody(categoryId.toString())
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())
        val filterIdsBody: RequestBody = AppUtils.getRequestBody(filterIds)
        val minAmountBody: RequestBody = AppUtils.getRequestBody(minAmount)
        val maxAmountBody: RequestBody = AppUtils.getRequestBody(maxAmount)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getCategoryProducts(
                        categoryIdBody,
                        limitBody,
                        offsetBody,
                        filterIdsBody,
                        minAmountBody,
                        maxAmountBody
                    )
                withContext(Dispatchers.Main) {
                    categoryProductsResponse.value = response
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

    fun searchProductsResponse(search: String, limit: Int, offset: Int) {
        val searchBody: RequestBody = AppUtils.getRequestBody(search)
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.searchProducts(searchBody, limitBody, offsetBody)
                withContext(Dispatchers.Main) {
                    categoryProductsResponse.value = response
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

    fun getCategoryListResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getCategoryList()
                withContext(Dispatchers.Main) {
                    categoryResponse.value = response
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

    fun addProductToWatchListResponse(productId: String) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.addProductToWishList(productIdBody)
                withContext(Dispatchers.Main) {
                    baseResponse.value = response
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

    fun removeProductToWatchListResponse(productId: String) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.removeProductFromWishList(productIdBody)
                withContext(Dispatchers.Main) {
                    baseResponse.value = response
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                traceErrorException(e)
            } catch (e: CancellationException) {
                e.printStackTrace()
                traceErrorException(e)
            } catch (e: Exception) {
                e.printStackTrace()
                traceErrorException(e)
            }
        }
    }

    fun getProductDetailsResponse(productId: String) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getProductDetails(productIdBody)
                withContext(Dispatchers.Main) {
                    productDetailsResponse.value = response
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                traceErrorException(e)
            } catch (e: CancellationException) {
                e.printStackTrace()
                traceErrorException(e)
            } catch (e: Exception) {
                e.printStackTrace()
                traceErrorException(e)
            }
        }
    }
}