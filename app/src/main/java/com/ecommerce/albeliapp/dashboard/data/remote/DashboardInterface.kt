package com.ecommerce.albeliapp.dashboard.data.remote

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import okhttp3.RequestBody
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
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

}

