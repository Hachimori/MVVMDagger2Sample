package com.github.hachimori.mvvmdagger2sample.util

import androidx.lifecycle.LiveData

/**
 * Reference:
 *   - https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/util/AbsentLiveData.kt
 *
 * A LiveData class that has `null` value.
 */
class AbsentLiveData<T : Any?> private constructor(): LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}
