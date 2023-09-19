package com.ecommerce.albeliapp.dashboard.data.reposotory

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import okhttp3.RequestBody

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
}