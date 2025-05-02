package com.skeeper.minicode.domain.contracts.other.callbacks;

import java.io.File;

public interface FileCallback {
    void onFinish(File file, boolean success);
}