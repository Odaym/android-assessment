package com.saltserv.assessment.di

import com.saltserv.assessment.App.Companion.appInstance
import com.saltserv.assessment.BuildConfig
import com.saltserv.assessment.PrefStore
import com.saltserv.assessment.PrefStoreImpl
import com.saltserv.assessment.base.BaseViewModel
import com.saltserv.assessment.network.*
import com.saltserv.assessment.ui.artist.ArtistDetailViewModel
import com.saltserv.assessment.ui.login.LoginViewModel
import com.saltserv.assessment.ui.search.SearchViewModel
import com.saltserv.assessment.util.NetworkAvailabilityChecker
import com.saltserv.assessment.util.NetworkAvailabilityCheckerImpl
import com.saltserv.assessment.util.ResourcesProvider
import com.saltserv.assessment.util.ResourcesProviderImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val json = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
    coerceInputValues = true
}

val application = module {
    single {
        appInstance
    }

    single {
        json
    }

    single<PrefStore> {
        PrefStoreImpl(get())
    }

    single<ResourcesProvider> {
        ResourcesProviderImpl(get())
    }

    single(IoScheduler) {
        Schedulers.io()
    }

    single(UiScheduler) {
        AndroidSchedulers.mainThread()
    }
}

val network = module {

    single<NetworkAvailabilityChecker> {
        NetworkAvailabilityCheckerImpl(get())
    }

    single {
        HostProvider()
    }

    factory {
        val cache = Cache(androidContext().cacheDir, 10 * 1024 * 1024L)

        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                } else {
                    it.addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                }
            }
            .addInterceptor(CacheInterceptor())
            .cache(cache)
            .build()

        NetworkRequestProcessor(client, get())
    }

    single<ApiService> {
        ApiServiceImpl(
            processor = get(),
            hostProvider = get(),
            prefStore = get()
        )
    }
}

val viewModels = module {
    single {
        BaseViewModel.Dependencies(
            resourceProvider = get(),
            ioScheduler = get(IoScheduler),
            uiScheduler = get(UiScheduler),
        )
    }
    viewModel {
        LoginViewModel(
            dependencies = get(),
            prefStore = get(),
            networkChecker = get()
        )
    }

    viewModel {
        SearchViewModel(
            dependencies = get(),
            apiService = get(),
            networkChecker = get()
        )
    }

    viewModel {
        ArtistDetailViewModel(
            dependencies = get()
        )
    }
}