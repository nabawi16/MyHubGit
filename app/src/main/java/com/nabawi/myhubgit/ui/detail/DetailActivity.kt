package com.nabawi.myhubgit.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.nabawi.myhubgit.R
import com.nabawi.myhubgit.data.Resource
import com.nabawi.myhubgit.databinding.ActivityDetailBinding
import com.nabawi.myhubgit.model.User
import com.nabawi.myhubgit.ui.adapter.FollowPagerAdapter
import com.nabawi.myhubgit.util.Constanta.EXTRA_USER
import com.nabawi.myhubgit.util.Constanta.TAB_TITLES
import com.nabawi.myhubgit.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), ViewStateCallback<User?> {
    private lateinit var detailBinding: ActivityDetailBinding
    private val viewModel  by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        val username = intent.getStringExtra(EXTRA_USER)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDetailUser(username.toString()).observe(this@DetailActivity, {
                when (it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> onSuccess(it.data)
                }
            })
        }

        val pageAdapter = FollowPagerAdapter(this, username.toString())

        detailBinding.apply {
            viewPager.adapter = pageAdapter
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = resources.getString(TAB_TITLES[position])
            }.attach()

            btnShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                val shareMessage = "Check out this user on MyHubGit: $username"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: User?) {

        detailBinding.apply {
            tvDetailNumberOfRepos.text = data?.repository.toString()
            tvDetailNumberOfFollowers.text = data?.follower.toString()
            tvDetailNumberOfFollowing.text = data?.following.toString()
            tvDetailName.text = data?.name
            tvDetailCompany.text = data?.company
            tvDetailLocation.text = data?.location

            Glide.with(this@DetailActivity)
                .load(data?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(ivDetailAvatar)

            ivFavorite.visibility = View.VISIBLE

            if (data?.isFavorite == true)
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
            else
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_unfavorite))

            supportActionBar?.title = data?.username

            ivFavorite.setOnClickListener {
                if (data?.isFavorite == true) {
                    data.isFavorite = false
                    viewModel.deleteFavoriteUser(data)
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_unfavorite))
                } else {
                    data?.isFavorite = true
                    data?.let { it1 -> viewModel.insertFavoriteUser(it1) }
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
                }
            }

        }
    }

    override fun onLoading() {
        detailBinding.ivFavorite.visibility = View.INVISIBLE
    }

    override fun onFailed(message: String?) {
        detailBinding.ivFavorite.visibility = View.INVISIBLE
    }
}