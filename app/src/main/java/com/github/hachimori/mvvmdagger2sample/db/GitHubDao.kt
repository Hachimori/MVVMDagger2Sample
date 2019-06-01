package com.github.hachimori.mvvmdagger2sample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.hachimori.mvvmdagger2sample.model.CommitsEntity
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User

@Dao
interface GitHubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repos: List<Repos>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommits(commits: CommitsEntity)

    @Query("SELECT * FROM User WHERE login LIKE :user")
    fun findUser(user: String): LiveData<User>

    @Query("SELECT * FROM Repos WHERE owner_login LIKE :owner")
    fun loadRepositories(owner: String): LiveData<List<Repos>>

    @Query("SELECT * FROM CommitsEntity WHERE owner LIKE :owner and repos LIKE :repos")
    fun loadCommits(owner: String, repos: String): LiveData<CommitsEntity>
}