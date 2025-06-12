package com.skeeper.minicode.presentation.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.enums.TemplateType;

import java.io.File;
import java.io.FileWriter;

public class TemplateViewModel extends AndroidViewModel {

    private final MutableLiveData<String> fileCreationStatus = new MutableLiveData<>();

    public TemplateViewModel(@NonNull Application application) {
        super(application);
    }

    public void createTemplate(TemplateType type) {
        new Thread(() -> {
            try {
                String content = getTemplateContent(type);
                String fileName = generateFileName(type);
                File file = createFile(fileName, content);

                fileCreationStatus.postValue("File created: " + file.getAbsolutePath());
            } catch (Exception e) {
                fileCreationStatus.postValue("Error: " + e.getMessage());
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
        String baseName = "Template_" + System.currentTimeMillis();
        return (type == TemplateType.JAVA)
                ? baseName + ".java"
                : baseName + ".py";
    }

    private File createFile(String fileName, String content) throws Exception {
        File templatesDir = new File(getApplication().getFilesDir(), "templates");
        if (!templatesDir.exists() && !templatesDir.mkdirs()) {
            throw new Exception("Failed to create directory");
        }

        File file = new File(templatesDir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }

    public MutableLiveData<String> getFileCreationStatus() {
        return fileCreationStatus;
    }
}