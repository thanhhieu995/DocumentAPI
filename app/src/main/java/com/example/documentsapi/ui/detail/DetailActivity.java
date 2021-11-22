package com.example.documentsapi.ui.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtDescription;
    ImageView imgLogo;

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
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        Repository repository = (Repository) bundle.getSerializable("repository");
//        Repository repository = bundle.getParcelable("repo");
//        repository = bundle.getParcelable("full_name");
//        repository = bundle.getParcelable("description");
//        repository = bundle.getParcelable("avatar_url");
        txtName.setText(repository.full_name);
        txtDescription.setText(repository.description);
        Picasso.get().load(repository.owner.avatar_url).into(imgLogo);
//        txtName = bundle.getParcelable("full_name");
//        txtDescription = bundle.getParcelable("description");
//        imgLogo = bundle.getParcelable("avatar_url");
    }
}