package com.skeeper.minicode.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;

public class UndoRedoManager {

    private final EditText editText;
    private final Deque<TextAction> undoStack = new ArrayDeque<>();
    private final Deque<TextAction> redoStack = new ArrayDeque<>();
    private Timer timer = new Timer();
    private final long DELAY = 500;

    private String lastText = "";
    private int lastCursorPosition = 0;
    private TextWatcher textWatcher;

    public UndoRedoManager(EditText editText) {
        this.editText = editText;
        initializeTextWatcher();
    }

    private void initializeTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastText = s.toString();
                lastCursorPosition = editText.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String newText = s.toString();
                        int newCursor = editText.getSelectionStart();

                        if (!newText.equals(lastText)) {
                            undoStack.push(new TextAction(lastText, newText,
                                    lastCursorPosition, newCursor));
                            redoStack.clear();
                        }
                        lastText = newText;
                        lastCursorPosition = newCursor;
                    }
                }, DELAY);
            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    public void undo() {
        if (canUndo()) {
            TextAction action = undoStack.pop();
            redoStack.push(action);
            applyTextUpdate(action.oldText, action.oldCursor);
        }
    }

    public void redo() {
        if (canRedo()) {
            TextAction action = redoStack.pop();
            undoStack.push(action);
            applyTextUpdate(action.newText, action.newCursor);
        }
    }

    private void applyTextUpdate(String text, int cursorPosition) {
        editText.removeTextChangedListener(textWatcher);
        editText.setText(text);
        editText.setSelection(Math.min(cursorPosition, text.length()));
        lastText = text;
        lastCursorPosition = cursorPosition;
        editText.addTextChangedListener(textWatcher);
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    private static class TextAction {
        final String oldText;
        final String newText;
        final int oldCursor;
        final int newCursor;

        TextAction(String oldText, String newText, int oldCursor, int newCursor) {
            this.oldText = oldText;
            this.newText = newText;
            this.oldCursor = oldCursor;
            this.newCursor = newCursor;
        }
    }
}