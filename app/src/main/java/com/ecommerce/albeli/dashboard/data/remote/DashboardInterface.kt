package com.ecommerce.albeli.dashboard.data.remote

import com.ecommerce.albeli.common.api.model.BaseResponse
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
import com.ecommerce.albeli.dashboard.data.model.ReviewListResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface DashboardInterface {
    @GET("dashboard")
    suspend fun getDashboard(): DashboardResponse

    @Multipart
    @POST("product")
    suspend fun getCategoryProducts(
        @Part("category_id") category_id: RequestBody,
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
    ): CategoryProductsResponse

    @Multipart
    @POST("product")
    suspend fun getCategoryProducts(
        @Part("category_id") category_id: RequestBody,
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
        @Part("filter_ids") filter_ids: RequestBody,
        @Part("min_amount") min_amount: RequestBody,
        @Part("max_amount") max_amount: RequestBody,
    ): CategoryProductsResponse

    @Multipart
    @POST("search-product")
    suspend fun searchProducts(
        @Part("search") search: RequestBody,
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
    ): CategoryProductsResponse

    @GET("category")
    suspend fun getCategoryList(): CategoryResponse

    @Multipart
    @POST("store-product-in-wishlist")
    suspend fun addProductToWishList(
        @Part("product_id") product_id: RequestBody
    ): BaseResponse

    @Multipart
    @POST("remove-product-from-wishlist")
    suspend fun removeProductFromWishList(
        @Part("product_id") product_id: RequestBody
    ): BaseResponse

    @Multipart
    @POST("product-detail")
    suspend fun getProductDetails(
        @Part("product_id") product_id: RequestBody
    ): ProductDetailsResponse

    @FormUrlEncoded
    @POST("add-product-to-cart")
    suspend fun addProductToCart(
        @FieldMap options: HashMap<String, String>
    ): BaseResponse

    @Multipart
    @POST("wishlist-products")
    suspend fun getWishlistProducts(
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
    ): CategoryProductsResponse

    @GET("get-cart")
    suspend fun getCartList(): CategoryProductsResponse

    @Multipart
    @POST("update-product-to-cart")
    suspend fun updateProductToCart(
        @Part("id") id: RequestBody,
        @Part("qty") qty: RequestBody,
    ): BaseResponse

    @Multipart
    @POST("remove-product-from-cart")
    suspend fun removeProductFromCart(
        @Part("id") id: RequestBody,
    ): BaseResponse

    @GET("address-data")
    suspend fun getAddressResources(): AddressResourcesResponse

    @POST("store-address")
    suspend fun addAddress(@Body addressRequest: AddressInfo): BaseResponse

    @Multipart
    @POST("make-default-address")
    suspend fun makeDefaultAddress(
        @Part("id") id: RequestBody,
    ): BaseResponse

    @GET("addresses")
    suspend fun getAddressList(): AddressResponse

    @GET("my-profile")
    suspend fun getMyProfile(): MyProfileResponse

    @Multipart
    @POST("store-profile")
    suspend fun storeMyProfile(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
    ): BaseResponse

    @Multipart
    @POST("notifications")
    suspend fun getNotificationList(
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
    ): NotificationResponse

    @GET("logout")
    suspend fun logout(): BaseResponse

    @FormUrlEncoded
    @POST("place-order")
    suspend fun placeOrder(
        @FieldMap options: HashMap<String, String>
    ): BaseResponse

    @Multipart
    @POST("my-orders")
    suspend fun getMyOrders(
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
    ): OrdersResponse

    @Multipart
    @POST("order-detail")
    suspend fun orderDetails(
        @Part("id") id: RequestBody,
    ): OrderDetailsResponse

    @Multipart
    @POST("add-device-token")
    suspend fun addDeviceToken(
        @Part("token") token: RequestBody,
        @Part("device_type") device_type: RequestBody,
    ): BaseResponse

    @Multipart
    @POST("product-review")
    suspend fun productReview(
        @Part("product_id") product_id: RequestBody,
        @Part("limit") limit: RequestBody,
        @Part("offset") offset: RequestBody,
    ): ReviewListResponse

    @Multipart
    @POST("store-product-review")
    suspend fun storeProductReview(
        @Part("product_id") product_id: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("reviewer_name") reviewer_name: RequestBody,
        @Part("comment") comment: RequestBody,
    ): BaseResponse

    @Multipart
    @POST("verify-coupon-code")
    suspend fun verifyCouponCode(
        @Part("coupon_code") coupon_code: RequestBody,
    ): CouponCodeResponse

}

