package com.the.club.di

import com.google.gson.Gson
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
import com.the.club.data.remote.shops.ShopsApi
import com.the.club.data.remote.surveys.SurveysApi
import com.the.club.data.remote.transactions.TransactionsApi
import com.the.club.data.repository.*
import com.the.club.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, gson: Gson) : AuthRepository {
        return AuthRepositoryImpl(gson, api)
    }

    @Provides
    fun provideProfileRepository(profileApi: ProfileApi, gson: Gson): ProfileRepository =
        ProfileRepositoryImpl(gson, profileApi)

    @Provides
    fun provideBonusCardsRepository(bonusCardsApi: BonusCardApi, gson: Gson): BonusCardsRepository =
        BonusCardsRepositoryImpl(gson, bonusCardsApi)

    @Provides
    fun provideBannersRepository(bannersApi: BannersApi, gson: Gson): BannersRepository =
        BannersRepositoryImpl(gson, bannersApi)

    @Provides
    fun provideClubsRepository(clubsApi: ClubsApi, gson: Gson): ClubsRepository =
        ClubsRepositoryImpl(gson, clubsApi)


    @Provides
    fun provideNotificationsRepository(notificationsApi: NotificationsApi, gson: Gson): NotificationsRepository =
        NotificationsRepositoryImpl(gson, notificationsApi)

    @Provides
    fun provideTransactionsRepository(transactionsApi: TransactionsApi, gson: Gson): TransactionsRepository =
        TransactionsRepositoryImpl(gson, transactionsApi)

    @Provides
    fun providePromotionsRepository(promotionsApi: PromotionsApi, gson: Gson): PromotionsRepository =
        PromotionsRepositoryImpl(gson, promotionsApi)

    @Provides
    fun provideShopsRepository(shopsApi: ShopsApi, gson: Gson): ShopsRepository =
        ShopsRepositoryImpl(gson, shopsApi)

    @Provides
    fun provideSurveyRepository(surveysApi: SurveysApi, gson: Gson): SurveysRepository =
        SurveysRepositoryImpl(gson, surveysApi)

    @Provides
    fun providePushRepository(pushApi: PushApi, gson: Gson): PushRepository =
        PushRepositoryImpl(gson, pushApi)
}