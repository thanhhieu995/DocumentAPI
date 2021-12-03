package com.example.documentsapi.api;

import com.example.documentsapi.model.Issues;
import com.example.documentsapi.model.IssuesComment;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.model.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("repositories")
    Call<List<Repository>> listRepos();

    @GET("search/repositories")
    Call<SearchResponse> searchRepository(@Query("q") String keyword, @Query("page") int page, @Query("per_page") int perPage);

    @GET("orgs/{username}/repos")
    Call<List<Repository>> listUserRepos(@Path("username") String username);

    @GET("repos/{username}/{reponame}/issues")
    Call<List<Issues>> listIssues(@Path("username") String username, @Path("reponame") String reponame);

    @GET("repos/{username}/{reponame}/issues")
    Call<List<Issues>> listIssuesEndScroll(@Path("username") String username, @Path("reponame") String reponame, @Query("page") int page, @Query("per_page") int perPage);

    @GET("repos/{owner}/{repo}/issues/{issues_number}/comments")
    Call<List<IssuesComment>> listIssueComment(@Path("owner") String owner, @Path("repo") String repo, @Path("issues_number") int issues_number);
}
