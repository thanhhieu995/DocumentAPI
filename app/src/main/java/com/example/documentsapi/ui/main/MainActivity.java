package com.example.documentsapi.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.listener.EndlessRecyclerViewScrollListener;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvRepository;
    ImageView btnSearch;

    RepositoryAdapter repoAdapter;

    List<Repository> repositories;
    Repository repository;

    private EndlessRecyclerViewScrollListener scrollListener;

    boolean isLoading = false;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.main_refreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rvRepository = findViewById(R.id.main_rvRepository);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRepository.setLayoutManager(layoutManager);
        repoAdapter = new RepositoryAdapter(repositories, this);
        rvRepository.setAdapter(repoAdapter);

        ButterKnife.bind(this);

        callRepositoryApi(1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                repoAdapter.clearData();
                callRepositoryApi(1);
            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callRepositoryApi(repoAdapter.largestId());
            }
        };

        rvRepository.addOnScrollListener(scrollListener);

        initScrollListener();
    }

    private void callRepositoryApi(int since) {
        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);

        Call<List<Repository>> repos = service.listReposEndScroll(since);
        //Call<List<Repository>> repos = service.listRepos();
        repos.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.body() != null) {
                    if (since == 1) {
                        repoAdapter.setRepos(response.body());
                        repositories = response.body();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        repoAdapter.addRepos(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
//        SearchView searchView = (SearchView) searchItem.getActionView();
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    private void initScrollListener() {
        rvRepository.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) rvRepository.getLayoutManager();
                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == repositories.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        repositories.add(null);

        repoAdapter.notifyItemInserted(repositories.size() -1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                repositories.remove(repositories.size() - 1);
                int scrollPosition = repositories.size();

                repoAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

//                while (currentSize - 1 < nextLimit) {
//                    repositories.add(currentSize)
//                }

                repoAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000 );
    }
}