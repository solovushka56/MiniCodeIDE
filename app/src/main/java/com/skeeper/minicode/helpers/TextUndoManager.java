package com.skeeper.minicode.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.Stack;

public class TextUndoManager {
    private final Stack<TextChange> undoStack = new Stack<>();
    private final Stack<TextChange> redoStack = new Stack<>();
    private boolean isUpdating = false;
    private EditText editText;

    public TextUndoManager(EditText editText) {
        this.editText = editText;
        setupTextWatcher();
    }

    private void setupTextWatcher() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!isUpdating) {
                    beginUpdate();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating) {
                    endUpdate();
                }
            }
        });
    }

    private void beginUpdate() {
        undoStack.push(new TextChange(editText.getText().toString(), editText));
        redoStack.clear();
        isUpdating = true;
    }

    private void endUpdate() {
        if (!undoStack.isEmpty()) {
            undoStack.peek().newText = editText.getText().toString();
        }
        isUpdating = false;
    }

    public void undo() {
        if (canUndo()) {
            isUpdating = true;
            TextChange change = undoStack.pop();
            redoStack.push(change);
            editText.setText(change.oldText);
            editText.setSelection(change.oldText.length());
            isUpdating = false;
        }
    }

    public void redo() {
        if (canRedo()) {
            isUpdating = true;
            TextChange change = redoStack.pop();
            undoStack.push(change);
            editText.setText(change.newText);
            editText.setSelection(change.newText.length());
            isUpdating = false;
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    private static class TextChange {
        String oldText;
        String newText;
        EditText editText;

        TextChange(String oldText, EditText editText) {
            this.oldText = oldText;
            this.editText = editText;
        }
    }
}
//public class TextUndoManager {
//
//    private final Stack<TextChange> undoStack = new Stack<>();
//    private final Stack<TextChange> redoStack = new Stack<>();
//    private boolean isUpdating = false;
//
//    public void beginUpdate(EditText editText) {
//        if (!isUpdating) {
//            undoStack.push(new TextChange(editText.getText().toString()));
//            isUpdating = true;
//        }
//    }
//
//    public void endUpdate(EditText editText) {
//        if (isUpdating) {
//            undoStack.peek().newText = editText.getText().toString();
//            isUpdating = false;
//        }
//    }
//
//    public void undo() {
//        if (canUndo()) {
//            TextChange change = undoStack.pop();
//            redoStack.push(change);
//            change.editText.setText(change.oldText);
//        }
//    }
//
//    public void redo() {
//        if (canRedo()) {
//            TextChange change = redoStack.pop();
//            undoStack.push(change);
//            change.editText.setText(change.newText);
//        }
//    }
//
//    public boolean canUndo() {
//        return !undoStack.isEmpty();
//    }
//
//    public boolean canRedo() {
//        return !redoStack.isEmpty();
//    }
//
//    private static class TextChange {
//        String oldText;
//        String newText;
//        EditText editText;
//
//        TextChange(String oldText) {
//            this.oldText = oldText;
//        }
//    }
//}