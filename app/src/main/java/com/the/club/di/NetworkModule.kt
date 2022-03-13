package com.the.club.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.the.club.common.ConstantUrls.serviceUrl
import com.the.club.common.AccessTokenInterceptor
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.data.remote.refreshToken.RefreshTokenApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(@Named("default_client") okHttpsClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpsClient)
            .baseUrl(serviceUrl)
            .build()
    }

    @Provides
    @Singleton
    @Named("refresh_retrofit")
    fun provideRefreshApi(@Named("refresh_client") okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(serviceUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAccessTokenInterceptor(refreshApi: RefreshTokenApi, prefs: PreferenceRepository) : AccessTokenInterceptor {
        return AccessTokenInterceptor(refreshApi, prefs)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    @Named("refresh_client")
    fun provideRefreshOkHttpClient() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request().newBuilder().build()
                )
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Named("default_client")
    fun provideOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}