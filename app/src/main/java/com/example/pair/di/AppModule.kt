package com.example.pair.di

import com.example.pair.ApiService
import com.example.pair.MyApplication
import com.example.pair.data.ExchangeResponse
import com.example.pair.data.ExchangeResponseDeserializer
import com.example.pair.repository.MyRepository
import com.example.pair.repository.MyRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(ExchangeResponse::class.java, ExchangeResponseDeserializer())
        .create()

    @Provides
    fun provideRetrofitService(gson: Gson): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .baseUrl("https://v6.exchangerate-api.com")
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideRepository(
        apiService: ApiService
    ): MyRepository = MyRepositoryImpl(apiService)

//    @Binds
//    abstract fun bindsViewModel(
//        mainViewModel: MainViewModel
//    ): MainViewModel
//
//    @Binds
//    abstract fun bindsUseCase(): MyUseCase

    @Singleton
    @Provides
    fun provideApplication() = MyApplication()
}