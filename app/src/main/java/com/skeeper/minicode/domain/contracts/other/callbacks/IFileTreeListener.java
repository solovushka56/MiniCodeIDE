package com.skeeper.minicode.domain.contracts.other.callbacks;

import com.skeeper.minicode.domain.models.FileItem;

import java.io.File;

public interface IFileTreeListener {
    void onFileClick(FileItem fileItem);
    void onFolderClick(FileItem fileItem);
    void onFolderExpandedStateChanged(File folder, boolean isExpanded);
    void onRenameSelected(FileItem item);
    void onDeleteSelected(FileItem item);
    void onCopyPathSelected(FileItem item);
    void onMoveSelected(FileItem item);
    void onAddFileSelected(FileItem item);

}
