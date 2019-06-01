package com.github.hachimori.mvvmdagger2sample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.hachimori.mvvmdagger2sample.model.CommitsEntity
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User

@Database(
    entities = [
        User::class,
        Repos::class,
        CommitsEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class GitHubDb : RoomDatabase() {
    abstract fun gitHubDao(): GitHubDao
}