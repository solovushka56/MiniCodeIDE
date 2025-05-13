package com.skeeper.minicode.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.data.repos.LocalFileRepository;
import com.skeeper.minicode.domain.contracts.other.callbacks.IKeyPressedListener;
import com.skeeper.minicode.presentation.ui.fragment.CodeEditorFragment;
import com.skeeper.minicode.presentation.ui.other.FileTreeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.adapters.KeySymbolAdapter;
import com.skeeper.minicode.data.KeywordsTemplate;
import com.skeeper.minicode.databinding.ActivityCodeEditorBinding;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;
import com.skeeper.minicode.presentation.viewmodels.factory.FileViewModelFactory;
import com.skeeper.minicode.utils.FileUtils;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.utils.helpers.VibrationManager;
import com.skeeper.minicode.domain.contracts.other.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.KeySymbolItemModel;
import com.skeeper.minicode.core.singleton.CodeDataSingleton;
import com.skeeper.minicode.core.singleton.PanelSnippetsDataSingleton;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.presentation.viewmodels.CodeEditViewModel;

import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class CodeEditorActivity extends AppCompatActivity implements IFileTreeListener, IKeyPressedListener {

    private ActivityCodeEditorBinding binding;

    private final CodeDataSingleton codeDataSingleton = CodeDataSingleton.getInstance();
    private CodeView codeView;

    private View rootView;
    private RecyclerView bottomPanel;
    private final int minKeyboardHeight = 100;
    private final String[] keywords = KeywordsTemplate.keywords;
    private String projectName = null;

    UndoRedoManager undoRedoManager;
    private List<KeySymbolItemModel> keySymbolModels;

    private FilesViewModel filesViewModel;
    private CodeEditViewModel codeEditViewModel;
    private CodeEditorFragment currentCodeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getResources().getColor(R.color.transparent));
        projectName = getIntent().getStringExtra("projectName");

        keySymbolModels = PanelSnippetsDataSingleton.loadList(
                this, "keySymbolsData.json");



        currentCodeFragment = new CodeEditorFragment();
        setFragment(currentCodeFragment);


        rootView = binding.main;
        bottomPanel = binding.symbolsPanel;
        codeDataSingleton.setCurrentCodeView(codeView);

        binding.backButton.setOnClickListener(v -> {
            finish();
        });
        binding.openFolderButton.setOnClickListener(v -> {
            hideKeyboard();
            getCurrentCodeView().clearFocus();
            binding.drawerLayout.openDrawer(GravityCompat.START);

//            Toast.makeText(this, "in development...", Toast.LENGTH_SHORT).show();
        });
        binding.optionsButton.setOnClickListener(v -> {
            startActivity(new Intent(
                    CodeEditorActivity.this,
                    CodeEditorSettingsActivity.class));
        });
        binding.recreateButton.setOnClickListener( v-> {
            recreate();
        });

        setKeySymbolsRecycler();
        setupKeyboardListener();



        // filesystem setup
        FileTreeView fileSystemView = new FileTreeView(this);
        fileSystemView.init(this, binding.leftDrawer,
                ProjectManager.getProjectDir(this, projectName));
        binding.leftDrawer.addView(fileSystemView);

        filesViewModel = new ViewModelProvider(
                this, new FileViewModelFactory(ProjectManager
                .getProjectDir(this, projectName)))
                .get(FilesViewModel.class);
        filesViewModel.getFileRepositoryList().observe(this, (fileItems) ->
                fileSystemView.updateFileItems(this, fileItems));

        filesViewModel.getSelectedFile().observe(this, fileItem -> {
//            currentCodeFragment = new CodeEditorFragment();
//            setFragment(currentCodeFragment);
            getCurrentCodeView().setText(FileUtils.readFileText(fileItem.getDirectory()));
        });



    }

    private CodeView getCurrentCodeView() {
        return currentCodeFragment.codeView;
    }
    private void setupFileTreeSystem() {

    }

    private void initCodeView(CodeView codeview) {
//        setupKeyboardListener();
        setupButtonListeners(binding.buttonUndo, binding.buttonRedo);
//        loadFileText();
        undoRedoManager = new UndoRedoManager(codeview);
        setupTextWatcher(codeview);

    }

    public void setFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_fade_in, R.anim.slide_down_fade_out);
        fragmentTransaction.replace(binding.codeViewFrame.getId(), newFragment);
        fragmentTransaction.commit();
    }


    private void setupButtonListeners(ImageButton btnUndo, ImageButton btnRedo) {
        btnUndo.setOnClickListener(v -> {
            if (!undoRedoManager.canUndo()) return;
            VibrationManager.vibrate(30L, this);
            undoRedoManager.undo();
            updateButtonStates();
        });
        btnRedo.setOnClickListener(v -> {
            if (!undoRedoManager.canRedo()) return;
            VibrationManager.vibrate(30L, this);
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
        binding.buttonUndo.setEnabled(undoRedoManager.canUndo());
        binding.buttonRedo.setEnabled(undoRedoManager.canRedo());
    }

    private void setupKeyboardListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            rootView.getWindowVisibleDisplayFrame(rect);

            int screenHeight = rootView.getRootView().getHeight();
            int keyboardHeight = screenHeight - rect.bottom;

            if (keyboardHeight > convertDpToPx(minKeyboardHeight)) {
                int fillerTab = 40;
                bottomPanel.getLayoutParams().height = keyboardHeight + fillerTab;
                bottomPanel.setVisibility(View.VISIBLE);
            } else {
                bottomPanel.setVisibility(View.GONE);
            }
            bottomPanel.requestLayout();
        });
    }
    private int convertDpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
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
    public void onSymbolClick(View view) {
        Button btn = (Button) view;
        EditText editText = codeView;
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);
        editText.getText().replace(Math.min(start, end), Math.max(start, end),
                btn.getContentDescription(), 0, btn.getText().length());
    }
    private void updateKeywordPanel(WindowInsetsCompat insets) {
        boolean isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
        int keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

        var windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        if (isKeyboardVisible) {
            binding.symbolsPanel.setY(screenHeight - keyboardHeight + 25);
            binding.symbolsPanel.setVisibility(View.VISIBLE);
        }
        else {
            binding.symbolsPanel.setVisibility(View.GONE);
        }

    }



    private void setKeySymbolsRecycler() {
        var recyclerView = binding.symbolsPanel;
        var adapter = new KeySymbolAdapter(this, keySymbolModels, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

    }
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }



    @Override
    public void onFileClick(FileItem fileItem) {
        if (filesViewModel.getSelectedFile().getValue() == fileItem) return;

        if (filesViewModel.getSelectedFile().getValue() != null) {
            filesViewModel.writeFileText(getCurrentCodeView().getText().toString()); // save
        }

        filesViewModel.getSelectedFile().setValue(fileItem);

    }



    @Override
    public void onFolderClick(FileItem fileItem) {
    }

    @Override
    public void onFolderAdd(FileItem fileItem) {

    }

    @Override
    public void onFileAdd(FileItem fileItem) {

    }


    //on key symbol panel key pressed
    @Override
    public void onKeyPressed(KeySymbolItemModel pressedKey) {
        var currentCodeView = getCurrentCodeView();
        if (currentCodeView == null) {
            Log.e("INIT_E", "codeview is null");
            return;
        }
        writeKeySymbol(pressedKey);
    }
    private void writeKeySymbol(KeySymbolItemModel keySymbolItemModel) {
        var currentCodeView = getCurrentCodeView();
        if (currentCodeView == null) {
            Log.e("INIT_E", "key symbol key not inited");
            return;
        }
//        Toast.makeText(this, "alal", Toast.LENGTH_SHORT).show();
        int cursorPosition = currentCodeView.getSelectionEnd();
        Editable editable = currentCodeView.getText();
        editable.insert(cursorPosition, keySymbolItemModel.getSymbolValue());
        currentCodeView.setSelection(cursorPosition + keySymbolItemModel.getSymbolValue().length());
    }
}