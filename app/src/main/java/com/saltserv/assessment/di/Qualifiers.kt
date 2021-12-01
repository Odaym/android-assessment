package com.saltserv.assessment.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

object IoScheduler : Qualifier {
    override val value: QualifierValue
        get() = "IoScheduler"
}

object UiScheduler : Qualifier {
    override val value: QualifierValue
        get() = "UiScheduler"
}