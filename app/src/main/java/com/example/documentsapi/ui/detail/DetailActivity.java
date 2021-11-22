package com.example.documentsapi.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.main.RepositoryAdapter;
import com.example.documentsapi.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtDescription;
    ImageView imgLogo;
    Repository repository;
    RecyclerView recyclerView;
    RepositoryAdapter repositoryAdapter;
    List<Repository> repositories;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtName = findViewById(R.id.detail_txtfullName);
        txtDescription = findViewById(R.id.detail_txtDescription);
        imgLogo = findViewById(R.id.detail_imgLogo);
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ProfileActivity.class);
                intent.putExtra("repository", repository);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        repository = (Repository) bundle.getSerializable("repository");

        txtName.setText(repository.full_name);
        txtDescription.setText(repository.description);
        Picasso.get().load(repository.owner.avatar_url).into(imgLogo);


        recyclerView = findViewById(R.id.detail_rvListIssues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repositoryAdapter = new RepositoryAdapter(repositories, this);
        recyclerView.setAdapter(repositoryAdapter);

        callRepositoryAPI(repository.owner.login, repository.owner.title);
    }

    private void callRepositoryAPI(String name, String reponame) {
        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);

        Call<List<Repository>> repos = service.listIssues(name, reponame);
        repos.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                repositoryAdapter.setRepos(response.body());
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}