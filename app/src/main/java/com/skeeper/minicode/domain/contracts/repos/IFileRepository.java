package com.skeeper.minicode.domain.contracts.repos;

import java.io.File;

public interface IFileRepository {
    String loadFileText();
    void saveFileText();

}
