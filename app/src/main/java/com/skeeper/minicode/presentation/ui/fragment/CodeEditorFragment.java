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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.data.repos.LangRepository;
import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.databinding.FragmentCodeEditorBinding;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.HighlightColorModel;
import com.skeeper.minicode.domain.usecases.LangRegexUseCase;
import com.skeeper.minicode.presentation.viewmodels.CodeEditViewModel;
import com.skeeper.minicode.presentation.viewmodels.factory.CodeEditViewModelFactory;
import com.skeeper.minicode.utils.ExtensionUtils;
import com.skeeper.minicode.utils.FileUtils;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.utils.helpers.VibrationManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class CodeEditorFragment extends Fragment {

    public FragmentCodeEditorBinding binding;
    public FileItem boundFileItem = null;
    public CodeView codeView = null;
    public CodeEditViewModel codeEditViewModel; //
    public UndoRedoManager undoRedoManager;


    public CodeEditorFragment(FileItem fileItem, ImageButton buttonUndo, ImageButton buttonRedo) {
        this.boundFileItem = fileItem;
        this.buttonUndo = buttonUndo;
        this.buttonRedo = buttonRedo;

    }

    public CodeEditorFragment(ImageButton buttonUndo, ImageButton buttonRedo) {
        this.buttonUndo = buttonUndo;
        this.buttonRedo = buttonRedo;
    }

    ImageButton buttonUndo;
    ImageButton buttonRedo;


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
//        codeEditViewModel = new ViewModelProvider(this, new CodeEditViewModelFactory(
//                boundFileItem, FileOpenMode.LOCAL)).get(CodeEditViewModel.class);

        // init highlight
        LangRepository langRepository = new LangRepository(requireContext(), ExtensionType.JAVA);
        var regexUseCase = new LangRegexUseCase(langRepository);
        codeView.setSyntaxPatternsMap(regexUseCase.execute());
        codeView.reHighlightSyntax();

        if (boundFileItem != null)
            codeView.setText(FileUtils.readFileText(boundFileItem.getDirectory()));
//        codeView.setText(String.valueOf(langRepository.getLangModel().getAttributes()));
//        if (fromGit)
//            vm.initVM(boundFileItem, FileOpenMode.FROM_GIT, langRepository.getLangModel());
//        else if (boundFileItem != null)
//            vm.initVM(boundFileItem, FileOpenMode.LOCAL, langRepository.getLangModel());
//        else vm.initVM(boundFileItem, FileOpenMode.NEW, langRepository.getLangModel());
//


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
//                updateButtonStates();
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


    // when fragment changes or leaving we save file
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        saveFile(codeView.getText().toString());

    }

    public void saveFile(String fileText) {
        if (boundFileItem == null) return;
        Log.e("MSG1", "saving file");
        FileUtils.writeFileText(boundFileItem.getDirectory(), fileText);
//        codeEditViewModel.saveFile(fileText);
    }

}