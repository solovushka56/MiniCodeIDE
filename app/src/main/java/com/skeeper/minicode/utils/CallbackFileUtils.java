package com.skeeper.minicode.utils;

import android.os.Handler;
import android.os.Looper;

import com.skeeper.minicode.domain.contracts.other.callbacks.FileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.ReadFileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.WriteFileCallback;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallbackFileUtils {

//OLD
//    public static String readFileText(Context context, File file) {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try (FileInputStream fis = new FileInputStream(file); BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//            }
//
//            if (stringBuilder.length() > 0) {
//                stringBuilder.setLength(stringBuilder.length() - 1);
//            }
//
//            return stringBuilder.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());



    public static void createFile(File file, FileCallback callback) {
        executor.execute(() -> {
            try {
                if (file.exists()) throw new IOException();

                File parent = file.getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    throw new IOException();
                }

                if (!file.createNewFile()) {
                    throw new IOException();
                }

                notifySuccess(file, callback);
            } catch (Exception e) {
                notifyError(callback);
            }
        });
    }
    public static void deleteFile(File file, FileCallback callback) {
        executor.execute(() -> {
            try {
                if (!file.exists()) throw new FileNotFoundException();

                deleteRecursive(file);
                notifySuccess(file, callback);
            } catch (Exception e) {
                notifyError(callback);
            }
        });
    }
    public static void renameFile(File source, String newName, FileCallback callback) {
        executor.execute(() -> {
            try {
                File target = new File(source.getParent(), newName);
                if (target.exists()) throw new IOException();

                if (!source.renameTo(target)) {
                    performMoveOperation(source, target);
                }
                notifySuccess(target, callback);
            } catch (Exception e) {
                notifyError(callback);
            }
        });
    }
    public static void moveFile(File source, File targetDir, FileCallback callback) {
        executor.execute(() -> {
            try {
                File target = new File(targetDir, source.getName());
                if (target.exists()) throw new IOException();

                if (!source.renameTo(target)) {
                    performMoveOperation(source, target);
                }
                notifySuccess(target, callback);
            } catch (Exception e) {
                notifyError(callback);
            }
        });
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
    private static void copyFile(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
    private static void performMoveOperation(File source, File target) throws IOException {
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


    private static void notifySuccess(File file, FileCallback callback) {
        mainHandler.post(() -> callback.onFinish(file, true));
    }
    private static void notifyError(FileCallback callback) {
        mainHandler.post(() -> callback.onFinish(null, false));
    }


    public static void readFileText(File file, ReadFileCallback callback) {
        executor.execute(() -> {
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

                onReadSuccessNotify(content.toString(), callback);
            } catch (Exception e) {
                onReadErrorNotify(callback);
            }
        });
    }
    public static void writeFileText(File file, String text, WriteFileCallback callback) {
        executor.execute(() -> {
            try {
                if (!file.exists()) {
                    createFileSync(file);
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(text);
                }

                onWriteSuccessNotify(callback);
            } catch (Exception e) {
                onWriteErrorNotify(callback);
            }
        });
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


    private static void onReadSuccessNotify(String content, ReadFileCallback callback) {
        mainHandler.post(() -> callback.onFinish(content, true));
    }
    private static void onReadErrorNotify(ReadFileCallback callback) {
        mainHandler.post(() -> callback.onFinish("", false));
    }

    private static void onWriteSuccessNotify(WriteFileCallback callback) {
        mainHandler.post(() -> callback.onFinish(true));
    }
    private static void onWriteErrorNotify(WriteFileCallback callback) {
        mainHandler.post(() -> callback.onFinish(false));
    }
}