package com.skeeper.minicode.presentation.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.data.repos.LangRepository;
import com.skeeper.minicode.databinding.FragmentCodeEditorBinding;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.HighlightColorModel;
import com.skeeper.minicode.domain.usecases.LangRegexUseCase;
import com.skeeper.minicode.presentation.viewmodels.CodeEditViewModel;
import com.skeeper.minicode.utils.ExtensionUtils;
import com.skeeper.minicode.utils.FileUtils;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class CodeEditorFragment extends Fragment {

    public FragmentCodeEditorBinding binding;
    public FileItem boundFileItem = null;
    public CodeView codeView = null;

    public UndoRedoManager undoRedoManager;
    public CodeEditorFragment() {}
    public CodeEditorFragment(FileItem fileItem) {
        this.boundFileItem = fileItem;
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
        undoRedoManager = new UndoRedoManager(codeView);
        initCodeView(codeView);

        boolean fromGit = false; // todo (if file editing once)

        // init highlight
        LangRepository langRepository = new LangRepository(requireContext(), ExtensionType.JAVA);
        var regexUseCase = new LangRegexUseCase(langRepository);
        codeView.setSyntaxPatternsMap(regexUseCase.execute());
        codeView.reHighlightSyntax();


//        codeView.setText(String.valueOf(langRepository.getLangModel().getAttributes()));

//        if (fromGit)
//            vm.initVM(boundFileItem, FileOpenMode.FROM_GIT, langRepository.getLangModel());
//        else if (boundFileItem != null)
//            vm.initVM(boundFileItem, FileOpenMode.LOCAL, langRepository.getLangModel());
//        else vm.initVM(boundFileItem, FileOpenMode.NEW, langRepository.getLangModel());
//



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void initCodeView(CodeView codeview) {
        codeview.setEnableAutoIndentation(true);
        codeview.setIndentationStarts(Set.of('{'));
        codeview.setIndentationEnds(Set.of('}'));
        codeview.setEnableLineNumber(false);
        codeview.setLineNumberTextColor(Color.parseColor("#3DC2EC"));
        codeview.setLineNumberTextSize(31f);
        codeview.setTextSize(16);
        codeview.setUpdateDelayTime(0);
        codeview.setTabLength(4);
        codeview.setLineNumberTypeface(ResourcesCompat.getFont(requireContext(), R.font.cascadia_code));
    }



}