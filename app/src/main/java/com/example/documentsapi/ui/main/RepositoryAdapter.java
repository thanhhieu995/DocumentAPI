package com.example.documentsapi.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.detail.DetailActivity;
import com.example.documentsapi.ui.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder>{
    List<Repository> repos;
    Context context;

    public RepositoryAdapter(List<Repository> repos, Context context) {
        this.repos = repos;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList (ArrayList<Repository> filterList) {
        repos = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepositoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_repository, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryAdapter.ViewHolder holder, int position) {
        Repository repository = repos.get(position);
        if (repository != null) {
            holder.tvName.setText(repository.full_name);
            holder.tvDescription.setText(repository.description);
            Picasso.get().load(repository.owner.avatar_url).into(holder.imgUser);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "><><><><", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("repository",  repository);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (repos != null) {
            return repos.size();
        }
        return 0;
    }

    public void setRepos(List<Repository> repos) {
        this.repos = repos;
        notifyDataSetChanged();
    }

    public void addRepos(List<Repository> repos) {
        if (this.repos != null) {
            this.repos.addAll(repos);
        } else {
            this.repos = repos;
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        this.repos = null;
        notifyDataSetChanged();
    }

    public int largestId() {
        int maxId = 0;
        if (repos != null) {
            for (int i = 0; i < repos.size(); i++) {
                if (repos.get(i).id > maxId) {
                    maxId = repos.get(i).id;
                }
            }
        }
        return maxId;
//        if (repos != null && repos.size() > 0) {
//            Repository repo = repos.get(repos.size() -1);
//            if (repo != null) {
//                return repo.id;
//            }
//        }
//        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        ImageView imgUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.repository_tvName);
            tvDescription = itemView.findViewById(R.id.repository_tvDescription);
            imgUser = itemView.findViewById(R.id.repository_imgUser);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
