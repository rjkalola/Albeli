package com.ecommerce.albeliapp.dashboard.data.remote

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressInfo
import com.ecommerce.albeliapp.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.model.ProductDetailsResponse
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

}

