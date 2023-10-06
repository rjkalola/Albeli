package com.ecommerce.albeliapp.dashboard.data.reposotory

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressInfo
import com.ecommerce.albeliapp.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.model.MyProfileResponse
import com.ecommerce.albeliapp.dashboard.data.model.NotificationResponse
import com.ecommerce.albeliapp.dashboard.data.model.OrderDetailsResponse
import com.ecommerce.albeliapp.dashboard.data.model.OrdersResponse
import com.ecommerce.albeliapp.dashboard.data.model.ProductDetailsResponse
import com.ecommerce.albeliapp.dashboard.data.model.ReviewListResponse
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DashboardRepository {
    suspend fun getDashboard(): DashboardResponse

    suspend fun getCategoryProducts(
        category_id: RequestBody,
        limit: RequestBody,
        offset: RequestBody,
    ): CategoryProductsResponse


    suspend fun getCategoryProducts(
        category_id: RequestBody,
        limit: RequestBody,
        offset: RequestBody,
        filter_ids: RequestBody,
        min_amount: RequestBody,
        max_amount: RequestBody,
    ): CategoryProductsResponse

    suspend fun searchProducts(
        search: RequestBody,
        limit: RequestBody,
        offset: RequestBody,
    ): CategoryProductsResponse

    suspend fun getCategoryList(): CategoryResponse

    suspend fun addProductToWishList(
        product_id: RequestBody
    ): BaseResponse

    suspend fun removeProductFromWishList(
        product_id: RequestBody
    ): BaseResponse

    suspend fun getProductDetails(
        product_id: RequestBody
    ): ProductDetailsResponse

    suspend fun addProductToCart(
        options: HashMap<String, String>
    ): BaseResponse

    suspend fun getWishlistProducts(
        limit: RequestBody,
        offset: RequestBody,
    ): CategoryProductsResponse

    suspend fun getCartList(): CategoryProductsResponse

    suspend fun updateProductToCart(
        id: RequestBody,
        qty: RequestBody,
    ): BaseResponse


    suspend fun removeProductFromCart(
        id: RequestBody,
    ): BaseResponse

    suspend fun getAddressResources(): AddressResourcesResponse

    suspend fun addAddress(addressRequest: AddressInfo): BaseResponse


    suspend fun makeDefaultAddress(
        id: RequestBody,
    ): BaseResponse


    suspend fun getAddressList(): AddressResponse

    suspend fun getMyProfile(): MyProfileResponse


    suspend fun storeMyProfile(
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        phone: RequestBody,
    ): BaseResponse


    suspend fun getNotificationList(
        limit: RequestBody,
        offset: RequestBody,
    ): NotificationResponse


    suspend fun logout(): BaseResponse


    suspend fun placeOrder(
        options: HashMap<String, String>
    ): BaseResponse

    suspend fun getMyOrders(
        limit: RequestBody,
        offset: RequestBody,
    ): OrdersResponse

    suspend fun orderDetails(
        id: RequestBody,
    ): OrderDetailsResponse

    suspend fun addDeviceToken(
        token: RequestBody,
        device_type: RequestBody,
    ): BaseResponse

    suspend fun productReview(
        product_id: RequestBody,
        limit: RequestBody,
        offset: RequestBody,
    ): ReviewListResponse

    suspend fun storeProductReview(
        product_id: RequestBody,
        rating: RequestBody,
        reviewer_name: RequestBody,
        comment: RequestBody,
    ): BaseResponse
}