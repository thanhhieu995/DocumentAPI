package com.example.documentsapi.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.model.User;
import com.example.documentsapi.ui.main.RepositoryAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgLogo;
    TextView tvName;
    RecyclerView recyclerView;
    RepositoryAdapter repositoryAdapter;
    List<Repository> repositories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgLogo = findViewById(R.id.imgLogo);
        tvName = findViewById(R.id.profile_tvName);
        recyclerView = findViewById(R.id.rv_recyclerView_Profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repositoryAdapter = new RepositoryAdapter(repositories, this);
        recyclerView.setAdapter(repositoryAdapter);

        Bundle bundle = getIntent().getExtras();
        Repository repository = (Repository) bundle.getSerializable("repository");
        Picasso.get().load(repository.owner.avatar_url).into(imgLogo);
        tvName.setText(repository.owner.login);

        callRepositoryAPI(repository.owner.login);
    }

    private void callRepositoryAPI(String fullname) {
        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);

        Call<List<Repository>> repos = service.listUserRepos(fullname);
        repos.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                repositoryAdapter.setRepos(response.body());
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }
        });
    }
}