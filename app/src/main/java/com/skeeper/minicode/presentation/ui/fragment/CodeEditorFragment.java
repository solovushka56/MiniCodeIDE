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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class CodeEditorFragment extends Fragment {

    public FragmentCodeEditorBinding binding;
    public CodeEditViewModel vm;
    public FileItem boundFileItem = null;
    public CodeView codeView = null;

    public HighlightColorModel highlightModel;




    public CodeEditorFragment(FileItem fileItem) {
        this.boundFileItem = fileItem;
    }
    public CodeEditorFragment() {
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
        vm = new ViewModelProvider(this).get(CodeEditViewModel.class);

        initCodeView(codeView);


        boolean fromGit = false; // todo (if file editing once)


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
    private void setupHighlight() {
        var langRepo = new LangRepository(requireContext(),
                ExtensionUtils.getFileExtensionType(boundFileItem.getName()));
        LangRegexUseCase langRegexUseCase = new LangRegexUseCase(langRepo);
        codeView.setSyntaxPatternsMap(langRegexUseCase.execute());
        codeView.reHighlightSyntax();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void initCodeView(CodeView codeview) {
//        addHighlightPatterns();
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
//        setupHighlight();
//        loadFileText();

    }

    private void addHighlightPatterns() {
        int keywordColor = Color.parseColor("#4B70F5");
        int typeColor = Color.parseColor("#4EC9B0");
        int classColor = Color.parseColor("#4EC9B0");
        int methodColor = Color.parseColor("#DCDCAA");
        int bracketColor = Color.parseColor("#569CD6");
        int stringColor = Color.parseColor("#CE9178");

        Map<Pattern, Integer> syntaxPatternsMap = new LinkedHashMap<>();

        String stringRegex = "\"(?:\\\\.|[^\"\\\\])*\"";
        syntaxPatternsMap.put(Pattern.compile(stringRegex), stringColor);

        String charRegex = "'(?:\\\\.|[^'\\\\])*'";
        syntaxPatternsMap.put(Pattern.compile(charRegex), stringColor);


        String keywordsRegex = "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|"
                + "continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|"
                + "implements|import|instanceof|int|interface|long|native|new|package|private|"
                + "protected|public|return|short|static|strictfp|super|switch|synchronized|this|"
                + "throw|throws|transient|try|void|volatile|while|var|record|sealed|non-sealed|permits|"
                + "true|false|null)\\b";
        syntaxPatternsMap.put(Pattern.compile(keywordsRegex), keywordColor);

        String typeRegex = "\\b(String|Integer|Double|Boolean|Float|Long|Short|Byte|" +
                "Character|Void|Object|Exception|Class|Number|System|Math)\\b";
        syntaxPatternsMap.put(Pattern.compile(typeRegex), typeColor);

        String classDeclarationRegex = "(?<=\\bclass\\s)[A-Za-z0-9_]+";
        syntaxPatternsMap.put(Pattern.compile(classDeclarationRegex), classColor);

        String methodCallRegex = "\\b([a-z][a-zA-Z0-9_]*)\\s*(?=\\()";
        syntaxPatternsMap.put(Pattern.compile(methodCallRegex), methodColor);



        codeView.setSyntaxPatternsMap(syntaxPatternsMap);
        codeView.reHighlightSyntax();


    }







}