package com.skeeper.minicode.presentation.ui.other.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;

import io.github.rosemoe.sora.graphics.Paint;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public final class EditorStyleConfigurator {

    private EditorStyleConfigurator() {}

    public static void applyStyle(CodeEditor editor, Context context) {
//        Typeface cascadia = Typeface.createFromAsset(
//                context.getAssets(),
//                "fonts/cascadia_code_semi_bold_origin.ttf"
//        );
//
//        editor.setTypefaceText(cascadia);
//        editor.setTypefaceLineNumber(cascadia);

        editor.setLineNumberEnabled(true);
        editor.setVerticalScrollBarEnabled(true);
        editor.setHorizontalScrollBarEnabled(true);
        editor.setOverScrollMode(View.OVER_SCROLL_NEVER);

        editor.setTextSizePx(sp(context, 15.5f));
        editor.setDividerWidth(dp(context, 1f));
        editor.setDividerMargin(dp(context, 10f));
        editor.setLineNumberMarginLeft(dp(context, 10f));
        editor.setCursorWidth(dp(context, 2f));
        editor.setTextBorderWidth(dp(context, 0.8f));
        editor.setLineNumberAlign(Paint.Align.RIGHT);
        editor.setColorScheme(new minicodeColorScheme());
        editor.invalidate();
    }

    private static float dp(Context context, float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.getResources().getDisplayMetrics()
        );
    }

    private static float sp(Context context, float value) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                context.getResources().getDisplayMetrics()
        );
    }

    public static class minicodeColorScheme extends EditorColorScheme {

        public minicodeColorScheme() {
            super(true);
        }

        @Override
        public void applyDefault() {
            super.applyDefault();
            setColor(WHOLE_BACKGROUND, 0xFF455A64);
            setColor(LINE_NUMBER_BACKGROUND, 0xFF455A64);
            setColor(LINE_NUMBER_PANEL, 0xFF37474F);
            setColor(LINE_DIVIDER, 0xFF546E7A);

            setColor(TEXT_NORMAL, 0xFFE5DADA);
            setColor(LINE_NUMBER, 0xFFB0BEC5);
            setColor(LINE_NUMBER_CURRENT, 0xFFFFFFFF);
            setColor(LINE_NUMBER_PANEL_TEXT, 0xFFF5F5F5);

            setColor(SELECTION_INSERT, 0xFFFFCC80);
            setColor(SELECTION_HANDLE, 0xFFFFCC80);
            setColor(SELECTED_TEXT_BACKGROUND, 0x334FC3F7);
            setColor(SELECTED_TEXT_BORDER, 0x554FC3F7);
            setColor(TEXT_SELECTED, 0xFFFFFFFF);

            setColor(CURRENT_LINE, 0x1AFFFFFF);
            setColor(CURRENT_ROW_BORDER, 0x33546E7A);

            setColor(SCROLL_BAR_TRACK, 0x00000000);
            setColor(SCROLL_BAR_THUMB, 0x6678909C);
            setColor(SCROLL_BAR_THUMB_PRESSED, 0xFF90A4AE);

            setColor(HIGHLIGHTED_DELIMITERS_BACKGROUND, 0x224FC3F7);
            setColor(HIGHLIGHTED_DELIMITERS_BORDER, 0xFF81D4FA);
            setColor(HIGHLIGHTED_DELIMITERS_UNDERLINE, 0x00000000);
            setColor(MATCHED_TEXT_BACKGROUND, 0x22FFD54F);
            setColor(MATCHED_TEXT_BORDER, 0x66FFD54F);

            setColor(BLOCK_LINE, 0x33546E7A);
            setColor(BLOCK_LINE_CURRENT, 0x6690CAF9);
            setColor(SIDE_BLOCK_LINE, 0x44546E7A);
            setColor(STICKY_SCROLL_DIVIDER, 0x66546E7A);

            setColor(COMPLETION_WND_BACKGROUND, 0xFF37474F);
            setColor(COMPLETION_WND_CORNER, 0xFF37474F);
            setColor(COMPLETION_WND_TEXT_PRIMARY, 0xFFF3F6F8);
            setColor(COMPLETION_WND_TEXT_SECONDARY, 0xFFB0BEC5);
            setColor(COMPLETION_WND_TEXT_MATCHED, 0xFF81D4FA);
            setColor(COMPLETION_WND_ITEM_CURRENT, 0x225FA8D3);

            setColor(TEXT_ACTION_WINDOW_BACKGROUND, 0xFF37474F);
            setColor(TEXT_ACTION_WINDOW_ICON_COLOR, 0xFFECEFF1);
            setColor(DIAGNOSTIC_TOOLTIP_BACKGROUND, 0xFF37474F);
            setColor(DIAGNOSTIC_TOOLTIP_BRIEF_MSG, 0xFFF3F6F8);
            setColor(DIAGNOSTIC_TOOLTIP_DETAILED_MSG, 0xFFB0BEC5);
            setColor(DIAGNOSTIC_TOOLTIP_ACTION, 0xFF81D4FA);

            setColor(KEYWORD, 0xFF82AAFF);
            setColor(COMMENT, 0xFF7F8C98);
            setColor(LITERAL, 0xFFC3E88D);
            setColor(OPERATOR, 0xFF89DDFF);
            setColor(ANNOTATION, 0xFFFFCB6B);
            setColor(FUNCTION_NAME, 0xFF80CBC4);
            setColor(IDENTIFIER_NAME, 0xFFF3F6F8);
            setColor(IDENTIFIER_VAR, 0xFFE5DADA);
            setColor(ATTRIBUTE_NAME, 0xFFFFCB6B);
            setColor(ATTRIBUTE_VALUE, 0xFFC3E88D);
            setColor(HTML_TAG, 0xFFF07178);

            setColor(PROBLEM_ERROR, 0xCCEF5350);
            setColor(PROBLEM_WARNING, 0xCCD4A017);
            setColor(PROBLEM_TYPO, 0xAA66BB6A);

            setColor(NON_PRINTABLE_CHAR, 0x55B0BEC5);
            setColor(HARD_WRAP_MARKER, 0x33546E7A);
        }
    }
}