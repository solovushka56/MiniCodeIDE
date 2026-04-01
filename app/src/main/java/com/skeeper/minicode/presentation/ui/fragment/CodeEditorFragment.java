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
    public UndoRedoManager undoRedoManager;
//    public HighlightViewModel highlightViewModel;
//    public CodeEditorViewModel codeEditorViewModel;

    private final FileItem boundFileItem;
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

//        setupCodeEditor();
        //setupButtonListeners(buttonUndo, buttonRedo);
        //codeEditorViewModel = new ViewModelProvider(this).get(CodeEditorViewModel.class);


        if (boundFileItem != null) {
            editor.setText(FileUtils.readFileText(boundFileItem.getDirectory()));
        }

        try {
            TextMateManager.init(requireContext());
            TextMateManager.applyColorScheme(editor);
            EditorTextMateApplier.applyLanguageByFileName(editor, boundFileItem.getName());
            //TextMateLanguage language = TextMateLanguage.create("source.python", true);
            //editor.setEditorLanguage(language);
        }
        catch (Exception e) {
            Log.e("EDITORRR", "failed to init: " + e.getMessage());
            e.printStackTrace();
        }

        setupCodeEditor();
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

        int bg = ContextCompat.getColor(getContext(), R.color.blue_grey);
        var scheme = editor.getColorScheme();
        scheme.setColor(EditorColorScheme.WHOLE_BACKGROUND, bg);
        scheme.setColor(EditorColorScheme.LINE_NUMBER_BACKGROUND, bg);
        scheme.setColor(EditorColorScheme.TEXT_NORMAL, Color.parseColor("#E5DADA"));
    }

    public FileItem getBoundFileItem() {
        return boundFileItem;
    }
}