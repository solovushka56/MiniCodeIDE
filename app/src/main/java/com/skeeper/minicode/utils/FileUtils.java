package com.skeeper.minicode.utils;

import com.skeeper.minicode.domain.enums.ExtensionType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


public class FileUtils {

    public static boolean createFolder(File directory, String name) {
        boolean success;

        File folder = new File(directory, name);
        if (!folder.exists())
            success = folder.mkdir();
        else
            success = false;

        return success;
    }

    public static boolean createFile(File file) {
        boolean success;
        try {
            if (file.exists()) throw new IOException();

            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException();
            }

            if (!file.createNewFile()) {
                throw new IOException();
            }
            success = true;

        } catch (Exception e) {
            success = false;
        }
        return success;
    }
    public static boolean deleteFile(File file) {
        boolean success;
        try {
            if (!file.exists()) throw new FileNotFoundException();
            deleteRecursive(file);
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }
    public static boolean renameFile(File source, String newName) {
        boolean success;
        try {
            File target = new File(source.getParent(), newName);
            if (target.exists()) throw new IOException();

            if (!source.renameTo(target)) {
                performMoveOperation(source, target);
            }
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }
    public static boolean moveFile(File source, File targetDir) {
        boolean success;
        try {
            File target = new File(targetDir, source.getName());
            if (target.exists()) throw new IOException();

            if (!source.renameTo(target)) {
                performMoveOperation(source, target);
            }
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }



    private static void deleteRecursive(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            File[] children = fileOrDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        fileOrDir.delete();
    }

    private static boolean copyFile(File source, File dest) {
        boolean success;
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            success = true;
        }
        catch (Exception e) {
            success = false;
        }
        return success;
    }


    private static void performMoveOperation(
            File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.mkdirs()) throw new IOException();
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    performMoveOperation(file, new File(target, file.getName()));
                }
            }
        } else {
            copyFile(source, target);
        }
        deleteRecursive(source);
    }
    public static String readFileText(File file) {
        try {
            if (!file.exists()) throw new FileNotFoundException();
            if (!file.isFile()) throw new IOException();

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            if (content.length() > 0) {
                content.setLength(content.length() - 1);
            }

            return content.toString();
        } catch (Exception e) {
            return "";
        }

    }
    public static boolean writeFileText(File file, String text) {
        boolean success;
        try {
            if (!file.exists()) {
                createFileSync(file);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(text);
            }
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    public static boolean writeFileTextWithOverwrite(File file, String text) {
        boolean success;
        try {
            if (!file.exists()) {
                createFileSync(file);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(text);
            }
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    private static void createFileSync(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException();
        }
        if (!file.createNewFile()) {
            throw new IOException();
        }
    }



}