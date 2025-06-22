package com.skeeper.minicode.domain.usecases.project.management;

import android.app.Application;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.enums.TemplateType;

import java.io.File;
import java.io.IOException;

public class CreateTemplateUseCase {
    private final IProjectRepository projectRepository;

    public CreateTemplateUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public void execute(Application application,
                        String projName,
                        TemplateType tempType) throws IOException {

        if (tempType == TemplateType.NONE)
            return;

        String fullFileName = (tempType == TemplateType.JAVA)
                ? "Main" + ".java"
                : "main" + ".py";

        File projectRootDirectory = projectRepository
                .getOperations()
                .getProjectDir(projName);

        projectRepository.getOperations().saveFile(
                projectRootDirectory,
                fullFileName,
                getTemplateContent(tempType, application));
    }


    private String getTemplateContent(TemplateType type, Application application) {
        int resId;
        if (type == TemplateType.JAVA) {
            resId = R.string.java_template;
        } else {
            resId = R.string.python_template;
        }
        return application
                .getString(resId)
                .replace("\t", "   ");
    }
}
