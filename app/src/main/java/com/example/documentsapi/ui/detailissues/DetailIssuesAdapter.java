package com.example.documentsapi.ui.detailissues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.IssuesComment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailIssuesAdapter extends RecyclerView.Adapter<DetailIssuesAdapter.ViewHolder> {

    List<IssuesComment> issuesCommentList;
    Context context;

    public DetailIssuesAdapter(List<IssuesComment> issuesCommentList, Context context) {
        this.issuesCommentList = issuesCommentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_detail_issues, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IssuesComment issuesComment = issuesCommentList.get(position);
        if (issuesComment != null) {
            holder.tvName.setText(issuesComment.node_id);
            Picasso.get().load(issuesComment.owner.avatar_url).into(holder.imgLogo);
        }
    }

    @Override
    public int getItemCount() {
        if (issuesCommentList != null) {
            return issuesCommentList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgLogo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_detailissues_tvName);
            imgLogo = itemView.findViewById(R.id.item_detaiissues_imgLogo);
        }
    }
}
