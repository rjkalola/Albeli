package com.ecommerce.albeliapp.common.api


import com.ecommerce.albeliapp.common.utils.CoroutineCallAdapterFactory
import com.ecommerce.albeliapp.common.utils.VariantConfig
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

var commonModule = module {
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(VariantConfig.serverBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create<CommonInterface>()
    }

    single<CommonRepository> { CommonRepositoryImp(commonInterface = get()) }

    viewModel { CommonViewModel(commonRepository = get()) }
}