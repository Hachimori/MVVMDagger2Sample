package com.github.hachimori.mvvmdagger2sample.ui.input_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User
import com.github.hachimori.mvvmdagger2sample.repository.GitHubRepository
import com.github.hachimori.mvvmdagger2sample.util.AbsentLiveData
import com.github.hachimori.mvvmdagger2sample.util.Resource
import com.github.hachimori.mvvmdagger2sample.util.SingleLiveEvent

class InputFormViewModel constructor(private val repository: GitHubRepository): ViewModel() {
    val reposList: MutableList<Repos> = mutableListOf()

    private val _userUserName: MutableLiveData<String> = MutableLiveData()
    val userUserName: LiveData<String> = _userUserName
    val user: LiveData<Resource<User>> = Transformations.switchMap(userUserName) { userName ->
        if (userName == null) {
            AbsentLiveData.create()
        } else {
            repository.getUser(userName)
        }
    }

    private val _reposUserName: MutableLiveData<String> = MutableLiveData()
    val reposUserName: LiveData<String> = _reposUserName
    val repos: LiveData<Resource<List<Repos>>>  = Transformations.switchMap(reposUserName) { userName ->
        if (userName == null) {
            AbsentLiveData.create()
        } else {
            repository.listRepos(userName)
        }
    }


    private val onClickRepositoryItemObservable: SingleLiveEvent<Repos> = SingleLiveEvent()

    fun getOnClickRepositoryItemObservable(): LiveData<Repos> = onClickRepositoryItemObservable

    fun onClickRepositoryItem(repos: Repos) {
        onClickRepositoryItemObservable.value = repos
    }

    fun getUser(name: String) {
        _userUserName.value = name
    }

    fun getUserReposList(user: String) {
        _reposUserName.value = user
    }
}
