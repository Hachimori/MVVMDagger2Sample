package com.github.hachimori.mvvmdagger2sample.ui.input_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.util.SingleLiveEvent

class InputFormViewModel : ViewModel() {
    
    private val onClickRepositoryItemObservable: SingleLiveEvent<Repos> = SingleLiveEvent()
    
    fun getOnClickRepositoryItemObservable(): LiveData<Repos> = onClickRepositoryItemObservable
    
    fun onClickRepositoryItem(repos: Repos) {
        onClickRepositoryItemObservable.value = repos
    }
}
