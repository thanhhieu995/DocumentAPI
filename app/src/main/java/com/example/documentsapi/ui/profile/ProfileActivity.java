package com.example.documentsapi.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.main.RepositoryAdapter;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgLogo;
    RecyclerView recyclerView;
    RepositoryAdapter repositoryAdapter;
    List<Repository> repositories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgLogo = findViewById(R.id.imgLogo);
        recyclerView = findViewById(R.id.rv_recyclerView_Profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repositoryAdapter = new RepositoryAdapter(repositories, this);
        recyclerView.setAdapter(repositoryAdapter);
    }
}