package com.skeeper.minicode.presentation.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.presentation.ui.activity.ProjectOpenActivity;
import com.skeeper.minicode.R;
import com.skeeper.minicode.utils.helpers.animations.QuartInterpolations;
import com.skeeper.minicode.utils.helpers.animations.ViewAnimator;
import com.skeeper.minicode.utils.helpers.animations.ViewScaleComponent;
import com.skeeper.minicode.data.models.ProjectModelParcelable;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    Context context;
    List<ProjectModelParcelable> projects;


    public ProjectAdapter(Context context, List<ProjectModelParcelable> projects) {
        this.context = context;
        this.projects = projects;
    }



    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.project_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.projectNameView.setText(projects.get(position).getProjectName());
        holder.filepathView.setText(projects.get(position).getProjectPath());


        ProjectModelParcelable currentModel = projects.get(position);
        bindProjectVisuals(holder, currentModel);

        holder.parentRectView.setAlpha(0f);
        holder.parentRectView.animate()
                .alpha(1f)
                .setDuration(500)
                .start();

        holder.parentRectView.setOnClickListener(v -> {

            ViewScaleComponent.scaleView(holder.parentRectView, 110, 0.7f, 0.7f,
                    () -> ViewScaleComponent.scaleView(holder.parentRectView, 150, 1f, 1f, () -> {
                        try{
                            bindProjectOnClickListener(holder, currentModel);
                        }
                        catch (Exception e) {
                            Log.e("OPEN_EXC", e.getMessage());
                            Toast.makeText(context, "Failed to open!", Toast.LENGTH_SHORT).show();
                        }

                            },
                            QuartInterpolations::quartIn),
                    QuartInterpolations::quartOut);


        });

    }


    private void bindProjectVisuals(@NonNull ProjectViewHolder holder, ProjectModelParcelable currentModel) {
        holder.projectNameView.setText(currentModel.getProjectName());
        holder.filepathView.setText(currentModel.getProjectPath());

        holder.parentRectView.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor(currentModel.getMainRectColorHex())));
        holder.filepathView.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor(currentModel.getInnerRectColorHex())));
    }


    private void bindProjectOnClickListener(@NonNull ProjectViewHolder holder, ProjectModelParcelable currentModel) {

        var intent = new Intent(holder.parentRectView.getContext(), ProjectOpenActivity.class);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                (Activity) context,
                new Pair<View, String>(holder.parentRectView, "projectTransition")
        );

        intent.putExtra("projectModel", currentModel);
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
