package com.skeeper.minicode.core.singleton.services.editor.codeformater;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class JavaCodeFormatter implements TextWatcher {
    private EditText editText;
    private boolean isFormatting;

    public JavaCodeFormatter(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (isFormatting) {
            return;
        }

        isFormatting = true;

        try {
            String original = editable.toString();
            String formatted = formatJavaCode(original);

            if (!original.equals(formatted)) {
                int cursorPos = editText.getSelectionStart();
                editable.replace(0, editable.length(), formatted);

                if (cursorPos <= formatted.length()) {
                    editText.setSelection(cursorPos);
                }
            }
        } finally {
            isFormatting = false;
        }
    }

    private String formatJavaCode(String code) {
        if (code.isEmpty()) {
            return code;
        }

        code = code.trim();
        code = code.replaceAll("\\s+", " ");
        code = formatAroundOperators(code);
        code = formatAroundBrackets(code);
        code = code.replaceAll("\\s*,\\s*", ", ");
        code = code.replaceAll("\\s*\\.\\s*", ".");
        code = code.replaceAll("\\s*;\\s*", "; ");
        code = code.replaceAll(";\\s+$", ";");

        return code;
    }

    private String formatAroundOperators(String code) {
        String[] operators = {"=", "\\+", "-", "\\*", "/", "%", "&", "\\|", "\\^", "<", ">", "==", "!=", "<=", ">=", "&&", "\\|\\|", "\\+=", "-=", "\\*=", "/="};

        for (String op : operators) {
            code = code.replaceAll("\\s*" + op + "\\s*", " " + op + " ");
            code = code.replaceAll("\\s+" + op + "\\s+", " " + op + " ");
        }

        return code;
    }

    private String formatAroundBrackets(String code) {
        code = code.replaceAll("\\s*\\(\\s*", "(");
        code = code.replaceAll("\\s*\\)\\s*", ") ");
        code = code.replaceAll("\\s*\\{\\s*", " { ");
        code = code.replaceAll("\\s*\\}\\s*", " } ");
        code = code.replaceAll("\\s*\\[\\s*", "[");
        code = code.replaceAll("\\s*\\]\\s*", "] ");
        code = code.replaceAll("\\(\\s+", "(");
        code = code.replaceAll("\\s+\\)", ")");
        code = code.replaceAll("\\[\\s+", "[");
        code = code.replaceAll("\\s+\\]", "]");

        return code;
    }
}