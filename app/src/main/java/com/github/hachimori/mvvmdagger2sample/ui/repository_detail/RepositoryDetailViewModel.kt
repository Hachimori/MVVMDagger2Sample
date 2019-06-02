package com.github.hachimori.mvvmdagger2sample.ui.repository_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.hachimori.mvvmdagger2sample.model.Commits
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.repository.GitHubRepository
import com.github.hachimori.mvvmdagger2sample.util.AbsentLiveData
import com.github.hachimori.mvvmdagger2sample.util.Resource

class RepositoryDetailViewModel constructor(private val repository: GitHubRepository): ViewModel() {
    val commitsList: MutableList<Commits> = mutableListOf()

    private val _repos: MutableLiveData<Repos> = MutableLiveData()
    val repos: LiveData<Repos> = _repos
    val listCommit: LiveData<Resource<List<Commits>>> = Transformations.switchMap(repos) { repo ->
        if (repo == null) {
            AbsentLiveData.create()
        } else {
            repository.listCommit(repo.owner.login, repo.name)
        }
    }

    fun getListCommit(repos: Repos) {
        _repos.value = repos
    }
}
