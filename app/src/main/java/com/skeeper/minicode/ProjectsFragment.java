package com.skeeper.minicode;

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
import android.widget.Toast;

import com.skeeper.minicode.adapters.ProjectAdapter;
import com.skeeper.minicode.databinding.FragmentProjectsBinding;
import com.skeeper.minicode.models.ProjectModel;
import com.skeeper.minicode.singleton.ProjectManager;

import java.util.ArrayList;
import java.util.List;


public class ProjectsFragment extends Fragment {

    FragmentProjectsBinding binding;


    public List<ProjectModel> models = new ArrayList<>();


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

        addProjectsData(models);
        setProjectsRecycler();

        binding.createProjectButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProjectCreateActivity.class);
            intent.putExtra("fromGit", false);
            startActivity(intent);
        });

        binding.cloneFromGitButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProjectCreateActivity.class);
            intent.putExtra("fromGit", true);
            startActivity(intent);
        });


    }







    private void addProjectsData(List<ProjectModel> models) {
        // todo: сделать здесь чтение существующих проектов из директории
        for (ProjectModel model : ProjectManager.loadAllProjectModels(getContext())) {
            models.add(model);
        }


//        Toast.makeText(
//                getContext(),
//                ProjectManager
//                        .getProjectConfigDir(getContext(), "xdd")
//                        .toString(),
//                Toast.LENGTH_LONG).show();



        models.add(new ProjectModel(1, "tutorial", "/0/sil/bindssd/intsaf/boor"));
        models.add(new ProjectModel(2, "startup", "/0/saf/boor"));
//        models.add(new ProjectModel(3, "startup2", "/0/sil/binintsaf/boor"));
//
//        models.add(new ProjectModel(1, "tutorial", "/0/sil/bindssd/intsaf/boor"));
//        models.add(new ProjectModel(2, "startup", "/0/saf/boor"));
//        models.add(new ProjectModel(3, "startup2", "/0/sil/binintsaf/boor"));
//
//        models.add(new ProjectModel(1, "tutorial", "/0/sil/bindssd/intsaf/boor"));
//        models.add(new ProjectModel(2, "startup", "/0/saf/boor"));
//        models.add(new ProjectModel(3, "startup2", "/0/sil/binintsaf/boor"));
//
//        models.add(new ProjectModel(1, "tutorial", "/0/sil/bindssd/intsaf/boor"));
//        models.add(new ProjectModel(2, "startup", "/0/saf/boor"));
//        models.add(new ProjectModel(3, "startup2", "/0/sil/binintsaf/boor"));

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