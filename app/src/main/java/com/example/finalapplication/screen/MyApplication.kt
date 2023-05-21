package com.example.finalapplication.screen

import android.app.Application
import android.preference.PreferenceManager
import com.example.finalapplication.data.repository.*
import com.example.finalapplication.data.repository.resource.*
import com.example.finalapplication.data.repository.resource.remote.*
import com.example.finalapplication.screen.chatroom.ChatViewModel
import com.example.finalapplication.screen.createaccount.CreateAccountViewModel
import com.example.finalapplication.screen.createpost.CreatePostViewModel
import com.example.finalapplication.screen.createpostaddress.CPAddressViewModel
import com.example.finalapplication.screen.createpostinfomation.CPInformationViewModel
import com.example.finalapplication.screen.detailpostsearch.PostSearchViewModel
import com.example.finalapplication.screen.explore.ExploreViewModel
import com.example.finalapplication.screen.historycontact.HistoryContactViewModel
import com.example.finalapplication.screen.home.HomeViewModel
import com.example.finalapplication.screen.incoming.IncomingViewModel
import com.example.finalapplication.screen.launch.LaunchViewModel
import com.example.finalapplication.screen.login.LoginViewModel
import com.example.finalapplication.screen.main.MainViewModel
import com.example.finalapplication.screen.mainstaff.MainStaffViewModel
import com.example.finalapplication.screen.mypost.MyPostViewModel
import com.example.finalapplication.screen.outgoing.OutgoingViewModel
import com.example.finalapplication.screen.postdetail.PostDetailViewModel
import com.example.finalapplication.screen.profile.ProfileViewModel
import com.example.finalapplication.screen.requireverify.RequireViewModel
import com.example.finalapplication.screen.requireverify.VerifyingViewModel
import com.example.finalapplication.screen.search.SearchViewModel
import com.example.finalapplication.screen.searchpost.SearchPostViewModel
import com.example.finalapplication.screen.statistics.StatisticViewModel
import com.example.finalapplication.screen.statisticstaff.StatisticStaffViewModel
import com.example.finalapplication.utils.ApiConstant
import com.example.finalapplication.utils.ApiFcmService
import com.example.finalapplication.utils.ApiGhn
import com.example.finalapplication.utils.FcmConstant
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val provideModule = module {

    fun provideRetrofit(factory: Gson): Retrofit {
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                return@addInterceptor it.proceed(request.build())
            }
            .addInterceptor(httpLogging)
            .build()
        return Retrofit.Builder()
            .baseUrl(FcmConstant.BASE_URL_FCM)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .build()
    }

    fun provideGson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()

    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiFcmService::class.java)
    single { provideApiService(get()) }
    single { provideGson() }
    single { provideRetrofit(get()) }
    single<UserDataSource.Remote> { RemoteUser() }
    single<UserRepository> { UserRepositoryIpml(get()) }
    single<NotificationRepository> {
        NotificationRepositoryImpl(
            provideApiService(
                provideRetrofit(
                    get()
                )
            )
        )
    }
}

val singletonModule = module {
    single<InvoiceDataSource> { RemoteInvoice() }
    single<InvoiceRepository> { InvoiceRepositoryImpl(get()) }
    single<ContactRepository> { ContactRepositoryImpl(get()) }
    single<ContactDataSource.Remote> { RemoteContact() }
    single<MessageDataSource.Remote> { RemoteMessage(get()) }
    single<MessageRepository> { MessageRepositoryImpl(get()) }
    single<PostDataSource> { RemotePost() }
    single<PostRepository> { PostRepositoryImpl(get()) }
    single<StatisticDataSource> { RemoteStatistic() }
    single<StatisticRepository> { StatisticRepositoryImpl(get()) }
    single {
        PreferenceManager.getDefaultSharedPreferences(androidContext())
    }
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
    single<HistorySearchDataSource> { RemoteHistory() }
}

val viewmodelModule = module {
    viewModel {
        CPInformationViewModel()
    }
    viewModel {
        ChatViewModel(get(), get(), get())
    }
    viewModel {
        HistoryContactViewModel(get())
    }
    viewModel {
        OutgoingViewModel(get(), get())
    }
    viewModel {
        IncomingViewModel(get(), get())
    }
    viewModel {
        LaunchViewModel(get())
    }
    viewModel {
        CreateAccountViewModel(get())
    }
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        ProfileViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
    viewModel {
        CreatePostViewModel(get())
    }
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        PostSearchViewModel(get())
    }
    viewModel {
        PostDetailViewModel(get(), get())
    }
    viewModel {
        SearchPostViewModel(get(), get(), get(), get())
    }
    viewModel {
        MyPostViewModel(get(), get())
    }
    viewModel {
        MainStaffViewModel(get(), get())
    }
    viewModel {
        RequireViewModel(get())
    }
    viewModel {
        VerifyingViewModel(get())
    }
    viewModel {
        ExploreViewModel(get())
    }
    viewModel {
        StatisticViewModel(get())
    }
    viewModel {
        StatisticStaffViewModel(get())
    }
}

val ghnModule = module {
    viewModel {
        CPAddressViewModel(get())
    }
    fun provideRetrofit(factory: Gson): Retrofit {
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                return@addInterceptor it.proceed(request.build())
            }
            .addInterceptor(httpLogging)
            .build()
        return Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL_GHN)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .build()
    }

    fun provideGson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()


    fun provideApiServiceGhn(retrofit: Retrofit) = retrofit.create(ApiGhn::class.java)
    single { provideApiServiceGhn(get()) }
    single { provideGson() }
    single { provideRetrofit(get()) }
    single<AddressRepository> { AddressRepositoryImpl(provideApiServiceGhn(provideRetrofit(get()))) }
}

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(viewmodelModule, singletonModule, provideModule, ghnModule)
        }
    }
}
