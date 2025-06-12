package com.skeeper.minicode.presentation.ui.other;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.adapters.FileTreeAdapter;
import com.skeeper.minicode.domain.contracts.other.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;

import java.io.File;
import java.util.List;

public class FileTreeView extends RelativeLayout {

    public RecyclerView filesRecyclerView;
    public ImageButton createFileButton;
    public ImageButton exitButton;

    public File directory;
    List<FileItem> fileItems;

    public FileTreeView(Context context) {
        super(context);
    }

    public void init(Context context, RelativeLayout parent, File directory) {
        View view = LayoutInflater.from(context).inflate(R.layout.filesystem_panel, parent, true);
        view.setBackgroundColor(Color.TRANSPARENT);

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        filesRecyclerView = view.findViewById(R.id.recycler_view);
        createFileButton = view.findViewById(R.id.createFileButton);
        exitButton = view.findViewById(R.id.panelCloseButton);

        filesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.directory = directory;

    }
    public void updateFileItems(Context context, List<FileItem> newFileItems) {
        fileItems = newFileItems;
        var changesListener = (context instanceof IFileTreeListener) ? context : null;
        filesRecyclerView.setAdapter(new FileTreeAdapter(fileItems, (IFileTreeListener) changesListener));

    }
    public void setOnCreateFileButtonListener(OnClickListener listener) {
        if (createFileButton != null) {
            createFileButton.setOnClickListener(listener);
        }
    }

    public void setOnClickCloseListener(OnClickListener listener) {
        if (exitButton != null) {
            exitButton.setOnClickListener(listener);
        }
    }

}
