package com.skeeper.minicode.domain.contracts.repos.file;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface IDirectoryRepository {
    List<File> getAllFiles(String directory);
    Map<String, String> getFilesContentMap(String rootDir); // filename -> content

}
