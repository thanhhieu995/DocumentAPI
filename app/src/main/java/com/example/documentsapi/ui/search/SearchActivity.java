package com.example.documentsapi.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.util.Log;
import android.widget.SearchView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.listener.EndlessRecyclerViewScrollListener;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.model.SearchResponse;
import com.example.documentsapi.ui.main.RepositoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    RepositoryAdapter repoAdapter;
    SearchView searchView;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.rv_recylerView_Search);
        refreshLayout = findViewById(R.id.search_srlRefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        repoAdapter = new RepositoryAdapter(null, this);
        recyclerView.setAdapter(repoAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callRepositoryApi(searchView.getQuery().toString(), page + 1);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                repoAdapter.clearData();
                callRepositoryApi(searchView.getQuery().toString(), 1);
            }
        });

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                repoAdapter.clearData();
                scrollListener.resetState();
                callRepositoryApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void callRepositoryApi(String keyword, int page) {
        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);

        Call<SearchResponse> repos = service.searchRepository(keyword, page, 10);

        repos.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.body() != null) {
                    List<Repository> repoList = response.body().items;
                    if (page == 1) {
                        repoAdapter.setRepos(repoList);
                        refreshLayout.setRefreshing(false);
                    } else {
                        repoAdapter.addRepos(repoList);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}