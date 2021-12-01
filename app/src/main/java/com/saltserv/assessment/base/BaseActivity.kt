package com.saltserv.assessment.base

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saltserv.assessment.ui.ErrorDialog
import com.saltserv.assessment.util.CloseScreen
import com.saltserv.assessment.util.ShowErrorDialog
import com.saltserv.assessment.util.ShowToast
import com.saltserv.assessment.util.ViewModelCommand
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import timber.log.Timber

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    val prefs: SharedPreferences by inject()

    private lateinit var vmCommandsSubscription: Disposable

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmCommandsSubscription = viewModel
            .commands
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleViewModelCommand(it) }

        Timber.plant(Timber.DebugTree())
    }

    override fun onDestroy() {
        super.onDestroy()
        vmCommandsSubscription.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    open fun handleViewModelCommand(command: ViewModelCommand): Boolean {
        when (command) {
            is ShowToast -> Toast.makeText(this, command.message, Toast.LENGTH_LONG).show()
            is ShowErrorDialog -> ErrorDialog.newInstance(command.message)
                .show(supportFragmentManager, ErrorDialog::class.java.simpleName)
            is CloseScreen -> finish()
        }

        return true
    }
}