package com.skeeper.minicode.domain.contracts.other;

import java.io.File;

import dagger.Provides;

public interface IFileDirectoryProvider {
    File getFilesDir();
}
