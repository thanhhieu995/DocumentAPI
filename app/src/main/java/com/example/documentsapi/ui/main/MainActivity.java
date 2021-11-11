package com.example.documentsapi.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvRepository;
    RepositoryAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRepository = findViewById(R.id.main_rvRepository);
        rvRepository.setLayoutManager(new LinearLayoutManager(this));
        repoAdapter = new RepositoryAdapter(this, null);
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
}