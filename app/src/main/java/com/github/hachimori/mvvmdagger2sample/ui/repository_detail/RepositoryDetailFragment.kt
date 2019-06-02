package com.github.hachimori.mvvmdagger2sample.ui.repository_detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.github.hachimori.mvvmdagger2sample.AppExecutors
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.db.GitHubDb
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import com.github.hachimori.mvvmdagger2sample.repository.GitHubRepository
import com.github.hachimori.mvvmdagger2sample.util.Status
import com.github.hachimori.mvvmdagger2sample.viewmodelfactory.RepositoryDetailViewModelFactory
import kotlinx.android.synthetic.main.fragment_repository_detail.*
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class RepositoryDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RepositoryDetailFragment()
    }
    private lateinit var viewModel: RepositoryDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_repository_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = RepositoryDetailViewModelFactory(GitHubRepository(
            AppExecutors(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(3),
                object: Executor {
                    private val mainThreadHandler = Handler(Looper.getMainLooper())
                    override fun execute(command: Runnable) {
                        mainThreadHandler.post(command)
                    }
                }
            ),
            Room
                .databaseBuilder(context!!, GitHubDb::class.java, "github.db")
                .fallbackToDestructiveMigration()
                .build().gitHubDao(),
            GitHubService.getService()
        ))

        viewModel = ViewModelProviders.of(activity!!, factory).get(RepositoryDetailViewModel::class.java)

        repository_detail_commit_list.layoutManager = LinearLayoutManager(context)
        repository_detail_commit_list.adapter = CommitAdapter(viewModel.commitsList)
        repository_detail_commit_list.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

        viewModel.listCommit.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val commitsList = resource.data ?: listOf()

                    viewModel.commitsList.clear()
                    viewModel.commitsList.addAll(commitsList)

                    (repository_detail_commit_list.adapter as CommitAdapter).notifyDataSetChanged()
                }
                Status.ERROR -> Timber.d(resource.message)
                Status.LOADING -> Timber.i("Loading")
            }
        })

        arguments?.let {
            val safeArgs = RepositoryDetailFragmentArgs.fromBundle(it)
            val repos = safeArgs.repos

            repository_detail_repository.text = repos.name
            repository_detail_description.text = repos.description

            viewModel.getListCommit(repos)
        }
    }
}
