package com.github.hachimori.mvvmdagger2sample.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.hachimori.mvvmdagger2sample.repository.GitHubRepository
import com.github.hachimori.mvvmdagger2sample.ui.repository_detail.RepositoryDetailViewModel

class RepositoryDetailViewModelFactory constructor(private val repository: GitHubRepository): ViewModelProvider.Factory {

     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RepositoryDetailViewModel::class.java)) {
            RepositoryDetailViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}