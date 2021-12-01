package com.saltserv.assessment.util

import android.content.Context
import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getString(@StringRes stringId: Int): String
}

class ResourcesProviderImpl(private val context: Context) : ResourcesProvider {
    override fun getString(stringId: Int): String = context.getString(stringId)
}