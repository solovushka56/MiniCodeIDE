package com.skeeper.minicode.domain.contracts.other;

import com.skeeper.minicode.domain.models.FileItem;

public interface IFileTreeListener {
    void onFileClick(FileItem fileItem);
    void onFolderClick(FileItem fileItem);
    void onFolderAdd(FileItem fileItem);
    void onFileAdd(FileItem fileItem);
}
