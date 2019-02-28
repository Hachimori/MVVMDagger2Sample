package com.github.hachimori.mvvmdagger2sample.ui.input_form

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import kotlinx.android.synthetic.main.fragment_input_form.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InputFormFragment : Fragment() {

    companion object {
        fun newInstance() = InputFormFragment()
    }

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    
    private lateinit var viewModel: InputFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_input_form, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(InputFormViewModel::class.java)
        // TODO: Use the ViewModel
        
        input_form_get_user_info.setOnClickListener {
            uiScope.launch {
                val userName = input_form_github_name.text.toString()
                val user = GitHubService.getService().getUser(userName).await()
                
                input_form_user_info.text = getString(R.string.input_form_user_info, 
                    user.name, user.company, user.email, user.bio, user.created_at, user.updated_at)
                
                input_form_user_info.visibility = View.VISIBLE
                input_form_repository_list.visibility = View.GONE
            }
        }
        
        input_form_get_repository.setOnClickListener {
            uiScope.launch {
                val userName = input_form_github_name.text.toString()
                val reposList = GitHubService.getService().listRepos(userName).await()
                
                input_form_repository_list.layoutManager = LinearLayoutManager(context)
                input_form_repository_list.adapter = RepositoryAdapter(reposList)
                
                input_form_repository_list.visibility = View.VISIBLE
                input_form_user_info.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
