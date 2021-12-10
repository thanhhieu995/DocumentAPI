package com.example.documentsapi.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Repository> repos;
    Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.item_repository, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.item_loading, parent, false);
            LoadingViewHolder viewHolder = new LoadingViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Repository repository = repos.get(position);
            if (repository != null) {
                ((ItemViewHolder) holder).tvName.setText(repository.full_name);
                ((ItemViewHolder) holder).tvDescription.setText(repository.description);
                Picasso.get().load(repository.owner.avatar_url).into(((ItemViewHolder) holder).imgUser);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "><><><><", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("repository", repository);
                        context.startActivity(intent);
                    }
                });
            }
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (repos != null) {
            return repos.size() + 1;
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
                if (repos.get(i) != null) {
                    if (repos.get(i).id > maxId) {
                        maxId = repos.get(i).id;
                    }
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        ImageView imgUser;
        public ItemViewHolder(@NonNull View itemView) {
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

    @Override
    public int getItemViewType(int position) {
        //return repos.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        if (position >= repos.size()) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
//        Repository repository = repos.get(position);
        //viewHolder.progressBar
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        Repository repository = repos.get(position);
        viewHolder.tvName.setText(repository.full_name);
        viewHolder.tvDescription.setText(repository.description);
        Picasso.get().load(repository.owner.avatar_url).into(viewHolder.imgUser);
    }
}
