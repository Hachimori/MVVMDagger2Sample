package com.github.hachimori.mvvmdagger2sample.ui.repository_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hachimori.mvvmdagger2sample.model.Commits
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RepositoryDetailViewModel : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val listCommitObservable: MutableLiveData<List<Commits>> = MutableLiveData()

    fun getListCommitObservable(): LiveData<List<Commits>> = listCommitObservable

    fun getListCommit(repos: Repos) {
        uiScope.launch {
            val commitsList = GitHubService.getService().listCommit(repos.owner.login, repos.name).await()
            listCommitObservable.value = commitsList
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
