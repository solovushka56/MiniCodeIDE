package com.skeeper.minicode.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.Stack;

public class TextUndoManager {

    private final Stack<TextChange> undoStack = new Stack<>();
    private final Stack<TextChange> redoStack = new Stack<>();
    private boolean isUpdating = false;

    public void beginUpdate(EditText editText) {
        if (!isUpdating) {
            undoStack.push(new TextChange(editText.getText().toString()));
            isUpdating = true;
        }
    }

    public void endUpdate(EditText editText) {
        if (isUpdating) {
            undoStack.peek().newText = editText.getText().toString();
            isUpdating = false;
        }
    }

    public void undo() {
        if (canUndo()) {
            TextChange change = undoStack.pop();
            redoStack.push(change);
            change.editText.setText(change.oldText);
        }
    }

    public void redo() {
        if (canRedo()) {
            TextChange change = redoStack.pop();
            undoStack.push(change);
            change.editText.setText(change.newText);
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

        TextChange(String oldText) {
            this.oldText = oldText;
        }
    }
}