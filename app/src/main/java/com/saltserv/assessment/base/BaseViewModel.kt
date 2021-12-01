package com.saltserv.assessment.base

import androidx.lifecycle.ViewModel
import com.saltserv.assessment.util.CloseScreen
import com.saltserv.assessment.util.ResourcesProvider
import com.saltserv.assessment.util.ViewModelCommand
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class BaseViewModel(dependencies: Dependencies) : ViewModel() {
    private val ioScheduler = dependencies.ioScheduler
    private val uiScheduler = dependencies.uiScheduler
    protected val resourcesProvider = dependencies.resourceProvider

    private val subscriptions = CompositeDisposable()

    private val commandsSubject: Subject<ViewModelCommand> = PublishSubject.create()
    val commands: Observable<ViewModelCommand> = commandsSubject.hide()

    protected fun emitCommand(command: ViewModelCommand) {
        commandsSubject.onNext(command)
    }

    private fun subscription(block: () -> Disposable) {
        subscriptions.add(block())
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    protected fun <T> Single<T>.viewModelSubscription(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = subscription {
        subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe(onSuccess, onError)
    }

    fun onBackButtonClicked() = emitCommand(CloseScreen)

    data class Dependencies(
        val resourceProvider: ResourcesProvider,
        val ioScheduler: Scheduler,
        val uiScheduler: Scheduler
    )
}