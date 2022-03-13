package com.the.club.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.the.club.data.remote.auth.AuthApi
import com.the.club.data.remote.banners.BannersApi
import com.the.club.data.remote.bonusCards.BonusCardApi
import com.the.club.data.remote.clubs.ClubsApi
import com.the.club.data.remote.notifications.NotificationsApi
import com.the.club.data.remote.profile.ProfileApi
import com.the.club.data.remote.promotions.PromotionsApi
import com.the.club.data.remote.push.PushApi
import com.the.club.data.remote.refreshToken.RefreshTokenApi
import com.the.club.data.remote.shops.ShopsApi
import com.the.club.data.remote.surveys.SurveysApi
import com.the.club.data.remote.transactions.TransactionsApi
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) : AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideRefreshTokenApi(@Named("refresh_retrofit") refreshRetrofit: Retrofit): RefreshTokenApi =
        refreshRetrofit.create(RefreshTokenApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit) : ProfileApi =
        retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideBonusCardApi(retrofit: Retrofit): BonusCardApi =
        retrofit.create(BonusCardApi::class.java)

    @Provides
    @Singleton
    fun provideBannersApi(retrofit: Retrofit): BannersApi =
        retrofit.create(BannersApi::class.java)

    @Provides
    @Singleton
    fun provideClubsApi(retrofit: Retrofit): ClubsApi =
        retrofit.create(ClubsApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi =
        retrofit.create(NotificationsApi::class.java)

    @Provides
    @Singleton
    fun provideTransactionsApi(retrofit: Retrofit): TransactionsApi =
        retrofit.create(TransactionsApi::class.java)

    @Provides
    @Singleton
    fun providePromotionsApi(retrofit: Retrofit): PromotionsApi =
        retrofit.create(PromotionsApi::class.java)

    @Provides
    @Singleton
    fun provideShopsApi(retrofit: Retrofit): ShopsApi =
        retrofit.create(ShopsApi::class.java)

    @Provides
    @Singleton
    fun surveyShopsApi(retrofit: Retrofit): SurveysApi =
        retrofit.create(SurveysApi::class.java)

    @Provides
    @Singleton
    fun pushApi(retrofit: Retrofit): PushApi =
        retrofit.create(PushApi::class.java)

}