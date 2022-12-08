package com.example.searchimage.network

import com.example.searchimage.network.api.DownloadApi
import com.example.searchimage.network.api.SearchApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideSearchApi(@Named("search") okHttpClient: OkHttpClient): SearchApi {
        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    @Provides
    @Named("search")
    fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }
    }

    @Provides
    fun provideDownloadApi(): DownloadApi {
        return Retrofit.Builder()
            .baseUrl("https://various.img.url")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(DownloadApi::class.java)
    }

    @Provides
    fun provideAppInterceptor(): AppInterceptor {
        return AppInterceptor()
    }
}
