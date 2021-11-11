package com.example.documentsapi.api;

import com.example.documentsapi.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("repositories")
    Call<List<Repository>> listRepos();
}
