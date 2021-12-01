package com.saltserv.assessment.testutils

import com.saltserv.assessment.util.ResourcesProvider

class FakeResourcesProvider : ResourcesProvider {
    override fun getString(stringId: Int) = "$stringId"
}