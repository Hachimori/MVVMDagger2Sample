package com.github.hachimori.mvvmdagger2sample.model

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import java.lang.reflect.Type

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
@Entity(primaryKeys = ["login"])
data class User(
    val login: String,
    val name: String,
    val company: String?,
    val email: String?,
    val bio: String?,
    val created_at: String,
    val updated_at: String
)


/**
 * GitHub repository information
 *   - https://developer.github.com/v3/repos/#list-user-repositories
 */
@Entity(
    indices = [
        Index("id"),
        Index("owner_login")],
    primaryKeys = ["name", "owner_login"]
)
@Parcelize
data class Repos(
    val id: Int,
    val name: String,
    val full_name: String,
    val url: String,
    val description: String?,
    @field:Embedded(prefix = "owner_")
    val owner: Owner
): Parcelable {

    @Parcelize
    data class Owner(
        val login: String
    ): Parcelable
}


/**
 * GitHub commit information
 *   - https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository
 */
data class Commits(
    val url: String,
    val sha: String,
    val commit: Commit
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


/**
 * Entity class for local DB.
 * This holds GitHub commit information.
 *   - https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository
 */
@Entity(
    indices = [
        Index("owner"),
        Index("repos")],
    primaryKeys = ["owner", "repos"]
)
@TypeConverters(CommitsConverter::class) // https://stackoverflow.com/q/44582397
data class CommitsEntity(
    val owner: String,
    val repos: String,
    val commits: List<Commits>
)

class CommitsConverter {

    @TypeConverter
    fun fromCommitList(commits: List<Commits>): String {
        return Gson().toJson(commits)
    }

    @TypeConverter
    fun toCommitList(commitListString: String): List<Commits> {
        // https://stackoverflow.com/a/46401789
        // https://qiita.com/chibatching/items/772a02e7043aeb29c645
        // https://mobikul.com/add-typeconverters-room-database-android/
        val type: Type = object : TypeToken<List<Commits>>() {}.type
        return Gson().fromJson(commitListString, type)
    }
}