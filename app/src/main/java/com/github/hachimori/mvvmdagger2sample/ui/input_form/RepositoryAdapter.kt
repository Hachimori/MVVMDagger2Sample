package com.github.hachimori.mvvmdagger2sample.ui.input_form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.model.Repos
import kotlinx.android.synthetic.main.item_repository.view.*

class RepositoryAdapter(private val viewModel: InputFormViewModel, private val reposList: List<Repos>) : RecyclerView.Adapter<RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(viewModel, view)
    }

    override fun getItemCount() = reposList.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(reposList[position])
    }

}

class RepositoryViewHolder(private val viewModel: InputFormViewModel, itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(repos: Repos) {
        itemView.item_repository_root.setOnClickListener {
            viewModel.onClickRepositoryItem(repos)
        }
        itemView.item_repository_name.text = repos.name
        itemView.item_repository_description.text = repos.description
    }
}
