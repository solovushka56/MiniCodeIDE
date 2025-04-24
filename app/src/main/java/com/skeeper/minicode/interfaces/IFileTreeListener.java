package com.skeeper.minicode.interfaces;

import com.skeeper.minicode.models.FileItem;

import java.io.IOException;

public interface IFileTreeListener {
    void onFileClick(FileItem fileItem);
    void onFolderClick(FileItem fileItem);
    void onFolderAdd(FileItem fileItem);
    void onFileAdd(FileItem fileItem);
}
