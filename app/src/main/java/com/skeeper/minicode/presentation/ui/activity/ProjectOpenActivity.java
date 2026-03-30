package com.skeeper.minicode.presentation.ui.activity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;
import com.skeeper.minicode.core.constants.ProjectTags;
import com.skeeper.minicode.data.sources.preferences.UserPreferencesProvider;
import com.skeeper.minicode.databinding.ActivityProjectOpenViewBinding;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.presentation.viewmodels.GitManageViewModel;
import com.skeeper.minicode.presentation.viewmodels.GitPullViewModel;
import com.skeeper.minicode.presentation.viewmodels.ProjectsViewModel;
import com.skeeper.minicode.presentation.viewmodels.SecurePrefViewModel;
import com.skeeper.minicode.presentation.viewmodels.TagViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProjectOpenActivity extends AppCompatActivity {



    private ActivityProjectOpenViewBinding binding;
    private ProjectsViewModel projectsViewModel;
    private GitManageViewModel gitManageViewModel;
    private SecurePrefViewModel securePrefViewModel;
    private GitPullViewModel gitPullViewModel;

    private TagViewModel tagViewModel;
    private ProjectModelParcelable boundModel = null;

    private List<String> repositoryBranches = new ArrayList<>();
    private String currentBranchName = null;
    private View currentBranchView = null;

    @Inject UserPreferencesProvider preferencesProvider;
    @Inject ProjectManager projectManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.activity_bottom));


        binding = ActivityProjectOpenViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initProjectViewByExtraArg();
        setupProjectStar();

        projectsViewModel = new ViewModelProvider(this).get(ProjectsViewModel.class);
        gitManageViewModel = new ViewModelProvider(this).get(GitManageViewModel.class);
        tagViewModel = new ViewModelProvider(this).get(TagViewModel.class);
        securePrefViewModel = new ViewModelProvider(this).get(SecurePrefViewModel.class);
        gitPullViewModel = new ViewModelProvider(this).get(GitPullViewModel.class);



        tagViewModel.getTags().observe(this, this::initTagViews);
        tagViewModel.loadProjectTags(boundModel.getProjectName());


        if (Arrays.asList(boundModel.getTags()).contains("git")) {
            gitManageViewModel.getProjectBranches().observe(this, data -> {
                repositoryBranches = data;
            });
            gitManageViewModel.getCurrentBranch().observe(this, data -> {
                currentBranchName = data;
                binding.currentBranchText.setText(currentBranchName != null
                        ? "Current branch: " + currentBranchName
                        : "Current branch: " + "Error: Can't load"
                );
            });
            gitManageViewModel.getBranchSetResult().observe(this, result -> {
                if (result == null) return;
                Toast.makeText(this, (result.currentBranch != null)
                        ? "Checkout successful"
                        : "Error: " + result.errorMessage, Toast.LENGTH_SHORT
                ).show();
            });
            gitManageViewModel.loadProjectBranches(boundModel.getProjectName());
            gitManageViewModel.loadCurrentBranch(boundModel.getProjectName());

            securePrefViewModel.getUsername().observe(this, username -> {
                if (username != null) gitPullViewModel.setUsername(username); });
            securePrefViewModel.getToken().observe(this, token -> {
                if (token != null) gitPullViewModel.setToken(token); });

            gitPullViewModel.getPullResult().observe(this, result -> {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            });

            securePrefViewModel.loadUsername();
            securePrefViewModel.loadToken();
        }


        binding.projectOpenButton.setOnClickListener(v -> {
            var intent = new Intent(
                    ProjectOpenActivity.this,
                    CodeEditorActivity.class
            );
            intent.putExtra("projectName", boundModel.getProjectName());
            Log.e("TRANSITION", "to code editor");
            preferencesProvider.setRecentProjectName(boundModel.getProjectName());
            startActivity(intent);
        });
        binding.buttonPanelRemove.setOnClickListener(v -> {
            projectManager.deleteProject(boundModel.getProjectName());
            startActivity(new Intent(ProjectOpenActivity.this, MenuActivity.class));
            finish();
        });
        binding.buttonPanelEditName.setOnClickListener(v -> {
            Toast.makeText(this, "in Development...", Toast.LENGTH_SHORT).show();
            //showRenamePopup();
        });
        binding.buttonPanelStar.setOnClickListener(v -> {
            updateStarButton();
        });
        binding.commitAndPushButton.setOnClickListener(v -> {
            var intent = new Intent(ProjectOpenActivity.this, ProjectPushActivity.class);
            intent.putExtra("PROJECT_NAME", boundModel.getProjectName());
            startActivity(intent);
        });
        binding.setBranchButton.setOnClickListener(v -> {
            if (currentBranchName != null)
                showBranchSetDialog(repositoryBranches, currentBranchName);
            else Toast.makeText(this, "Can't set branch",
                    Toast.LENGTH_SHORT).show();
        });
        binding.gitPullButton.setOnClickListener(v -> {
            gitPullViewModel.makeGitPull(boundModel.getProjectPath());
        });


        if (!Arrays.asList(boundModel.getTags()).contains("git"))
        {
            binding.gitPullButton.setVisibility(GONE);
            binding.commitAndPushButton.setVisibility(GONE);
            binding.setBranchButton.setVisibility(GONE);
            binding.currentBranchTextCard.setVisibility(GONE);
        }
    }

    private void setupProjectStar() {
        var list = new ArrayList<>(Arrays.asList(projectManager.loadProjectModel(
                boundModel.getProjectName()).tags()));
        boolean starred = list.contains(ProjectTags.PROJECT_STARRED);
        var starImage = binding.starImage;
        int color = starred ? R.color.yellow_saturated : R.color.blue_grey;
        starImage.setColorFilter(ContextCompat.getColor(this, color));
        findViewById(R.id.projectPanelStar).setVisibility(
                starred ? VISIBLE : INVISIBLE);
    }

    private void initProjectViewByExtraArg() {

        boundModel = (ProjectModelParcelable) getIntent().getParcelableExtra("projectModel");

        if (boundModel == null) {
            Toast.makeText(this, "Failed to open project: empty projectModel", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String projectFilepath = boundModel.getProjectPath();
        String projectName = boundModel.getProjectName();
        String mainRectColor = boundModel.getMainRectColorHex();
        String innerRectColor = boundModel.getInnerRectColorHex();
        binding.projectCard.setMainRectColor(Color.parseColor(mainRectColor));
        binding.projectCard.setInnerRectColor(Color.parseColor(innerRectColor));
        binding.projectCard.setProjectName(projectName);
        binding.projectCard.setProjectFilepathText(projectFilepath);

        if (!boundModel.getProjectDescription().isEmpty()) {
            binding.projectDescriptonText.setText(boundModel.getProjectDescription());
        }
    }

    private void showRenamePopup() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rename_file, null);

        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        TextInputEditText input = dialogView.findViewById(R.id.newNameTextEdit);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();

            if (projectManager.projectExists(newName)) {
                input.setError("Project with this name already exists!");
                return;
            }
            projectsViewModel.renameProject(boundModel.getProjectName(), newName);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    private void showBranchSetDialog(List<String> branches, String currentBranch) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_set_branch, null);

        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        LinearLayout container = dialogView.findViewById(R.id.branchesContainer);

        var builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        var branchViews = new ArrayList<View>();

        for (String name : branches) {
            Log.e("BRANCH_UI", "add view: " + name);
            LayoutInflater branchInflater = LayoutInflater.from(this);
            View branchView = branchInflater.inflate(R.layout.branch,
                    container, false);
            TextView branchText = branchView.findViewById(R.id.branchText);
            branchText.setText(name);
            branchViews.add(branchView);



            if (Objects.equals(name, currentBranch))
            {
                animateBranch(branchView, true);
                currentBranchView = branchView;
            }

            var params = new LinearLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.MATCH_PARENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            params.bottomMargin = 16;
            branchView.setLayoutParams(params);
            container.addView(branchView);

            branchView.setOnClickListener(v -> {
                if (currentBranchView == null) return;
                animateBranch(currentBranchView, false);
                currentBranchView = branchView; // handle br view change
                //currentBranchName = name; // anyway not be updated
                animateBranch(currentBranchView, true);
            });
        }


        positiveButton.setOnClickListener(v -> {
         // todo refactor (bad practice) ?????
            var selectedBranchName = ((TextView) currentBranchView
                    .findViewById(R.id.branchText)).getText().toString();
            gitManageViewModel.setRepoBranch(
                    boundModel.getProjectName(),
                    selectedBranchName
            );
            dialog.dismiss();
            currentBranchView = null;
        });
        negativeButton.setOnClickListener(v -> {
            dialog.dismiss();
            currentBranchView = null;
        });
        dialog.show();
    }
    private void updateStarButton() {
        var list = new ArrayList<>(Arrays.asList(
                projectManager.loadProjectModel(boundModel.getProjectName()).tags()
        ));
        boolean starred = list.contains(ProjectTags.PROJECT_STARRED);
        var starImage = binding.starImage;
        int color = starred ? R.color.blue_grey : R.color.yellow_saturated;
        starImage.setColorFilter(ContextCompat.getColor(this, color));

        if (starred) {
            list.remove(ProjectTags.PROJECT_STARRED);
        } else {
            list.add(ProjectTags.PROJECT_STARRED);
        }


        tagViewModel.saveProjectTags(
                list.toArray(new String[0]),
                boundModel.getProjectName()
        );

    }
    private void addViewToFlexbox(View view, int marginRight) {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, marginRight, 8);
        view.setLayoutParams(params);
        binding.tagFlexbox.addView(view);
    }
    private void initTagViews(List<String> items) {
        int[] colors = {R.color.green_light, R.color.blue_ultra,
                R.color.orange_light, R.color.pink};
        Random random = new Random();
        binding.tagFlexbox.removeAllViews();


        LayoutInflater _inflater = LayoutInflater.from(this);
        View tagTitleView = _inflater.inflate(R.layout.tag_title,
                binding.tagFlexbox, false);
        addViewToFlexbox(tagTitleView, 9);

        for (String tag : items) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View tagView = inflater.inflate(R.layout.tag,
                    binding.tagFlexbox, false);
            TextView tagText = tagView.findViewById(R.id.tagText);
            ImageView tagImage = tagView.findViewById(R.id.tagCircleImage);

            int randIdx = random.nextInt(colors.length);
            int randColor = colors[randIdx];

            tagImage.setColorFilter(ContextCompat.getColor(this, randColor));
            tagText.setText(tag);
            addViewToFlexbox(tagView, 9);
        }

    }

    private void animateBranch(View branchView, boolean select) {
        int fromColor = ContextCompat.getColor(
                this,
                select ? R.color.violet_light : R.color.pale_blue
        );
        int toColor = ContextCompat.getColor(
                this,
                select ? R.color.pale_blue : R.color.violet_light
        );

        ValueAnimator colorAnimator = ValueAnimator.ofArgb(fromColor, toColor);
        colorAnimator.setDuration(220);
        colorAnimator.setInterpolator(new DecelerateInterpolator());

        colorAnimator.addUpdateListener(animator -> {
            int animatedColor = (int) animator.getAnimatedValue();
            branchView.setBackgroundTintList(ColorStateList.valueOf(animatedColor));
        });

        colorAnimator.start();

        ImageView checkView = branchView.findViewById(R.id.branchSelectedCheckmark);
        checkView.setVisibility(select ? VISIBLE : INVISIBLE);
    }


}