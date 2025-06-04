package com.skeeper.minicode.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProjectsFragment extends Fragment {

    FragmentProjectsBinding binding;

    public List<ProjectModel> models = new ArrayList<>();

    @Inject
    ProjectManager projectManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        models = projectManager.loadAllProjectModels();
        setProjectsRecycler();

        binding.createProjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ProjectCreateActivity.class);
            intent.putExtra("fromGit", false);
            startActivity(intent);
        });

        binding.cloneFromGitButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ProjectCloneActivity.class);
            intent.putExtra("fromGit", true);
            startActivity(intent);
        });


    }

    private void setProjectsRecycler() {
        var recyclerView = binding.projectsRecyclerView;
        var adapter = new ProjectAdapter(requireActivity(), models);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                requireActivity(), RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}