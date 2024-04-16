package com.nabawi.myhubgit.util

import androidx.annotation.StringRes
import com.nabawi.myhubgit.BuildConfig
import com.nabawi.myhubgit.R

object Constanta {
    const val TIME_SPLASH = 2000L

    const val EXTRA_USER = "EXTRA_USER"

    @StringRes
    val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )

    const val GITHUB_TOKEN = BuildConfig.GITHUB_TOKEN

    const val BASE_URL = BuildConfig.BASE_URL
}