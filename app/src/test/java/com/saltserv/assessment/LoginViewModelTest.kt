package com.saltserv.assessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.saltserv.assessment.testutils.TestBaseViewModelDependencies
import com.saltserv.assessment.ui.login.LoginViewModel
import com.saltserv.assessment.util.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val prefStore: PrefStore = mock()

    private val networkChecker: NetworkAvailabilityChecker = mock()

    private val tested by lazy {
        LoginViewModel(
            dependencies = TestBaseViewModelDependencies(),
            prefStore = prefStore,
            networkChecker = networkChecker
        )
    }

    @Test
    fun `does start login flow when connected`() {
        givenIsConnected()

        val tester = tested.commands.test()

        tested.loginButtonClicked()

        tester.assertValue(LoginWithSpotify)
    }

    @Test
    fun `does show error dialog when not connected`() {
        givenIsNotConnected()

        val tester = tested.commands.test()

        tested.loginButtonClicked()

        tester.assertValue(ShowErrorDialog("${R.string.offline_error}"))
    }

    @Test
    fun `does show login error dialog when spotify login fails`() {
        val tester = tested.commands.test()

        tested.spotifyResponseReceived(failedLoginResponse)

        tester.assertValue(ShowErrorDialog("${R.string.spotify_login_failed}"))
    }

    @Test
    fun `does open Search Screen when auth token received and token is not null`() {
        val tester = tested.commands.test()

        tested.spotifyResponseReceived(successfulLoginResponse)

        verify(prefStore).setAuthToken(successfulLoginResponse.accessToken)

        tester.assertValues(OpenSearchScreen, CloseScreen)
    }

    @Test
    fun `does show error dialog when auth token received and is null`() {
        val tester = tested.commands.test()

        tested.spotifyResponseReceived(tokenlessLoginResponse)

        tester.assertValue(ShowErrorDialog("${R.string.spotify_auth_failed}"))
    }

    private fun givenIsConnected() {
        whenever((networkChecker).isConnectedToInternet).thenReturn(true)
    }

    private fun givenIsNotConnected() {
        whenever((networkChecker).isConnectedToInternet).thenReturn(false)
    }

    private val failedLoginResponse =
        AuthenticationResponse.Builder().setType(AuthenticationResponse.Type.ERROR).build()

    private val successfulLoginResponse =
        AuthenticationResponse.Builder().setAccessToken("1uhei1u2he1").build()

    private val tokenlessLoginResponse = AuthenticationResponse.Builder().build()
}