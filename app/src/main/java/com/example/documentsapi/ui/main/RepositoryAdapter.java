package com.example.documentsapi.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Repository;

import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder>{
    List<Repository> repos;
    Context context;

    public RepositoryAdapter(Context context, List<Repository> repos) {
        this.context = context;
        this.repos = repos;
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
        Repository repo = repos.get(position);
        if (repo != null) {
            holder.tvName.setText(repo.full_name);
            holder.tvDescription.setText(repo.description);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        ImageView imgUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.repository_tvName);
            tvDescription = itemView.findViewById(R.id.repository_tvDescription);
            imgUser = itemView.findViewById(R.id.repository_imgUser);
        }
    }
}
