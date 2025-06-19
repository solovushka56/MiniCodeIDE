package com.skeeper.minicode.domain.contracts.operations;

import com.skeeper.minicode.data.models.ProjectModelParcelable;

import java.io.File;
import java.io.IOException;

public interface IProjectOperations {
    File getProjectsStoreFolder();
    File getProjectDir(String projectName);
    File getProjectConfigFile(String projectName);
    void generateMetadata(File projectDir, ProjectModelParcelable model) throws IOException;
    boolean createDirectory(File dir);
    boolean deleteRecursive(File fileOrDirectory);
    boolean rename(File source, File target);

    String readFile(File file) throws IOException;
    void saveFile(File projectDir,
                  String fileName, String content) throws IOException;

    boolean exists(File file);
    File[] listChildDirectories(File dir);
}