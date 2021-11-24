package com.example.documentsapi.ui.detailissues;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documentsapi.R;
import com.example.documentsapi.api.GitHubService;
import com.example.documentsapi.api.RetrofitClient;
import com.example.documentsapi.model.Issues;
import com.example.documentsapi.model.IssuesComment;
import com.example.documentsapi.model.Repository;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailIssuesActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvDescription;
    ImageView imgLogo;
    RecyclerView recyclerView;
    Issues issues;
    Repository repository;
    DetailIssuesAdapter detailIssuesAdapter;
    List<IssuesComment> issuesCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_issues);

        tvName = findViewById(R.id.detail_issues_tvName);
        tvDescription = findViewById(R.id.detail_issues_tvDescription);
        imgLogo = findViewById(R.id.detail_issues_imgLogo);
        recyclerView = findViewById(R.id.detail_issues_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailIssuesAdapter = new DetailIssuesAdapter(issuesCommentList, this);
        recyclerView.setAdapter(detailIssuesAdapter);

        Bundle bundle = getIntent().getExtras();
        issues = (Issues) bundle.getSerializable("issues");
        repository = (Repository) bundle.getSerializable("repository");
        tvName.setText(issues.user.login);
        tvDescription.setText(issues.title);
        Picasso.get().load(issues.user.avatar_url).into(imgLogo);

        //callIssuesDetailAPI(repository.owner.login, repository.name, issues.number);
        callIssuesDetailAPI("octocat", "hello-world", 42);
    }

    private void callIssuesDetailAPI(String owner, String repo, int issues_numner) {

        GitHubService service = RetrofitClient.getClient().create(GitHubService.class);
        Call<List<IssuesComment>> listCall = service.listIssueComment(owner, repo, issues_numner);
        listCall.enqueue(new Callback<List<IssuesComment>>() {
            @Override
            public void onResponse(Call<List<IssuesComment>> call, Response<List<IssuesComment>> response) {
                if (response.body() != null) {
                    detailIssuesAdapter.setDetailIssues(response.body());
                    issuesCommentList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<IssuesComment>> call, Throwable t) {

            }
        });
    }
}