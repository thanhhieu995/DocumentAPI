package com.example.documentsapi.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.model.Issues;
import com.example.documentsapi.model.Repository;
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
    Issues issues;
    RecyclerView recyclerView;
    IssuesAdapter issuesAdapter;
    List<Issues> issuesList;
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

        callIssuesAPI(repository.owner.login, repository.name);

        recyclerView = findViewById(R.id.detail_rvListIssues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        issuesAdapter = new IssuesAdapter(issuesList, this, repository);
        recyclerView.setAdapter(issuesAdapter);


    }

    private void callIssuesAPI(String name, String reponame) {

        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);
        Call<List<Issues>> listCall = service.listIssues(name, reponame);
        listCall.enqueue(new Callback<List<Issues>>() {
            @Override
            public void onResponse(Call<List<Issues>> call, Response<List<Issues>> response) {
                if (response.body() != null) {
                    issuesAdapter.setIssues(response.body());
                    issuesList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Issues>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}