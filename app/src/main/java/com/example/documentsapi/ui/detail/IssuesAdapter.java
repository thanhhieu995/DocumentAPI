package com.example.documentsapi.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.documentsapi.R;
import com.example.documentsapi.model.Issues;
import com.example.documentsapi.model.Repository;
import com.example.documentsapi.ui.detailissues.DetailIssuesActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder> {

    List<Issues> issuesList;
    Context context;
    Repository repository;

    public IssuesAdapter(List<Issues> issuesList, Context context, Repository repository) {
        this.issuesList = issuesList;
        this.context = context;
        this.repository = repository;
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
            holder.tvDescription.setText(issues.title);
            Picasso.get().load(issues.user.avatar_url).into(holder.imageLogo);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailIssuesActivity.class);
                    intent.putExtra("issues", issues);
                    intent.putExtra("repository", repository);
                    context.startActivity(intent);
                }
            });
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

    public void clearData() {
        this.issuesList = null;
        notifyDataSetChanged();
    }

    public void addIssues(List<Issues> body) {
        if (this.issuesList != null) {
            issuesList.addAll(body);
        } else {
            this.issuesList = body;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        ImageView imageLogo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.item_issues_description);
            imageLogo = itemView.findViewById(R.id.item_Issues_imgLogo);
        }
    }
}
