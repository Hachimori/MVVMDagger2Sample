package com.github.hachimori.mvvmdagger2sample.ui.input_form




import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.github.hachimori.mvvmdagger2sample.AppExecutors
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.db.GitHubDb
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import com.github.hachimori.mvvmdagger2sample.repository.GitHubRepository
import com.github.hachimori.mvvmdagger2sample.ui.repository_detail.RepositoryDetailFragmentArgs
import com.github.hachimori.mvvmdagger2sample.util.Status
import com.github.hachimori.mvvmdagger2sample.viewmodelfactory.InputFormViewModelFactory
import kotlinx.android.synthetic.main.fragment_input_form.*
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class InputFormFragment : Fragment() {

    companion object {
        fun newInstance() = InputFormFragment()
    }

    private lateinit var viewModel: InputFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_input_form, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = InputFormViewModelFactory(GitHubRepository(
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

        viewModel = ViewModelProviders.of(this, factory).get(InputFormViewModel::class.java)

        viewModel.getOnClickRepositoryItemObservable().observe(viewLifecycleOwner, Observer { repos ->
            val args = RepositoryDetailFragmentArgs.Builder(repos).build()
            findNavController().navigate(R.id.repositoryDetailFragment, args.toBundle())
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    if (resource.data != null) {
                        val user = resource.data
                        input_form_user_info.text = getString(
                            R.string.input_form_user_info,
                            user.name, user.company, user.email, user.bio, user.created_at, user.updated_at
                        )
                    } else {
                        input_form_user_info.text = getString(
                            R.string.input_form_user_info, "", "", "", "", "", ""
                        )
                    }
                    input_form_user_info.visibility = View.VISIBLE
                    input_form_repository_list.visibility = View.GONE
                }
                Status.ERROR -> Timber.d(resource.message)
                Status.LOADING -> Timber.i("user loading")
            }

        })

        input_form_repository_list.layoutManager = LinearLayoutManager(context)
        input_form_repository_list.adapter = RepositoryAdapter(viewModel, viewModel.reposList)

        viewModel.repos.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val reposList = resource.data ?: listOf()
                    viewModel.reposList.clear()
                    viewModel.reposList.addAll(reposList)

                    (input_form_repository_list.adapter as RepositoryAdapter).notifyDataSetChanged()

                    input_form_repository_list.visibility = View.VISIBLE
                    input_form_user_info.visibility = View.GONE
                }
                Status.ERROR -> Timber.d(resource.message)
                Status.LOADING -> Timber.i("repository loading")
            }
        })

        input_form_get_user_info.setOnClickListener {
            val userName = input_form_github_name.text.toString()
            viewModel.getUser(userName)
        }

        input_form_get_repository.setOnClickListener {
            val userName = input_form_github_name.text.toString()
            viewModel.getUserReposList(userName)
        }
    }
}
