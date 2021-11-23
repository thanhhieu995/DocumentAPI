package com.example.documentsapi.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Issues;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder> {

    List<Issues> issuesList;
    Context context;

    public IssuesAdapter(List<Issues> issues, Context context) {
        this.issuesList = issues;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_issues, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Issues issues = issuesList.get(position);
        if (issues != null) {
            holder.tvName.setText(issues.node_id);
            holder.tvDescription.setText(issues.title);
            Picasso.get().load(issues.user.avatar_url).into(holder.imageLogo);
        }

    }

    @Override
    public int getItemCount() {
        if (issuesList != null) {
            return issuesList.size();
        }
        return 0;
    }

    public void setIssues(List<Issues> issuesList) {
        this.issuesList = issuesList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDescription;
        ImageView imageLogo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_issues_name);
            tvDescription = itemView.findViewById(R.id.item_issues_description);
            imageLogo = itemView.findViewById(R.id.item_Issues_imgLogo);
        }
    }
}
