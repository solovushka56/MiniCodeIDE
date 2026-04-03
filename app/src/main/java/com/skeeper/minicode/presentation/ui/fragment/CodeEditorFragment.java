package com.skeeper.minicode.presentation.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.FragmentCodeEditorBinding;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.LangModel;
import com.skeeper.minicode.presentation.viewmodels.CodeEditorSaveViewModel;
import com.skeeper.minicode.presentation.viewmodels.CodeEditorViewModel;
import com.skeeper.minicode.presentation.viewmodels.HighlightViewModel;
import com.skeeper.minicode.utils.FileUtils;
import com.skeeper.minicode.utils.helpers.EditorTextMateApplier;
import com.skeeper.minicode.utils.helpers.TextMateManager;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.utils.helpers.VibrationManager;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

@AndroidEntryPoint
public class CodeEditorFragment extends Fragment {

    public FragmentCodeEditorBinding binding;
    private final ImageButton buttonUndo;
    private final ImageButton buttonRedo;
    private CodeEditorSaveViewModel saveViewModel;
    private boolean suppressAutosave = true;
    private final FileItem boundFileItem;
    @Nullable private Runnable onEditorHistoryChanged;

    public CodeEditorViewModel codeEditorViewModel;

    public CodeEditor editor = null;
    public CodeEditorFragment(FileItem fileItem, ImageButton buttonUndo, ImageButton buttonRedo) {
        this.boundFileItem = fileItem;
        this.buttonUndo = buttonUndo;
        this.buttonRedo = buttonRedo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCodeEditorBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editor = binding.codeEditorMain;

        try {
            TextMateManager.applyColorScheme(editor);
            EditorTextMateApplier.applyLanguageByFileName(editor, boundFileItem.getName());
            //TextMateLanguage language = TextMateLanguage.create("source.python", true);
            //editor.setEditorLanguage(language);
        }
        catch (Exception e) {
            Log.e("EDITOR_EXC", "failed to init: " + e.getMessage());
            e.printStackTrace();
        }
        setupCodeEditor();


        codeEditorViewModel = new ViewModelProvider(this).get(CodeEditorViewModel.class);
        codeEditorViewModel.getFileText().observe(getViewLifecycleOwner(), data -> {
            editor.setText(data == null ? "" : data);
            notifyEditorHistoryChanged();
        });
        codeEditorViewModel.loadFileText(boundFileItem);

        saveViewModel = new ViewModelProvider(this).get(CodeEditorSaveViewModel.class);

        setupUndoRedoStateSync();
        setupAutosave();
        observeSaveState();
        suppressAutosave = false;
    }


    private void setupCodeEditor() {
        editor = binding.codeEditorMain;
        editor.setTextSizePx(42f);
        editor.setLineNumberEnabled(true);
        editor.setWordwrap(false);
        editor.setBackgroundResource(R.color.blue_grey);
        editor.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                        | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        );

        var typeface = ResourcesCompat.getFont(requireContext(),
                R.font.cascadia_code);
        editor.setTypefaceText(typeface);
        editor.setTypefaceLineNumber(typeface);
        editor.setLineNumberEnabled(false);
        editor.setScrollBarEnabled(false); // todo back
        editor.setHighlightBracketPair(true);
        editor.setBlockLineWidth(0.08f);

        int bg = ContextCompat.getColor(getContext(), R.color.blue_grey);
        var scheme = editor.getColorScheme();
        scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, bg);
        scheme.setColor(EditorColorScheme.LINE_NUMBER_BACKGROUND, bg);
        scheme.setColor(EditorColorScheme.TEXT_NORMAL, Color.parseColor("#E5DADA"));
    }

    private void setupAutosave() {
        if (editor == null) return;

        editor.subscribeAlways(io.github.rosemoe.sora.event.ContentChangeEvent.class, event -> {
            if (suppressAutosave) return;
            if (boundFileItem == null) return;

            saveViewModel.onEditorTextChanged(
                    boundFileItem.getDirectory(),
                    editor.getText().toString()
            );
        });
    }

    private void observeSaveState() {
        saveViewModel.getSaveError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.trim().isEmpty()) {
                Log.e("SAVE_FILE", "save error:" + error);
            }
        });
    }
    public void saveNow() {
        if (boundFileItem == null || editor == null) return;

        saveViewModel.saveNow(
                boundFileItem.getDirectory(),
                editor.getText().toString()
        );
    }

    public void flushPendingSave() {
        saveViewModel.flushPendingSave();
    }
    public FileItem getBoundFileItem() {
        return boundFileItem;
    }
    public void setOnEditorHistoryChanged(@Nullable Runnable onEditorHistoryChanged) {
        this.onEditorHistoryChanged = onEditorHistoryChanged;
    }
    private void notifyEditorHistoryChanged() {
        if (onEditorHistoryChanged != null) {
            onEditorHistoryChanged.run();
        }
    }

    private void setupUndoRedoStateSync() {
        if (editor == null) return;

        editor.subscribeAlways(io.github.rosemoe.sora.event.ContentChangeEvent.class, event -> {
            notifyEditorHistoryChanged();
        });
    }
}