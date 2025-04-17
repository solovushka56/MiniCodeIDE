package com.skeeper.minicode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.adapters.KeySymbolAdapter;
import com.skeeper.minicode.data.KeywordsTemplate;
import com.skeeper.minicode.databinding.ActivityCodeEditorBinding;
import com.skeeper.minicode.helpers.UndoRedoManager;
import com.skeeper.minicode.helpers.VibrationManager;
import com.skeeper.minicode.models.KeySymbolItemModel;
import com.skeeper.minicode.services.editor.codeformater.JavaCodeFormatter;
import com.skeeper.minicode.singleton.CodeDataSingleton;
import com.skeeper.minicode.singleton.PanelSnippetsDataSingleton;
import com.skeeper.minicode.singleton.ProjectManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class CodeEditorActivity extends AppCompatActivity {
    
    private ActivityCodeEditorBinding binding;

    private final CodeDataSingleton codeDataSingleton = CodeDataSingleton.getInstance();
    private CodeView codeView;
    
    private View rootView;
    private RecyclerView bottomPanel;
    private int minKeyboardHeight = 100;
    private final String[] keywords = KeywordsTemplate.keywords;
    private String projectName = null;

    UndoRedoManager undoRedoManager;
    private List<KeySymbolItemModel> keySymbolModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

//        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        keySymbolModels = PanelSnippetsDataSingleton.loadList(
                this,
                "keySymbolsData.json");;

        codeView = binding.codeViewMain;
        rootView = binding.main;
        bottomPanel = binding.symbolsPanel;
        codeDataSingleton.setCurrentCodeView(codeView);

        binding.backButton.setOnClickListener(v -> {
            finish();
        });
        binding.openFolderButton.setOnClickListener(v -> {
            Toast.makeText(this, "in development...", Toast.LENGTH_SHORT).show();
        });
        binding.optionsButton.setOnClickListener(v -> {
            startActivity(new Intent(CodeEditorActivity.this, CodeEditorSettingsActivity.class));
        });


        binding.recreateButton.setOnClickListener( v-> {
            recreate();
        });
        setKeySymbolsRecycler();
//        addKeywordsTokens(codeView);
        addHighlightPatterns();



        codeView.setEnableAutoIndentation(true);
        codeView.setIndentationStarts(Set.of('{'));
        codeView.setIndentationEnds(Set.of('}'));
        codeView.setEnableLineNumber(false);
        codeView.setLineNumberTextColor(Color.parseColor("#3DC2EC"));
        codeView.setLineNumberTextSize(31f);
        codeView.setTextSize(16);
        codeView.setUpdateDelayTime(0);
        codeView.setTabLength(4);
        codeView.setLineNumberTypeface(ResourcesCompat.getFont(this, R.font.cascadia_code));
        setupKeyboardListener();
        undoRedoManager = new UndoRedoManager(codeView);
        setupButtonListeners(binding.buttonUndo, binding.buttonRedo);
        setupTextWatcher(codeView);



        loadFileText();
        binding.saveButton.setOnClickListener(v -> {
            String code = codeView.getText().toString();
            try {
                ProjectManager.saveFile(this, projectName, "main.java", code);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

        });

    }


    private void loadFileText() {
        projectName = getIntent().getStringExtra("projectName");
        if (projectName == null) return;

        try {
            codeView.append(ProjectManager.readFile(this, projectName, "main.java"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private void codeViewSetup() {
        addKeywordsTokens(codeView);

        codeView.setEnableAutoIndentation(true);
        codeView.setIndentationStarts(Set.of('{'));
        codeView.setIndentationEnds(Set.of('}'));

        codeView.setEnableLineNumber(false);
        codeView.setLineNumberTextColor(Color.parseColor("#3DC2EC"));
        codeView.setLineNumberTextSize(31f);
        codeView.setTextSize(16);

        codeView.setUpdateDelayTime(0);
        codeView.setTabLength(4);


        codeView.setLineNumberTypeface(ResourcesCompat.getFont(this, R.font.cascadia_code));


    }



    private void addAutocomplete() {
        String[] languageKeywords = keywords;
        var adapter = new ArrayAdapter<String>(
                this, R.layout.activity_code_editor, binding.codeViewMain.getId(), languageKeywords);
        codeView.setAdapter(adapter);
    }



    private void setKeySymbolsRecycler() {
        var recyclerView = binding.symbolsPanel;
        var adapter = new KeySymbolAdapter(this, keySymbolModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

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

        String keywordsRegex = "\\b(abstract|assert|boolean|break|byte|case|catch|char|" +
                "class|const|continue|default|do|double|else|enum|extends|final|finally|" +
                "float|for|goto|if|implements|import|instanceof|int|interface|long|native|" +
                "new|package|private|protected|public|return|short|static|strictfp|super|" +
                "switch|synchronized|this|throw|throws|transient|try|void|volatile|while|" +
                "true|false|null)\\b";
        syntaxPatternsMap.put(Pattern.compile(keywordsRegex), keywordColor);

        String typeRegex = "\\b(String|Integer|Double|Boolean|Float|Long|Short|Byte|" +
                "Character|Void|Object|Exception|Class|Number|System|Math)\\b";
        syntaxPatternsMap.put(Pattern.compile(typeRegex), typeColor);

        String classDeclarationRegex = "(?<=\\bclass\\s)[A-Za-z0-9_]+";
        syntaxPatternsMap.put(Pattern.compile(classDeclarationRegex), classColor);

        String methodCallRegex = "\\b([a-z][a-zA-Z0-9_]*)\\s*(?=\\()";
        syntaxPatternsMap.put(Pattern.compile(methodCallRegex), methodColor);

        syntaxPatternsMap.put(Pattern.compile("(?<=\\w)\\s*(\\(|\\))"), bracketColor);

        codeView.setSyntaxPatternsMap(syntaxPatternsMap);
        codeView.reHighlightSyntax();



//        Map<Pattern, Integer> syntaxPatternsMap = new HashMap<>();
//
//        int keywordColor = Color.parseColor("#4B70F5");
//        int methodColor = Color.parseColor("#DCDCAA");
//        int pinkedColor = Color.parseColor("#C270D6");
//        int strColor = Color.parseColor("#00BCB2");
//        int greenColor = Color.parseColor("#00BCB2");
//
//        String keywordsRegex = "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|"
//                + "continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|"
//                + "implements|import|instanceof|int|interface|long|native|new|package|private|"
//                + "protected|public|return|short|static|strictfp|super|switch|synchronized|this|"
//                + "throw|throws|transient|try|void|volatile|while|var|record|sealed|non-sealed|permits|"
//                + "true|false|null)\\b";
//
//
//        syntaxPatternsMap.put(Pattern.compile("\"(.*?)\""), Color.parseColor("#CE9178"));
//        syntaxPatternsMap.put(Pattern.compile(keywordsRegex), keywordColor);
//        syntaxPatternsMap.put(
//                Pattern.compile("(?<![\"'])\\b([A-Z][a-zA-Z]*)\\b(?![\"'])"),
//                greenColor
//        );
//        syntaxPatternsMap.put(Pattern.compile("\\.(\\w+)\\(\\)"), methodColor);
//        syntaxPatternsMap.put(Pattern.compile("\\b(\\w+)\\("), methodColor);
//        syntaxPatternsMap.put(Pattern.compile("\\("), pinkedColor);
//        syntaxPatternsMap.put(Pattern.compile("\\)"), pinkedColor);
//
//        codeView.setSyntaxPatternsMap(syntaxPatternsMap);
    }

    private void addKeywordsTokens(CodeView codeView) {
        String regex_classname;
        String regex_only_before_brackets;
        String regex_only_after_point_and_before_round_brackets;

//
//        String typesRegex = "\\b(boolean|byte|char|short|int|long|float|double|void|"
//                + "Boolean|Byte|Character|Short|Integer|Long|Float|Double|String|Object|Class)\\b";
//
//        String stringsRegex = "\"(\\\\\"|[^\"])*\"|'(\\\\'|[^'])*'";
//
//        String numbersRegex = "\\b(0b[01_]+|0x[0-9a-fA-F_]+|0[0-7_]*|\\d[\\d_]*(\\.\\d[\\d_]*)?([eE][+-]?\\d[\\d_]*)?[fFdDlL]?)\\b";
//
//        String commentsRegex = "//.*|/\\*(.|\\R)*?\\*/|/\\*\\*(.|\\R)*?\\*/";
//
//        String annotationsRegex = "@[\\w$]+(\\([^)]*\\))?";
//
//        String classDeclarationRegex = "\\b(class|interface|enum|record)\\s+([A-Z$][\\w$]*)";
//
//        String methodDeclarationRegex = "\\b([A-Za-z_$][\\w$]*\\s+)+([A-Za-z_$][\\w$]*)\\s*\\([^)]*\\)\\s*\\{";
//
        int keywordColor = Color.parseColor("#4B70F5");
        int methodColor = Color.parseColor("#DCDCAA");
        int pinkedColor = Color.parseColor("#C270D6");
        int strColor = Color.parseColor("#00BCB2");
        int greenColor = Color.parseColor("#00BCB2");

        String keywordsRegex = "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|"
                + "continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|"
                + "implements|import|instanceof|int|interface|long|native|new|package|private|"
                + "protected|public|return|short|static|strictfp|super|switch|synchronized|this|"
                + "throw|throws|transient|try|void|volatile|while|var|record|sealed|non-sealed|permits|"
                + "true|false|null)\\b";

        String methodCallRegex = "\\b([A-Za-z_$][\\w$]*)\\s*\\([^)]*\\)";


        codeView.addSyntaxPattern(Pattern.compile(keywordsRegex), keywordColor);


        // to classes
        codeView.addSyntaxPattern(
                Pattern.compile("\\b[A-Z][a-zA-Z]*\\b"),
                Color.parseColor("#00BCB2"));

        // to strings
        codeView.addSyntaxPattern(
                Pattern.compile("\"(.*?)\""),
                Color.parseColor("#CE9178"));

        // to methods calling
        codeView.addSyntaxPattern(
                Pattern.compile("\\.(\\w+)\\(\\)"),
                Color.parseColor("#DCDCAA"));


//         to methods
//        codeView.addSyntaxPattern(
//                Pattern.compile("\\b(\\w+)\\s*\\(\\)"),
//                Color.parseColor("#DCDCAA")
//        );
        // to .method()

        String METHOD_REGEX =
                "(?xi)" +
                        "(?:@\\w+(?:\\(.*?\\))?\\s+)*" +
                        "\\b((public|protected|private|static|final|" +
                        "synchronized|abstract|volatile|native|strictfp|default)\\b\\s+)*" +
                        "(<[^>]+>\\s+)?" +
                        "([\\w.<>\\[\\],\\s]+?)\\s+" +
                        "(\\w+)\\s*\\(";


//        codeView.patt
        codeView.addSyntaxPattern(
                Pattern.compile("\\b(\\w+)\\("),
                Color.parseColor("#DCDCAA")
        );
        // to "("
        codeView.addSyntaxPattern(
                Pattern.compile("\\("),
                Color.parseColor("#C270D6")
        );
        // to ")"
        codeView.addSyntaxPattern(
                Pattern.compile("\\)"),
                Color.parseColor("#C270D6")
        );
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
}