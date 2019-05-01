package com.github.hachimori.mvvmdagger2sample.ui.input_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import com.github.hachimori.mvvmdagger2sample.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InputFormViewModel : ViewModel() {

    var reposList: MutableList<Repos> = mutableListOf()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val onClickRepositoryItemObservable: SingleLiveEvent<Repos> = SingleLiveEvent()
    private val userObservable: MutableLiveData<User> = MutableLiveData()
    private val userReposListObservable: MutableLiveData<List<Repos>> = MutableLiveData()


    fun getOnClickRepositoryItemObservable(): LiveData<Repos> = onClickRepositoryItemObservable

    fun onClickRepositoryItem(repos: Repos) {
        onClickRepositoryItemObservable.value = repos
    }

    fun getUserObservable(): LiveData<User> = userObservable

    fun getUser(userName: String) {
        uiScope.launch {
            val user = GitHubService.getService().getUser(userName).await()
            userObservable.value = user
        }
    }

    fun getUserReposListObservable(): LiveData<List<Repos>> = userReposListObservable

    fun getUserReposList(user: String) {
        uiScope.launch {
            val reposList = GitHubService.getService().listRepos(user).await()
            userReposListObservable.value = reposList
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
