package com.skeeper.minicode.domain.contracts.other;

import com.skeeper.minicode.domain.models.FileItem;

import java.io.File;

public interface IFileTreeListener {
    void onFileClick(FileItem fileItem);
    void onFolderClick(FileItem fileItem);
    void onFileRename(FileItem fileItem);
    void onFolderLongClick(FileItem fileItem);
    void onFileLongClick(FileItem fileItem);
    void onFolderAdd(FileItem fileItem);
    void onFileAdd(FileItem fileItem);
}
