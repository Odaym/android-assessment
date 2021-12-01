package com.saltserv.assessment.testutils

import com.saltserv.assessment.base.BaseViewModel
import com.saltserv.assessment.util.ResourcesProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

object TestBaseViewModelDependencies {
    operator fun invoke(
        ioScheduler: Scheduler = Schedulers.trampoline(),
        uiScheduler: Scheduler = Schedulers.trampoline(),
        resourceProvider: ResourcesProvider = FakeResourcesProvider(),
    ) = BaseViewModel.Dependencies(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler,
        resourceProvider = resourceProvider
    )
}