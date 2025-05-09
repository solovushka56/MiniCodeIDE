package com.skeeper.minicode.domain.enums;

public enum FileOpenMode {
    NEW, // if creating new file
    FROM_GIT, // if cloning and editing one from git
    LOCAL // if from local dir
}
