package com.saltserv.assessment

import android.app.Application
import com.saltserv.assessment.di.application
import com.saltserv.assessment.di.network
import com.saltserv.assessment.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appInstance = this

        initDependencyGraph()
    }

    protected open fun initDependencyGraph() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    application,
                    network,
                    viewModels
                )
            )
        }
    }

    companion object {
        lateinit var appInstance: App
    }
}