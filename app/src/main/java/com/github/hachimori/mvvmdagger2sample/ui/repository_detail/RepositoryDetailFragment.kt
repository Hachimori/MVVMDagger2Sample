package com.github.hachimori.mvvmdagger2sample.ui.repository_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.model.Commits
import kotlinx.android.synthetic.main.fragment_repository_detail.*

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
        viewModel = ViewModelProviders.of(this).get(RepositoryDetailViewModel::class.java)

        viewModel.getListCommitObservable().observe(viewLifecycleOwner, Observer<List<Commits>> { commitsList ->
            repository_detail_commit_list.layoutManager = LinearLayoutManager(context)
            repository_detail_commit_list.adapter = CommitAdapter(commitsList)
            repository_detail_commit_list.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))
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
