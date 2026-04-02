package com.skeeper.minicode.presentation.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.lifecycle.HiltViewModel;
import android.os.Handler;
import android.os.Looper;

import javax.inject.Inject;

@HiltViewModel
public class CodeEditorSaveViewModel extends ViewModel {

    private static final long delay = 900L;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Object lock = new Object();


    private final MutableLiveData<Boolean> savingState = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> hasUnsavedChanges = new MutableLiveData<>(false);
    private final MutableLiveData<String> saveError = new MutableLiveData<>(null);
    private final MutableLiveData<Long> lastSavedAt = new MutableLiveData<>(0L);

    @Nullable
    private File pendingFile;

    @Nullable
    private String pendingText;

    @Nullable
    private Runnable pendingSaveRunnable;

    @Inject
    public CodeEditorSaveViewModel() {
    }

    public LiveData<Boolean> getSavingState() {
        return savingState;
    }

    public LiveData<Boolean> getHasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public LiveData<String> getSaveError() {
        return saveError;
    }

    public LiveData<Long> getLastSavedAt() {
        return lastSavedAt;
    }

    public void onEditorTextChanged(@Nullable File file, @Nullable String text) {
        if (file == null) return;

        synchronized (lock) {
            pendingFile = file;
            pendingText = text == null ? "" : text;
        }

        hasUnsavedChanges.postValue(true);
        scheduleDebSave();
    }

    public void saveNow(@Nullable File file, @Nullable String text) {
//        executor.execute(() -> {
//
//        });
        if (file == null) return;

        synchronized (lock) {
            pendingFile = file;
            pendingText = text == null ? "" : text;
        }

        flushPendingSave();
    }

    public void flushPendingSave() {
        mainHandler.removeCallbacksAndMessages(null);

        final File fileToSave;
        final String textToSave;

        synchronized (lock) {//тк может быть рассинхрон
            if (pendingFile == null || pendingText == null) return;
            fileToSave = pendingFile;
            textToSave = pendingText;
        }
        persist(fileToSave, textToSave);
    }

    private void scheduleDebSave() {
        mainHandler.removeCallbacksAndMessages(null);

        pendingSaveRunnable = this::flushPendingSave;
        mainHandler.postDelayed(pendingSaveRunnable, delay);
    }

    private void persist(@NonNull File file, @NonNull String text) {
        savingState.postValue(true);
        saveError.postValue(null);

        executor.execute(() -> {
            try {
                writeTextToFile(file, text);

                savingState.postValue(false);
                hasUnsavedChanges.postValue(false);
                lastSavedAt.postValue(System.currentTimeMillis());
            } catch (Exception e) {
                savingState.postValue(false);
                saveError.postValue(e.getMessage());
            }
        });
    }

    private void writeTextToFile(@NonNull File file, @NonNull String text) throws Exception {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file, false),
                        StandardCharsets.UTF_8
                )
        )) {
            writer.write(text);
            writer.flush();
        }
    }

    @Override
    protected void onCleared() {
        mainHandler.removeCallbacksAndMessages(null);
        executor.shutdownNow();
        super.onCleared();
    }
}