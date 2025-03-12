package com.skeeper.minicode;

import static java.security.AccessController.getContext;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.amrdeveloper.codeview.KeywordTokenizer;
import com.skeeper.minicode.adapters.KeySymbolAdapter;
import com.skeeper.minicode.adapters.ProjectAdapter;
import com.skeeper.minicode.data.KeywordsTemplate;
import com.skeeper.minicode.databinding.ActivityCodeEditorBinding;
import com.skeeper.minicode.models.KeySymbolBarModel;
import com.skeeper.minicode.models.KeySymbolItemModel;
import com.skeeper.minicode.singleton.CodeDataSingleton;

import java.util.ArrayList;
import java.util.HashMap;
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

    private List<KeySymbolItemModel> models = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_code_editor);

         binding = ActivityCodeEditorBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        codeView = binding.codeViewMain;
        rootView = binding.main;
        bottomPanel = binding.symbolsPanel;

        codeDataSingleton.setCurrentCodeView(codeView);
        models.add(new KeySymbolItemModel(0, "{}", "{}"));
        models.add(new KeySymbolItemModel(1, "pb", "public"));

        setRecycler();









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






    private void setRecycler() {
        var recyclerView = binding.symbolsPanel;
        var adapter = new KeySymbolAdapter(this, models);

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
        int method_color = Color.parseColor("#DCDCAA");
        int pinked_color = Color.parseColor("#C270D6");

        String[] expressionsPint = new String[] {
                "{",
                "}"
        };


        for (String keyword : keywords) {
//            String keywordRegex = "\\s+(" + keyword + ")\\s+";
//            codeView.addSyntaxPattern(Pattern.compile(keywordRegex), keywordColor);
            codeView.addSyntaxPattern(Pattern.compile(keyword), keywordColor);

        }

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
        codeView.addSyntaxPattern(
                Pattern.compile("\\b\\w+\\("),
                Color.parseColor("#DCDCAA")
        );
        // to .method()
        codeView.addSyntaxPattern(
                Pattern.compile("(?<=\\.)(\\w+)(?=(\\(\\w+|^\\)))"),
                Color.parseColor("#DCDCAA")
        );


//        codeView.setHighlightWhileTextChanging();
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