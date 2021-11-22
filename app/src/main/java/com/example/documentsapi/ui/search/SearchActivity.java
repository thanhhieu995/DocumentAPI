package com.example.documentsapi.ui.search;

import androidx.appcompat.app.AppCompatActivity;

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
    List<Repository> repositories;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);

        callRepositoryApi();

//        Intent intent = getIntent();
//        repositories = intent.getParcelableExtra("repositories");

    }


    private void callRepositoryApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        Call<SearchResponse> repos = service.searchRepository("acb");

        repos.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.body() != null) {
                    repoAdapter.setRepos(response.body().items);
                    repositories = response.body().items;
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
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

    }

//    public boolean onCreateOptionsMenu (Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.actionSearch);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
//
    private void filter(String newText) {
        ArrayList<Repository> filteredList = new ArrayList<>();
        if (repositories != null) {
            for (Repository repository : repositories) {
                if (repository.full_name.toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))) {
                    filteredList.add(repository);
                }
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "NO Data Found...", Toast.LENGTH_SHORT).show();
        } else {
            repoAdapter.filterList(filteredList);
        }
    }
}