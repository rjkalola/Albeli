package com.ecommerce.albeliapp.dashboard.data.reposotory

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressInfo
import com.ecommerce.albeliapp.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.model.ProductDetailsResponse
import okhttp3.RequestBody
import retrofit2.http.GET
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
}