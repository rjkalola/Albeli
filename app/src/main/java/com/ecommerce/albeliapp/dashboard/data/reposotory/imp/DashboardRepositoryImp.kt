package com.ecommerce.albeliapp.dashboard.data.reposotory.imp

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.model.ProductDetailsResponse
import com.ecommerce.albeliapp.dashboard.data.remote.DashboardInterface
import com.ecommerce.albeliapp.dashboard.data.reposotory.DashboardRepository
import okhttp3.RequestBody

class DashboardRepositoryImp(
    private val dashboardInterface: DashboardInterface
) : DashboardRepository {
    override suspend fun getDashboard(): DashboardResponse {
        return dashboardInterface.getDashboard()
    }

    override suspend fun getCategoryProducts(
        category_id: RequestBody,
        limit: RequestBody,
        offset: RequestBody
    ): CategoryProductsResponse {
        return dashboardInterface.getCategoryProducts(category_id, limit, offset)
    }

    override suspend fun getCategoryProducts(
        category_id: RequestBody,
        limit: RequestBody,
        offset: RequestBody,
        filter_ids: RequestBody,
        min_amount: RequestBody,
        max_amount: RequestBody
    ): CategoryProductsResponse {
        return dashboardInterface.getCategoryProducts(
            category_id,
            limit,
            offset,
            filter_ids,
            min_amount,
            max_amount
        )
    }

    override suspend fun searchProducts(
        search: RequestBody,
        limit: RequestBody,
        offset: RequestBody
    ): CategoryProductsResponse {
        return dashboardInterface.searchProducts(search, limit, offset)
    }

    override suspend fun getCategoryList(): CategoryResponse {
        return dashboardInterface.getCategoryList()
    }

    override suspend fun addProductToWishList(product_id: RequestBody): BaseResponse {
        return dashboardInterface.addProductToWishList(product_id)
    }

    override suspend fun removeProductFromWishList(product_id: RequestBody): BaseResponse {
        return dashboardInterface.removeProductFromWishList(product_id)
    }

    override suspend fun getProductDetails(product_id: RequestBody): ProductDetailsResponse {
        return dashboardInterface.getProductDetails(product_id)
    }
}
