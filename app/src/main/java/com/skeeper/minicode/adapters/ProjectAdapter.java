package com.skeeper.minicode.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.helpers.ProjectRectColorBindings;
import com.skeeper.minicode.models.ProjectModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    Context context;
    List<ProjectModel> projects; // сюда подаём модели, они инициализируются дальше


    public ProjectAdapter(Context context, List<ProjectModel> projects) {
        this.context = context;
        this.projects = projects;
    }



    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.project_item, parent, false);
        // context - стр view, где будет всё отображаться
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) { // подст полей/значений
        holder.projectNameView.setText(projects.get(position).getProjectName());
        holder.filepathView.setText(projects.get(position).getProjectPath());

        Toast.makeText(context, "создан", Toast.LENGTH_LONG).show();

        var colorBindings = ProjectRectColorBindings.bindingsList;
        Collections.shuffle(colorBindings);
        var randomBinding = colorBindings.get(0);

        // background main
        holder.parentRectView.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor(randomBinding.mainRectColor)));

        // background inner
        holder.filepathView.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor(randomBinding.innerRectColor)));

    }
    @Override
    public int getItemCount() {
        return projects.size();
    }


    public static final class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView filepathView;
        TextView projectNameView;

        View parentRectView;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            filepathView = itemView.findViewById(R.id.projectFilepath);
            projectNameView = itemView.findViewById(R.id.projectTitle);
            parentRectView = itemView.findViewById(R.id.parentRectView);
        }
    }

}
