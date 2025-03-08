package com.skeeper.minicode.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.models.ProjectModel;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    Context context;
    List<ProjectModel> projects;


    public ProjectAdapter(Context context, List<ProjectModel> projects) {
        this.context = context;
        this.projects = projects;
    }



    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return projects.size();
    }



    public static final class ProjectViewHolder extends RecyclerView.ViewHolder {
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
