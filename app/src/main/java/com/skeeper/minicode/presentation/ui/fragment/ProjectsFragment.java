package com.skeeper.minicode.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.adapters.ProjectAdapter;
import com.skeeper.minicode.databinding.FragmentProjectsBinding;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.presentation.ui.activity.ProjectCloneActivity;
import com.skeeper.minicode.presentation.ui.activity.ProjectCreateActivity;
import com.skeeper.minicode.presentation.viewmodels.ProjectsListViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

//@AndroidEntryPoint
public class ProjectsFragment extends Fragment {

    FragmentProjectsBinding binding;
    ProjectsListViewModel projectsListViewModel;

    public List<ProjectModel> models = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectsListViewModel = new ViewModelProvider(
                this).get(ProjectsListViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addProjectsData(models);
        setProjectsRecycler();

        binding.createProjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProjectCreateActivity.class);
            intent.putExtra("fromGit", false);
            startActivity(intent);
        });

        binding.cloneFromGitButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProjectCloneActivity.class);
            intent.putExtra("fromGit", true);
            startActivity(intent);
        });





    }







    private void addProjectsData(List<ProjectModel> models) {
        for (ProjectModel model : ProjectManager.loadAllProjectModels(getContext())) {
            models.add(model);
        }

    }
    private void setProjectsRecycler() {
        var recyclerView = binding.projectsRecyclerView;
        var adapter = new ProjectAdapter(getContext(), models);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}