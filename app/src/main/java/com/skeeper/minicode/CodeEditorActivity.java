package com.skeeper.minicode;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.skeeper.minicode.helpers.GsonMapHelper;
import com.skeeper.minicode.models.KeySymbolItemModel;
import com.skeeper.minicode.singleton.CodeDataSingleton;
import com.skeeper.minicode.singleton.PanelSnippetsDataSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CodeEditorActivity extends AppCompatActivity {
    
    private ActivityCodeEditorBinding binding;

    private final CodeDataSingleton codeDataSingleton = CodeDataSingleton.getInstance();
    private CodeView codeView;
    
    private View rootView;
    private RecyclerView bottomPanel;
    private int minKeyboardHeight = 100;
    private final String[] keywords = KeywordsTemplate.keywords;

    private List<KeySymbolItemModel> keySymbolModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
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




        setKeySymbolsRecycler();
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



//        Map<Character, Character> pairCompleteMap = new HashMap<>();
//        pairCompleteMap.put('{', '}');
//        pairCompleteMap.put('[', ']');
//        pairCompleteMap.put('(', ')');
//        pairCompleteMap.put('<', '>');
//        pairCompleteMap.put('"', '"');

        setupKeyboardListener();

//        addAutocomplete();
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



        Map<Character, Character> pairCompleteMap = new HashMap<>();
        pairCompleteMap.put('{', '}');
        pairCompleteMap.put('[', ']');
        pairCompleteMap.put('(', ')');
        pairCompleteMap.put('<', '>');
        pairCompleteMap.put('"', '"');

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



    private void addKeywordsTokens(CodeView codeView) {
        String regex_classname;
        String regex_only_before_brackets;
        String regex_only_after_point_and_before_round_brackets;


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
        String methodCallRegex = "\\b([A-Za-z_$][\\w$]*)\\s*\\([^)]*\\)";
//
//        String importsRegex = "\\bimport\\s+(static\\s+)?[\\w$.]+\\s*;";
//        String packagesRegex = "\\bpackage\\s+[\\w$.]+\\s*;";
//
//        String operatorsRegex = "([+=\\-*/%&|^<>!~?:]|&&|\\|\\||<<|>>|==|!=|<=|>=|->|::)"; // invalid
//
//        String bracketsRegex = "[{}\\[\\]()]";
//
//        try {
//            codeView.addSyntaxPattern(Pattern.compile(keywordsRegex), keywordColor);
//            codeView.addSyntaxPattern(
//                    Pattern.compile(commentsRegex, Pattern.DOTALL | Pattern.MULTILINE),
//                    strColor);
//            codeView.addSyntaxPattern(Pattern.compile(typesRegex), keywordColor);
//            codeView.addSyntaxPattern(Pattern.compile(classDeclarationRegex), greenColor);
//            codeView.addSyntaxPattern(Pattern.compile(methodDeclarationRegex), methodColor);
//            codeView.addSyntaxPattern(Pattern.compile(methodCallRegex), methodColor);
//
//        }
//        catch (Exception e) {
//            Toast.makeText(this, "syntax highlighter error", Toast.LENGTH_SHORT).show();
//        }



        String[] expressionsPint = new String[] {
                "{",
                "}"
        };
        codeView.addSyntaxPattern(Pattern.compile(keywordsRegex), keywordColor);
//        for (String keyword : keywords) {
//            codeView.addSyntaxPattern(
//                    Pattern.compile("\\s+(" + keyword + ")\\s+"),
//                    keywordColor);
//        }
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



        codeView.addSyntaxPattern(
                Pattern.compile("\\b\\w+\\s*\\.\\s*\\w+\\s*$.*?$\n"),
//                Pattern.compile(METHOD_REGEX),
//                Pattern.compile("\\b(\\w+)\\s*\\$.*?\\$"),
//                Pattern.compile("(?<=\\.)(\\w+)(?=(\\(\\w+|^\\)))"),
//                Pattern.compile(methodCallRegex),
                Color.parseColor("#DCDCAA")
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