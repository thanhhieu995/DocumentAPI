package com.example.documentsapi.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.model.Repository;
import com.google.gson.JsonArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvRepository;
    RepositoryAdapter repoAdapter;
    List<Repository> repos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRepository = findViewById(R.id.main_rvRepository);
        rvRepository.setLayoutManager(new LinearLayoutManager(this));
        repoAdapter = new RepositoryAdapter(null, this);
        rvRepository.setAdapter(repoAdapter);

        callRepositoryApi();

    }

    private void callRepositoryApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        Call<List<Repository>> repos = service.listRepos();
        repos.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.body() != null) {
                    repoAdapter.setRepos(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }
        });
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String newText) {
        ArrayList<Repository> filteredList = new ArrayList<>();
        for (Repository repository : repos) {
            if (repository.full_name.toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))) {
                filteredList.add(repository);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "NO Data Found...", Toast.LENGTH_SHORT).show();
        } else {
            repoAdapter.filterList(filteredList);
        }
    }
}