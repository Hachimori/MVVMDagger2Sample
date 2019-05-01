package com.github.hachimori.mvvmdagger2sample.ui.input_form




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User
import com.github.hachimori.mvvmdagger2sample.ui.repository_detail.RepositoryDetailFragmentArgs
import kotlinx.android.synthetic.main.fragment_input_form.*

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

        viewModel = ViewModelProviders.of(this).get(InputFormViewModel::class.java)

        viewModel.getOnClickRepositoryItemObservable().observe(viewLifecycleOwner, Observer<Repos> { repos ->
            val args = RepositoryDetailFragmentArgs.Builder(repos).build()
            findNavController().navigate(R.id.repositoryDetailFragment, args.toBundle())
        })

        viewModel.getUserObservable().observe(viewLifecycleOwner, Observer<User> { user ->
            input_form_user_info.text = getString(R.string.input_form_user_info,
                user.name, user.company, user.email, user.bio, user.created_at, user.updated_at)

            input_form_user_info.visibility = View.VISIBLE
            input_form_repository_list.visibility = View.GONE
        })

        viewModel.getUserReposListObservable().observe(viewLifecycleOwner, Observer<List<Repos>> { result ->
            viewModel.reposList.clear()
            viewModel.reposList.addAll(result)

            input_form_repository_list.layoutManager = LinearLayoutManager(context)
            input_form_repository_list.adapter = RepositoryAdapter(viewModel, viewModel.reposList)

            input_form_repository_list.visibility = View.VISIBLE
            input_form_user_info.visibility = View.GONE
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
