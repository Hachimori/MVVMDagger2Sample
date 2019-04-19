package com.github.hachimori.mvvmdagger2sample.ui.repository_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import kotlinx.android.synthetic.main.fragment_repository_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RepositoryDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoryDetailFragment()
    }

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var viewModel: RepositoryDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_repository_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RepositoryDetailViewModel::class.java)

        arguments?.let {
            val safeArgs = RepositoryDetailFragmentArgs.fromBundle(it)
            val repos = safeArgs.repos
            val user = safeArgs.user

            repository_detail_repository.text = repos.name
            repository_detail_description.text = repos.description

            uiScope.launch {
                val commitsList = GitHubService.getService().listCommit(user, repos.name).await()
                repository_detail_commit_list.layoutManager = LinearLayoutManager(context)
                repository_detail_commit_list.adapter = CommitAdapter(commitsList)
                repository_detail_commit_list.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
