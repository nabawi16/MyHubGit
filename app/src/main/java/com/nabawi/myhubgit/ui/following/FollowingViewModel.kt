package com.nabawi.myhubgit.ui.following

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nabawi.myhubgit.data.Repository

class FollowingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)

    fun getUserFollowing(username: String) = repository.getUserFollowing(username)
}