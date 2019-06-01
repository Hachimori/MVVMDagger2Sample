package com.github.hachimori.mvvmdagger2sample.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.hachimori.mvvmdagger2sample.repository.GitHubRepository
import com.github.hachimori.mvvmdagger2sample.ui.input_form.InputFormViewModel

class InputFormViewModelFactory constructor(private val repository: GitHubRepository): ViewModelProvider.Factory {

     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(InputFormViewModel::class.java)) {
            InputFormViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}