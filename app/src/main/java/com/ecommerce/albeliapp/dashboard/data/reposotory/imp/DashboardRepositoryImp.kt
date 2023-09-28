package com.ecommerce.albeliapp.dashboard.data.reposotory.imp

import com.ecommerce.albeliapp.common.api.model.BaseResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressInfo
import com.ecommerce.albeliapp.dashboard.data.model.AddressResourcesResponse
import com.ecommerce.albeliapp.dashboard.data.model.AddressResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryProductsResponse
import com.ecommerce.albeliapp.dashboard.data.model.CategoryResponse
import com.ecommerce.albeliapp.dashboard.data.model.DashboardResponse
import com.ecommerce.albeliapp.dashboard.data.model.MyProfileResponse
import com.ecommerce.albeliapp.dashboard.data.model.NotificationResponse
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

    override suspend fun addProductToCart(
        options: HashMap<String, String>
    ): BaseResponse {
        return dashboardInterface.addProductToCart(options)
    }

    override suspend fun getWishlistProducts(
        limit: RequestBody,
        offset: RequestBody
    ): CategoryProductsResponse {
        return dashboardInterface.getWishlistProducts(limit, offset)
    }

    override suspend fun getCartList(): CategoryProductsResponse {
        return dashboardInterface.getCartList()
    }

    override suspend fun updateProductToCart(id: RequestBody, qty: RequestBody): BaseResponse {
        return dashboardInterface.updateProductToCart(id, qty)
    }

    override suspend fun removeProductFromCart(id: RequestBody): BaseResponse {
        return dashboardInterface.removeProductFromCart(id)
    }

    override suspend fun getAddressResources(): AddressResourcesResponse {
        return dashboardInterface.getAddressResources()
    }

    override suspend fun addAddress(addressRequest: AddressInfo): BaseResponse {
        return dashboardInterface.addAddress(addressRequest)
    }

    override suspend fun makeDefaultAddress(id: RequestBody): BaseResponse {
        return dashboardInterface.makeDefaultAddress(id)
    }

    override suspend fun getAddressList(): AddressResponse {
        return dashboardInterface.getAddressList()
    }

    override suspend fun getMyProfile(): MyProfileResponse {
        return dashboardInterface.getMyProfile()
    }

    override suspend fun storeMyProfile(
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        phone: RequestBody
    ): BaseResponse {
        return dashboardInterface.storeMyProfile(first_name, last_name, email, phone)
    }

    override suspend fun getNotificationList(
        limit: RequestBody,
        offset: RequestBody
    ): NotificationResponse {
        return dashboardInterface.getNotificationList(limit, offset)
    }

    override suspend fun logout(): BaseResponse {
        return dashboardInterface.logout()
    }

    override suspend fun placeOrder(options: HashMap<String, String>): BaseResponse {
        return dashboardInterface.placeOrder(options)
    }
}
