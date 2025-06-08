package com.skeeper.minicode.presentation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.FragmentCodeEditorBinding;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.presentation.viewmodels.CodeEditViewModel;
import com.skeeper.minicode.presentation.viewmodels.HighlightViewModel;
import com.skeeper.minicode.utils.FileUtils;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.utils.helpers.VibrationManager;

import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CodeEditorFragment extends Fragment {

    public FragmentCodeEditorBinding binding;
    private final ImageButton buttonUndo;
    private final ImageButton buttonRedo;
    public UndoRedoManager undoRedoManager;

    public HighlightViewModel highlightViewModel;

    private final FileItem boundFileItem;
    public CodeView codeView = null;


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
        codeView = binding.codeViewMain;
        initCodeView(codeView);

        undoRedoManager = new UndoRedoManager(codeView);
        setupButtonListeners(undoRedoManager, buttonUndo, buttonRedo);
        setupTextWatcher(codeView);

        highlightViewModel = new ViewModelProvider(this).get(HighlightViewModel.class);

        highlightViewModel.getCurrentRegexMapData().observe(requireActivity(), data -> {
            codeView.setSyntaxPatternsMap(data);
            codeView.reHighlightSyntax();
        });

        if (boundFileItem != null) {
            highlightViewModel.initHighlightTo(boundFileItem.getDirectory());
            codeView.setText(FileUtils.readFileText(boundFileItem.getDirectory()));
        }
    }


    private void setupButtonListeners(UndoRedoManager undoRedoManager, ImageButton btnUndo, ImageButton btnRedo) {

        btnUndo.setOnClickListener(v -> {
            if (!undoRedoManager.canUndo()) return;
            VibrationManager.vibrate(30L, requireContext());
            undoRedoManager.undo();
            updateButtonStates();
        });
        btnRedo.setOnClickListener(v -> {
            if (!undoRedoManager.canRedo()) return;
            VibrationManager.vibrate(30L, requireContext());
            undoRedoManager.redo();
            updateButtonStates();
        });
    }

    private void setupTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void updateButtonStates() {
        buttonUndo.setEnabled(undoRedoManager.canUndo());
        buttonRedo.setEnabled(undoRedoManager.canRedo());
    }

    private void initCodeView(CodeView codeview) {
//        codeview.setEnableHighlightCurrentLine(true);
        codeview.setEnableAutoIndentation(true);
        codeview.setIndentationStarts(Set.of('{'));
        codeview.setIndentationEnds(Set.of('}'));
        codeview.setEnableLineNumber(false);
        codeview.setLineNumberTextColor(
                Color.parseColor("#3DC2EC"));
        codeview.setLineNumberTextSize(31f);
        codeview.setTextSize(16);
        codeview.setUpdateDelayTime(0);
        codeview.setTabLength(4);
        codeview.setLineNumberTypeface(ResourcesCompat.getFont(requireContext(), R.font.cascadia_code));
    }

    public FileItem getBoundFileItem() {
        return boundFileItem;
    }
}