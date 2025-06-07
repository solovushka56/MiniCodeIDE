package com.skeeper.minicode.domain.usecases.file;

import androidx.annotation.NonNull;

import com.skeeper.minicode.domain.enums.ExtensionType;

import java.io.File;
import java.util.Locale;

public class GetExtensionUseCase {

    public ExtensionType execute(@NonNull File file) {
        if (file.isDirectory())
            return ExtensionType.OTHER;


        String fileName = file.getName();
        int lastDotIdx = fileName.lastIndexOf('.');
        if (lastDotIdx == -1 || lastDotIdx == fileName.length() - 1) {
            return ExtensionType.OTHER;
        }

        String extension = fileName
                .substring(lastDotIdx + 1)
                .toLowerCase(Locale.ROOT);

        return switch (extension) {
            case "java" -> ExtensionType.JAVA;
            case "py" -> ExtensionType.PYTHON;
            case "cs" -> ExtensionType.CSHARP;
            default -> ExtensionType.OTHER;
        };
    }
}