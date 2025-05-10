package com.skeeper.minicode.utils;

import com.skeeper.minicode.domain.enums.ExtensionType;

import java.util.Arrays;

// ut for file extensions (.py, cpp etc)

public class ExtensionUtils {
    public static ExtensionType getFileExtensionType(String filename) {
        String extensionStr = "";
        char[] nameChars = filename.toCharArray();
        for (int idx = 0; idx < nameChars.length; idx++) {
            if (nameChars[idx] == '.') {
                extensionStr = String.valueOf(Arrays.copyOfRange(nameChars, nameChars[idx], nameChars.length - 1));
                break;
            }
        }
        switch (extensionStr)
        {
            case ".cs":
                return ExtensionType.CSHARP;
            case ".py":
                return ExtensionType.PYTHON;
            case ".java":
                return ExtensionType.JAVA;
            case ".txt":
                return ExtensionType.TEXT;
            default:
                return ExtensionType.OTHER;
        }
    }
}
