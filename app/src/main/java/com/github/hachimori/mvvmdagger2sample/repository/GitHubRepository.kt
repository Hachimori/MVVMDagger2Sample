package com.github.hachimori.mvvmdagger2sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.hachimori.mvvmdagger2sample.AppExecutors
import com.github.hachimori.mvvmdagger2sample.db.GitHubDao
import com.github.hachimori.mvvmdagger2sample.model.Commits
import com.github.hachimori.mvvmdagger2sample.model.CommitsEntity
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User
import com.github.hachimori.mvvmdagger2sample.network.GitHubService
import com.github.hachimori.mvvmdagger2sample.util.NetworkBoundResource
import com.github.hachimori.mvvmdagger2sample.util.RateLimiter
import com.github.hachimori.mvvmdagger2sample.util.Resource
import java.util.concurrent.TimeUnit

class GitHubRepository(
    private val appExecutors: AppExecutors,
    private val dao: GitHubDao,
    private val service: GitHubService
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun getUser(user: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                dao.insertUser(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = dao.findUser(user)

            override fun createCall() = service.getUser(user)
        }.asLiveData()
    }


    fun listRepos(user: String): LiveData<Resource<List<Repos>>> {
        return object : NetworkBoundResource<List<Repos>, List<Repos>>(appExecutors) {
            override fun saveCallResult(item: List<Repos>) {
                dao.insertRepos(item)
            }

            override fun shouldFetch(data: List<Repos>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(arrayOf("Repos", user).joinToString(separator = "#"))
            }

            override fun loadFromDb() = dao.loadRepositories(user)

            override fun createCall() = service.listRepos(user)
        }.asLiveData()
    }

    fun listCommit(owner: String, repos: String): LiveData<Resource<List<Commits>>> {
        return object : NetworkBoundResource<List<Commits>, List<Commits>>(appExecutors) {
            override fun saveCallResult(item: List<Commits>) {
                dao.insertCommits(CommitsEntity(owner, repos, item))
            }

            override fun shouldFetch(data: List<Commits>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(arrayOf("Commits", owner, repos).joinToString(separator = "#"))
            }

            override fun loadFromDb() = Transformations.map(dao.loadCommits(owner, repos)) { commits -> commits?.commits ?: listOf() }

            override fun createCall() = service.listCommit(owner, repos)
        }.asLiveData()
    }
}