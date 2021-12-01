package com.saltserv.assessment.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.saltserv.assessment.base.BaseActivity
import com.saltserv.assessment.databinding.ActivitySearchBinding
import com.saltserv.assessment.ui.ErrorDialog
import com.saltserv.assessment.ui.artist.ArtistDetailActivity
import com.saltserv.assessment.util.OpenArtistDetailScreen
import com.saltserv.assessment.util.ViewModelCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<SearchViewModel>(), ErrorDialog.Listener {

    override val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: ActivitySearchBinding

    private val adapter by lazy { RecyclerAdapter(viewModel::onListItemClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        viewModel.isRefreshing.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = it == true
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText ?: "")
                return true
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.search()
        }

        setupRecycler()
    }

    override fun handleViewModelCommand(command: ViewModelCommand) = when (command) {
        is OpenArtistDetailScreen -> {
            ArtistDetailActivity.start(this, command.artist)
            true
        }
        else -> super.handleViewModelCommand(command)
    }

    override fun onRetryClicked() {
        viewModel.search()
    }

    override fun onCancelClicked(dialog: ErrorDialog) {
        dialog.dismiss()
    }

    private fun setupRecycler() {
        viewModel.listItems.observe(this, adapter)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
