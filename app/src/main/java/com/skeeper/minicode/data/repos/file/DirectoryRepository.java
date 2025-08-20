package com.skeeper.minicode.data.repos.file;

import com.skeeper.minicode.domain.contracts.repos.file.IDirectoryRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryRepository implements IDirectoryRepository {
    @Override
    public List<File> getAllFiles(String directory) {
        return getAllFilesInDir(new File(directory));
    }

    @Override
    public Map<String, String> getFilesContentMap(String rootDir) {
        return getFilesContent(new File(rootDir));
    }


    public static List<String> getAllFilesNamesInDir(String directory) {
        var filesList = getAllFilesInDir(new File(directory));
        var namesList = new ArrayList<String>();
        for (File file : filesList) {
            namesList.add(file.getName());
        }
        return namesList;
    }

    private static List<File> getAllFilesInDir(File directory) {
        List<File> fileList = new ArrayList<>();
        if (directory == null || !directory.exists() || !directory.isDirectory())
            return fileList;

        File[] files = directory.listFiles();
        if (files == null) return fileList;

        for (File file : files) {
            if (file.isFile()) fileList.add(file);
            else if (file.isDirectory()) fileList.addAll(getAllFilesInDir(file));
        }
        return fileList;
    }


    public static Map<String, String> getFilesContent(File rootDir) {
        Map<String, String> resultMap = new HashMap<>();
        List<File> allFiles = getAllFilesInDir(rootDir);

        for (File file : allFiles) {
            if (!file.isFile()) continue;

            try {
                String content = new String(
                        Files.readAllBytes(file.toPath()),
                        StandardCharsets.UTF_8
                );
                resultMap.put(file.getName(), content);
            }
            catch (IOException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }
}
