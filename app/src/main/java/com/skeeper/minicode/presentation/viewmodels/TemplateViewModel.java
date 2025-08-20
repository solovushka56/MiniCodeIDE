package com.skeeper.minicode.presentation.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.skeeper.minicode.R;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.enums.TemplateType;

import java.io.File;
import java.io.FileWriter;

import javax.inject.Inject;



public class TemplateViewModel extends AndroidViewModel {

    private final MutableLiveData<String> fileCreationStatus = new MutableLiveData<>();

    @Inject
    ProjectManager projectManager;


    public TemplateViewModel(@NonNull Application application) {
        super(application);
    }

    public void createTemplate(TemplateType type, File path) {
        new Thread(() -> {
            try {
                String content = getTemplateContent(type);
                String fileName = generateFileName(type);
                File file = createFile(path, fileName, content);

                fileCreationStatus.setValue("File created: " + file.getAbsolutePath());
            } catch (Exception e) {
                fileCreationStatus.setValue("Error: " + e.getMessage());
            }
        }).start();
    }

    private String getTemplateContent(TemplateType type) {
        int resId;
        if (type == TemplateType.JAVA) {
            resId = R.string.java_template;
        } else {
            resId = R.string.python_template;
        }
        return getApplication().getString(resId);
    }

    private String generateFileName(TemplateType type) {
        String baseName = "main";
        return (type == TemplateType.JAVA)
                ? baseName + ".java"
                : baseName + ".py";
    }

    private File createFile(File dir, String fileName, String content) throws Exception {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new Exception("Failed to create directory");
        }

        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }

    public MutableLiveData<String> getFileCreationStatus() {
        return fileCreationStatus;
    }
}