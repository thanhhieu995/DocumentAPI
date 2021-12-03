package com.example.documentsapi.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.listener.EndlessRecyclerViewScrollListener;
import com.example.documentsapi.model.Issues;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtDescription;
    ImageView imgLogo;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    IssuesAdapter issuesAdapter;

    Repository repository;
    Issues issues;
    List<Issues> issuesList;
    List<Repository> repositories;

    private EndlessRecyclerViewScrollListener scrollListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtName = findViewById(R.id.detail_txtfullName);
        txtDescription = findViewById(R.id.detail_txtDescription);
        imgLogo = findViewById(R.id.detail_imgLogo);
        swipeRefreshLayout = findViewById(R.id.detail_swiperefreshlayout);
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

        callIssuesAPI(repository.owner.login, repository.name, 1);

        recyclerView = findViewById(R.id.detail_rvListIssues);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        issuesAdapter = new IssuesAdapter(issuesList, this, repository);
        recyclerView.setAdapter(issuesAdapter);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                issuesAdapter.clearData();
                callIssuesAPI(repository.owner.login, repository.name, 1);
            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callIssuesAPI(repository.owner.login, repository.name, page + 1);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        Log.d("Hieu", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Hieu", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Hieu", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Hieu", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Hieu", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Hieu", "onDestroy");
    }

    private void callIssuesAPI(String name, String reponame, int page) {

        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);
        Call<List<Issues>> listCall = service.listIssuesEndScroll(name, reponame, page, 10);
        listCall.enqueue(new Callback<List<Issues>>() {
            @Override
            public void onResponse(Call<List<Issues>> call, Response<List<Issues>> response) {
                if (response.body() != null) {
                    if (page == 1) {
                        swipeRefreshLayout.setRefreshing(false);
                        issuesAdapter.setIssues(response.body());
                        issuesList = response.body();
                    } else {
                        issuesAdapter.addIssues(response.body());
                    }
                }
                Log.d("Hieu", "onResponse call Issue Api");
            }

            @Override
            public void onFailure(Call<List<Issues>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Log.d("Hieu", "After call Issue Api");
    }
}