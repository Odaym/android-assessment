package com.saltserv.assessment.ui.artist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import com.squareup.picasso.Picasso
import com.saltserv.assessment.base.BaseActivity
import com.saltserv.assessment.databinding.ActivityArtistDetailBinding
import com.saltserv.assessment.responses.ArtistItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistDetailActivity : BaseActivity<ArtistDetailViewModel>() {

    override val viewModel by viewModel<ArtistDetailViewModel>()
    private lateinit var binding: ActivityArtistDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val artist = intent.extras?.getParcelable<ArtistItem>(EXTRA_ARTIST_ITEM)

        if (artist != null) {
            binding.name.text = artist.name

            binding.genres.text = artist.genres.take(2)
                .joinToString(", ") { it.replaceFirstChar { char -> char.uppercaseChar() } }

            binding.goToArtist.setOnClickListener {
                val uriUrl: Uri = Uri.parse(artist.external_urls.spotify)
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                startActivity(launchBrowser)
            }

            Picasso.get()
                .load(artist.images.firstOrNull()?.url)
                .into(binding.image)
        } else {
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    companion object {
        const val EXTRA_ARTIST_ITEM = "ArtistItem"

        fun start(activity: Activity, artist: ArtistItem) {
            val intent = Intent(activity, ArtistDetailActivity::class.java)
            intent.putExtra(EXTRA_ARTIST_ITEM, artist)
            activity.startActivity(intent)
        }
    }
}