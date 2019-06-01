package com.github.hachimori.mvvmdagger2sample.network

import androidx.lifecycle.LiveData
import com.android.example.github.util.LiveDataCallAdapterFactory
import com.github.hachimori.mvvmdagger2sample.BuildConfig
import com.github.hachimori.mvvmdagger2sample.model.Commits
import com.github.hachimori.mvvmdagger2sample.model.Repos
import com.github.hachimori.mvvmdagger2sample.model.User
import com.github.hachimori.mvvmdagger2sample.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {

    /**
     * Get user information
     *   - https://developer.github.com/v3/users/#get-a-single-user
     */
    @GET("users/{username}")
    fun getUser(@Path("username") user: String): LiveData<ApiResponse<User>>

    /**
     * Get user's repository list
     *   - https://developer.github.com/v3/repos/#list-user-repositories
     */
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): LiveData<ApiResponse<List<Repos>>>

    /**
     * Get commit list of specified repository
     *   - https://developer.github.com/v3/repos/commits/#list-commits-on-a-repository
     */
    @GET("repos/{owner}/{repos}/commits")
    fun listCommit(@Path("owner") owner: String, @Path("repos") repos: String): LiveData<ApiResponse<List<Commits>>>


    companion object {
        private val INSTANCE: GitHubService

        init {
            val httpClientBuilder = OkHttpClient.Builder()

            // Display the result of Web API request on Logcat
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
                httpClientBuilder.addInterceptor(interceptor)
            }

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.GITHUB_API_ENDPOINT)
                    .client(httpClientBuilder.build())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            INSTANCE = retrofit.create(GitHubService::class.java)
        }

        fun getService() = INSTANCE
    }
}