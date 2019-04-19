package com.github.hachimori.mvvmdagger2sample.ui.repository_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hachimori.mvvmdagger2sample.R
import com.github.hachimori.mvvmdagger2sample.model.Commits
import kotlinx.android.synthetic.main.item_commit.view.*

class CommitAdapter(private val commitList: List<Commits>) : RecyclerView.Adapter<CommitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_commit, parent, false)
        return CommitViewHolder(view)
    }

    override fun getItemCount() = commitList.size

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        holder.bind(commitList[position])
    }
}

class CommitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(commit: Commits) {
        itemView.item_commit_commit_message.text = commit.commit.message
        itemView.item_commit_sha.text = commit.sha.substring(0..7)
        itemView.item_commit_committer.text = commit.commit.committer.name
        itemView.item_commit_date.text = commit.commit.committer.date
    }
}
