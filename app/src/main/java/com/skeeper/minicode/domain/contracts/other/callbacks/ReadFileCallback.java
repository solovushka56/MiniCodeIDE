package com.skeeper.minicode.domain.contracts.other.callbacks;

public interface ReadFileCallback {
    void onFinish(String content, boolean success);
}