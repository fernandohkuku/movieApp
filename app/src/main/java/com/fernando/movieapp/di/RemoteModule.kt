package com.fernando.movieapp.di

import android.app.Activity
import android.content.Context
import com.fernando.core.data.exceptions.NotInternetException
import com.fernando.core.infrastructure.error.ErrorHandler
import com.fernando.movieapp.HomeActivity
import com.fernando.ui_ktx.content.isInternetAvailable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().apply {
        setDateFormat("yyyy-MM-dd'T':HH:mm:ss.SSS'Z'")
        setLenient()
    }.create()


    @Provides
    @Named("apiKey")
    fun provideApiKey() = "c6426bbd7e9df2dfb1fd8c67b4653042"

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://api.themoviedb.org/3/".toHttpUrl()

    @Provides
    @Named("InterceptorError")
    @Singleton
    fun provideInterceptorError(@Named("ErrorHandler") errorHandler: ErrorHandler) =
        Interceptor { chain ->
            val response = chain.proceed(chain.request())
            errorHandler.throwFromCode(response.code, response.body?.charStream())
            response
        }

    @Provides
    @Named("InterceptorInternet")
    @Singleton
    fun provideInterceptorInternet(@ApplicationContext context: Context) =
        Interceptor { chain ->
            if (!context.isInternetAvailable()) {
                throw NotInternetException()
            }
            chain.proceed(chain.request())
        }

    @Provides
    @Named("InterceptorAuthenticator")
    fun provideInterceptorAuthenticator(@Named("apiKey") apiKey: String) = Interceptor { chain ->
        val original = chain.request()
        val originalUrl = original.url
        val url = originalUrl.newBuilder().addQueryParameter("api_key", apiKey).build()
        val requestBuilder = original.newBuilder().url(url)
        chain.proceed(requestBuilder.build())

    }


    @Singleton
    @Provides
    fun provideOkHttpBuilder(
        @Named("InterceptorError") errorInterceptor: Interceptor,
        @Named("InterceptorInternet") internetInterceptor: Interceptor,
        @Named("InterceptorAuthenticator") authenticatorInterceptor: Interceptor,
    ) =
        OkHttpClient.Builder().apply {
            addInterceptor(errorInterceptor)
            addInterceptor(internetInterceptor)
            addInterceptor(authenticatorInterceptor)
        }.build()

    @Singleton
    @Provides
    fun provideRetroFit(@Named("BaseUrl") baseUrl: HttpUrl, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideHomeActivity()= HomeActivity::class.java as Class<out Activity>
}