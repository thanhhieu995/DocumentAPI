package com.example.documentsapi.ui.detailissues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.IssuesComment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailIssuesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    List<IssuesComment> issuesCommentList;
    Context context;

    public DetailIssuesAdapter(List<IssuesComment> issuesCommentList, Context context) {
        this.issuesCommentList = issuesCommentList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_detailissues, parent, false);
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
            IssuesComment issuesComment = issuesCommentList.get(position);
            if (issuesComment != null) {
                ((ItemViewHolder) holder).tvName.setText(issuesComment.body);
                Picasso.get().load(issuesComment.user.avatar_url).into(((ItemViewHolder) holder).imgLogo);
            }
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (issuesCommentList != null) {
            return issuesCommentList.size() + 1;
        }
        return 0;
    }

    public void setDetailIssues(List<IssuesComment> body) {
        this.issuesCommentList = body;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgLogo;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_detailissues_tvName);
            imgLogo = itemView.findViewById(R.id.item_detaiissues_imgLogo);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public void showLoadingView (LoadingViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position >= issuesCommentList.size()) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }
}
