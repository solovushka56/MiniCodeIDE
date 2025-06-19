package com.skeeper.minicode.presentation.ui.fragment;

import static android.view.View.VISIBLE;

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
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.presentation.ui.activity.ProjectCloneActivity;
import com.skeeper.minicode.presentation.ui.activity.ProjectCreateActivity;
import com.skeeper.minicode.presentation.viewmodels.ProjectsViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProjectsFragment extends Fragment {

    private FragmentProjectsBinding binding;
    private ProjectsViewModel projectsViewModel;

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


        projectsViewModel = new ViewModelProvider(this).get(ProjectsViewModel.class);

        projectsViewModel.getModels().observe(
                requireActivity(),
                models -> {
                    setProjectsRecycler(models);

                    if (models == null || models.isEmpty()) {
                        binding.tipView.setVisibility(VISIBLE);
                        binding.projectsRecyclerView.setVisibility(View.GONE);
                    }
                    else {
                        binding.tipView.setVisibility(View.GONE);
                        binding.projectsRecyclerView.setVisibility(VISIBLE);
                    }
                });

        projectsViewModel.loadModelsAsync();

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

    private void setProjectsRecycler(List<ProjectModelParcelable> models) {
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