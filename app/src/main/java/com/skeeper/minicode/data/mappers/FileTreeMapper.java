package com.skeeper.minicode.data.mappers;

import com.skeeper.minicode.domain.models.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTreeMapper {
    public List<FileItem> buildFileTree(File directory, int level) {
        List<FileItem> items = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                FileItem item = new FileItem(file, file.getName(), file.isDirectory(), level);
                if (file.isDirectory()) {
                    item.getChildren().addAll(buildFileTree(file, level + 1));
                }
                items.add(item);
            }
        }
        return items;
    }
}
