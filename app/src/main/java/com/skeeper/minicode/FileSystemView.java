package com.skeeper.minicode;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.adapters.FileTreeAdapter;
import com.skeeper.minicode.models.FileItem;
import com.skeeper.minicode.singleton.ProjectManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSystemView extends RelativeLayout {

    public RecyclerView filesRecyclerView;
    public File directory;
    List<FileItem> fileItems;

    public FileSystemView(Context context) {
        super(context);
    }

    public void init(Context context, RelativeLayout parent, File directory) {
        View view = LayoutInflater.from(context).inflate(R.layout.filesystem_panel, parent, true);
        view.setBackgroundColor(Color.TRANSPARENT);


        filesRecyclerView = view.findViewById(R.id.recycler_view);
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.directory = directory;
        fileItems = buildFileTree(directory, 1);
        filesRecyclerView.setAdapter(new FileTreeAdapter(fileItems));

    }


    private List<FileItem> buildFileTree(File directory, int level) {
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
