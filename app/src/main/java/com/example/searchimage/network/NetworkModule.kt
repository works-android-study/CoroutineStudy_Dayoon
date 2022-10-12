package com.example.searchimage.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideSearchApi(okHttpClient: OkHttpClient): SearchApi {
        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    @Provides
    fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }
    }

    @Provides
    fun provideAppInterceptor(): AppInterceptor {
        return AppInterceptor()
    }
}
