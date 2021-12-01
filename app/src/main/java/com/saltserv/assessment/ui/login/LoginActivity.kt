package com.saltserv.assessment.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.saltserv.assessment.R
import com.saltserv.assessment.base.BaseActivity
import com.saltserv.assessment.databinding.ActivityLoginBinding
import com.saltserv.assessment.ui.ErrorDialog
import com.saltserv.assessment.ui.search.SearchActivity
import com.saltserv.assessment.util.LoginWithSpotify
import com.saltserv.assessment.util.OpenSearchScreen
import com.saltserv.assessment.util.ViewModelCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<LoginViewModel>(), ErrorDialog.Listener {

    override val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    override fun handleViewModelCommand(command: ViewModelCommand) = when (command) {
        is LoginWithSpotify -> {
            loginWithSpotify()
            true
        }
        is OpenSearchScreen -> {
            SearchActivity.start(this)
            true
        }
        else -> super.handleViewModelCommand(command)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        viewModel.spotifyResponseReceived(response)
    }

    override fun onRetryClicked() {
        loginWithSpotify()
    }

    override fun onCancelClicked(dialog: ErrorDialog) {
        finish()
    }

    private fun loginWithSpotify() {
        AuthenticationClient.openLoginActivity(
            this, SPOTIFY_LOGIN_REQUEST,
            AuthenticationRequest.Builder(
                SPOTIFY_CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, Uri.Builder()
                    .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                    .authority(getString(R.string.com_spotify_sdk_redirect_host))
                    .build().toString()
            )
                .setShowDialog(true)
                .setScopes(arrayOf("user-read-email"))
                .build()
        )
    }

    companion object {
        const val SPOTIFY_LOGIN_REQUEST = 101
        const val SPOTIFY_CLIENT_ID = "84ea753e599142b8bace9b63d153227b"
    }
}
