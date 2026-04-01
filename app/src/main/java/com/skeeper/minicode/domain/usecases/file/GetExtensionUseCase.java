package com.skeeper.minicode.domain.usecases.file;

import androidx.annotation.NonNull;

import com.skeeper.minicode.domain.enums.EditorLang;

import java.io.File;
import java.util.Locale;

public class GetExtensionUseCase {

    public EditorLang execute(@NonNull File file) {
        if (file.isDirectory())
            return EditorLang.OTHER;


        String fileName = file.getName();
        int lastDotIdx = fileName.lastIndexOf('.');
        if (lastDotIdx == -1 || lastDotIdx == fileName.length() - 1) {
            return EditorLang.OTHER;
        }

        String extension = fileName
                .substring(lastDotIdx + 1)
                .toLowerCase(Locale.ROOT);

        return switch (extension) {
            case "java" -> EditorLang.JAVA;
            case "py" -> EditorLang.PYTHON;
            case "xml" -> EditorLang.XML;
            case "html" -> EditorLang.HTML;
            default -> EditorLang.OTHER;
        };
    }
}