package com.github.hachimori.mvvmdagger2sample.model

/**
 * Model class for storing data retrieved from GitHub API.
 *
 *   API Document:
 *       - https://developer.github.com/v3/
 */


/**
 * GitHub user information
 *   - https://developer.github.com/v3/users/#get-a-single-user
 */
data class User(
    val name: String,
    val company: String,
    val email: String,
    val bio: String,
    val created_at: String,
    val updated_at: String
)


/**
 * GitHub repository information
 *   - https://developer.github.com/v3/repos/#list-user-repositories
 */
data class Repos(
    val id: String,
    val name: String,
    val full_name: String,
    val url: String,
    val description: String
)


/**
 * GitHub commit information
 *   - https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository
 */
data class Commits(
    val url: String,
    val sha: String,
    private val commit: Commit
)


data class Commit(
    val message: String,
    val committer: Committer
)


data class Committer(
    val name: String,
    val email: String,
    val date: String
)
