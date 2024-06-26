package com.ecommerce.albeli.dashboard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.albeli.common.api.model.BaseResponse
import com.ecommerce.albeli.common.utils.AppUtils
import com.ecommerce.albeli.common.utils.traceErrorException
import com.ecommerce.albeli.dashboard.data.model.AddressInfo
import com.ecommerce.albeli.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeli.dashboard.data.model.AddressResponse
import com.ecommerce.albeli.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeli.dashboard.data.model.CategoryResponse
import com.ecommerce.albeli.dashboard.data.model.CouponCodeResponse
import com.ecommerce.albeli.dashboard.data.model.DashboardResponse
import com.ecommerce.albeli.dashboard.data.model.MyProfileResponse
import com.ecommerce.albeli.dashboard.data.model.NotificationResponse
import com.ecommerce.albeli.dashboard.data.model.OrderDetailsResponse
import com.ecommerce.albeli.dashboard.data.model.OrdersResponse
import com.ecommerce.albeli.dashboard.data.model.ProductDetailsResponse
import com.ecommerce.albeli.dashboard.data.model.ProductOptionsItemInfo
import com.ecommerce.albeli.dashboard.data.model.ReviewListResponse
import com.ecommerce.albeli.dashboard.data.reposotory.DashboardRepository
import com.ecommerce.utilities.utils.StringHelper
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
    val productDetailsReviewUpdateResponse = MutableLiveData<ProductDetailsResponse>()
    val addProductToCartResponse = MutableLiveData<BaseResponse>()
    val addressResourcesResponse = MutableLiveData<AddressResourcesResponse>()
    val addAddressResponse = MutableLiveData<BaseResponse>()
    val addressResponse = MutableLiveData<AddressResponse>()
    val makeDefaultAddress = MutableLiveData<BaseResponse>()
    val myProfileResponse = MutableLiveData<MyProfileResponse>()
    val mNotificationResponse = MutableLiveData<NotificationResponse>()
    val mLogoutResponse = MutableLiveData<BaseResponse>()
    val mPlaceOrderResponse = MutableLiveData<BaseResponse>()
    val mMyOrdersResponse = MutableLiveData<OrdersResponse>()
    val mOrderDetailsResponse = MutableLiveData<OrderDetailsResponse>()
    val mAddDeviceTokenResponse = MutableLiveData<BaseResponse>()
    val mStoreReviewResponse = MutableLiveData<BaseResponse>()
    val mGetReviewListResponse = MutableLiveData<ReviewListResponse>()
    val mCouponCodeResponse = MutableLiveData<CouponCodeResponse>()

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

    fun getProductDetailsReviewUpdateResponse(productId: String) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getProductDetails(productIdBody)
                withContext(Dispatchers.Main) {
                    productDetailsReviewUpdateResponse.value = response
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

    fun addProductToCartResponse(
        productId: String,
        quantity: Int,
        options: MutableList<ProductOptionsItemInfo>
    ) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId)
        val quantityBody: RequestBody = AppUtils.getRequestBody(quantity.toString())
        val partMap: HashMap<String, String> = HashMap()
        partMap["product_id"] = productId
        partMap["quantity"] = quantity.toString()
        if (options.isNotEmpty()) {
            for (i in 0 until options.size) {
////                partMap["options[$i][id]"] = AppUtils.getRequestBody(options[i].id.toString())
////                partMap["options[$i][value]"] = AppUtils.getRequestBody(options[i].label)
////                if (!StringHelper.isEmpty(options[i].price))
////                    partMap["options[$i][price]"] = AppUtils.getRequestBody(options[i].price.toString())
////                else
////                    partMap["options[$i][price]"] = AppUtils.getRequestBody("0")

                partMap["options[$i][id]"] = options[i].option_id.toString()
                partMap["options[$i][value]"] = options[i].id.toString()
                if (!StringHelper.isEmpty(options[i].price))
                    partMap["options[$i][price]"] = options[i].price.toString()
                else
                    partMap["options[$i][price]"] = "0"
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.addProductToCart(partMap)
                withContext(Dispatchers.Main) {
                    addProductToCartResponse.value = response
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

    fun getWishlistProductsResponse(limit: Int, offset: Int) {
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getWishlistProducts(limitBody, offsetBody)
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

    fun getCartListResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getCartList()
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

    fun updateProductToCartResponse(id: Int, qty: Int) {
        val idBody: RequestBody = AppUtils.getRequestBody(id.toString())
        val qtyBody: RequestBody = AppUtils.getRequestBody(qty.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.updateProductToCart(idBody, qtyBody)
                withContext(Dispatchers.Main) {
                    addProductToCartResponse.value = response
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

    fun removeProductFromCartResponse(id: Int) {
        val idBody: RequestBody = AppUtils.getRequestBody(id.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.removeProductFromCart(idBody)
                withContext(Dispatchers.Main) {
                    addProductToCartResponse.value = response
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

    fun getAddressResourcesResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getAddressResources()
                withContext(Dispatchers.Main) {
                    addressResourcesResponse.value = response
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

    fun addAddressResponse(addressRequest: AddressInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.addAddress(addressRequest)
                withContext(Dispatchers.Main) {
                    addAddressResponse.value = response
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

    fun getAddressListResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getAddressList()
                withContext(Dispatchers.Main) {
                    addressResponse.value = response
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

    fun makeDefaultAddress(id: Int) {
        val idBody: RequestBody = AppUtils.getRequestBody(id.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.makeDefaultAddress(idBody)
                withContext(Dispatchers.Main) {
                    makeDefaultAddress.value = response
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

    fun getMyProfileResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getMyProfile()
                withContext(Dispatchers.Main) {
                    myProfileResponse.value = response
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

    fun storeMyProfileResponse(firstName: String, lastName: String, email: String, phone: String) {
        val firstNameBody: RequestBody = AppUtils.getRequestBody(firstName)
        val lastNameBody: RequestBody = AppUtils.getRequestBody(lastName)
        val emailBody: RequestBody = AppUtils.getRequestBody(email)
        val phoneBody: RequestBody = AppUtils.getRequestBody(phone)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.storeMyProfile(
                        firstNameBody,
                        lastNameBody,
                        emailBody,
                        phoneBody
                    )
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

    fun getNotificationList(limit: Int, offset: Int) {
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getNotificationList(limitBody, offsetBody)
                withContext(Dispatchers.Main) {
                    mNotificationResponse.value = response
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

    fun mLogoutResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.logout()
                withContext(Dispatchers.Main) {
                    mLogoutResponse.value = response
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

    fun placeOrderResponse(
        addressId: Int,
        paymentType: Int,
        paymentCode: String,
    ) {
        val partMap: HashMap<String, String> = HashMap()
        partMap["address_id"] = addressId.toString()
        partMap["payment_type"] = paymentType.toString()
        if (paymentType == 1)
            partMap["payment_success_code"] = paymentCode

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.placeOrder(partMap)
                withContext(Dispatchers.Main) {
                    mPlaceOrderResponse.value = response
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

    fun mOrderDetailsResponse(id: String) {
        val idBody: RequestBody = AppUtils.getRequestBody(id)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.orderDetails(idBody)
                withContext(Dispatchers.Main) {
                    mOrderDetailsResponse.value = response
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

    fun getMyOrdersList(limit: Int, offset: Int) {
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.getMyOrders(limitBody, offsetBody)
                withContext(Dispatchers.Main) {
                    mMyOrdersResponse.value = response
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

    fun addDeviceToken(token: String) {
        val tokenBody: RequestBody = AppUtils.getRequestBody(token)
        val deviceTypeBody: RequestBody = AppUtils.getRequestBody("1")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.addDeviceToken(tokenBody, deviceTypeBody)
                withContext(Dispatchers.Main) {
                    mAddDeviceTokenResponse.value = response
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

    fun getReviewsList(productId: Int, limit: Int, offset: Int) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId.toString())
        val limitBody: RequestBody = AppUtils.getRequestBody(limit.toString())
        val offsetBody: RequestBody = AppUtils.getRequestBody(offset.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.productReview(productIdBody, limitBody, offsetBody)
                withContext(Dispatchers.Main) {
                    mGetReviewListResponse.value = response
                }
            } catch (e: JSONException) {
                traceErrorException(e)
                e.printStackTrace()
            } catch (e: CancellationException) {
                traceErrorException(e)
                e.printStackTrace()
            } catch (e: Exception) {
                traceErrorException(e)
                e.printStackTrace()
            }
        }
    }

    fun storeReview(productId: Int, rating: Int, reviewerName: String, comment: String) {
        val productIdBody: RequestBody = AppUtils.getRequestBody(productId.toString())
        val ratingBody: RequestBody = AppUtils.getRequestBody(rating.toString())
        val reviewerNameBody: RequestBody = AppUtils.getRequestBody(reviewerName)
        val commentBody: RequestBody = AppUtils.getRequestBody(comment)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.storeProductReview(
                        productIdBody,
                        ratingBody,
                        reviewerNameBody,
                        commentBody
                    )
                withContext(Dispatchers.Main) {
                    mStoreReviewResponse.value = response
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

    fun verifyCouponCode(couponCode: String) {
        val couponCodeBody: RequestBody = AppUtils.getRequestBody(couponCode)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    dashboardRepository.verifyCouponCode(
                        couponCodeBody
                    )
                withContext(Dispatchers.Main) {
                    mCouponCodeResponse.value = response
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