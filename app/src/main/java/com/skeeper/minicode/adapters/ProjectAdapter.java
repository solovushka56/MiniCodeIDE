package com.skeeper.minicode.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.SyncStateContract;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.ProjectOpenView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.helpers.ProjectRectColorBindings;
import com.skeeper.minicode.helpers.animations.BackInterpolations;
import com.skeeper.minicode.helpers.animations.QuartInterpolations;
import com.skeeper.minicode.helpers.animations.ViewScaleComponent;
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

    // вызывается каждый раз, даже когда мы пролистываем список и он начинает отображаться
    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) { // подст полей/значений
        holder.projectNameView.setText(projects.get(position).getProjectName());
        holder.filepathView.setText(projects.get(position).getProjectPath());


        ProjectModel currentModel = projects.get(position);

        // background main
        holder.parentRectView.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor(currentModel.getMainRectColorHex())));

        // background inner
        holder.filepathView.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor(currentModel.getInnerRectColorHex())));


        holder.parentRectView.setOnClickListener(v -> {

            ViewScaleComponent.scaleView(holder.parentRectView, 110, 0.7f, 0.7f,
                    () -> ViewScaleComponent.scaleView(holder.parentRectView, 150, 1f, 1f, () -> {
                                bindProjectOnClickListener(holder, currentModel);
                            },
                            input -> QuartInterpolations.quartIn(input)),
                    input -> QuartInterpolations.quartOut(input)
            );


        });

    }

    private void bindProjectOnClickListener(@NonNull ProjectViewHolder holder, ProjectModel currentModel) {
        var intent = new Intent(holder.parentRectView.getContext(), ProjectOpenView.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                (Activity) context,
                new Pair<View, String>(holder.parentRectView, "projectTransition")
        );


        intent.putExtra("id", currentModel.getId());
        intent.putExtra("projectName", currentModel.getProjectName());
        intent.putExtra("projectFilepath", currentModel.getProjectName());
        intent.putExtra("mainRectColor", currentModel.getMainRectColorHex());
        intent.putExtra("innerRectColor", currentModel.getInnerRectColorHex());

        context.startActivity(intent, options.toBundle());
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
