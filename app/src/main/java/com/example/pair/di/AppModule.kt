package com.example.pair.di

import androidx.lifecycle.ViewModel
import com.example.pair.BuildConfig
import com.example.pair.MyApplication
import com.example.pair.data.api.ApiService
import com.example.pair.data.model.ExchangeResponse
import com.example.pair.data.model.ExchangeResponseDeserializer
import com.example.pair.data.repository.ExchangeRatesRepositoryImpl
import com.example.pair.domain.repository.ExchangeRatesRepository
import com.example.pair.presentation.MainViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class AppModule {

    @[Binds IntoMap VMKey(MainViewModel.ViewModel::class)]
    abstract fun bindMainViewModel(viewModel: MainViewModel): MainViewModel

    companion object {
        @Provides
        fun provideGson(): Gson = GsonBuilder()
            .registerTypeAdapter(ExchangeResponse::class.java, ExchangeResponseDeserializer())
            .create()

        @Provides
        fun provideRetrofitService(gson: Gson): ApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OkHttpClient())
                .baseUrl(BuildConfig.BASE_URL)
                .build()
                .create(ApiService::class.java)
        }

        @Provides
        fun provideRepository(
            apiService: ApiService
        ): ExchangeRatesRepository = ExchangeRatesRepositoryImpl(apiService)

        @Singleton
        @Provides
        fun provideApplication() = MyApplication()
    }
}

@Retention
@MapKey
annotation class VMKey(val value: KClass<out ViewModel>)