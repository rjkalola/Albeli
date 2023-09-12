package com.ecommerce.albeliapp.dashboard.di


import com.ecommerce.albeliapp.common.utils.CoroutineCallAdapterFactory
import com.ecommerce.albeliapp.common.utils.VariantConfig
import com.ecommerce.albeliapp.dashboard.data.remote.DashboardInterface
import com.ecommerce.albeliapp.dashboard.data.reposotory.DashboardRepository
import com.ecommerce.albeliapp.dashboard.data.reposotory.imp.DashboardRepositoryImp
import com.ecommerce.albeliapp.dashboard.ui.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

var dashboardModule = module {
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(VariantConfig.serverBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create<DashboardInterface>()
    }

    single<DashboardRepository> { DashboardRepositoryImp(dashboardInterface = get()) }

    viewModel { DashboardViewModel(dashboardRepository = get()) }
}