package com.skeeper.minicode.presentation.ui.activity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.domain.contracts.other.callbacks.IKeyPressedListener;
import com.skeeper.minicode.presentation.ui.dialog.CreateFileDialog;
import com.skeeper.minicode.presentation.ui.dialog.DeleteFileDialog;
import com.skeeper.minicode.presentation.ui.dialog.MoveFileDialog;
import com.skeeper.minicode.presentation.ui.dialog.RenameFileDialog;
import com.skeeper.minicode.presentation.ui.fragment.CodeEditorFragment;
import com.skeeper.minicode.presentation.ui.other.FileTreeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.adapters.SnippetsAdapter;
import com.skeeper.minicode.databinding.ActivityCodeEditorBinding;
import com.skeeper.minicode.presentation.viewmodels.CompileViewModel;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;
import com.skeeper.minicode.presentation.viewmodels.SnippetViewModel;
import com.skeeper.minicode.presentation.viewmodels.factory.FileViewModelFactory;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.domain.contracts.other.callbacks.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.SnippetModel;

import com.skeeper.minicode.core.singleton.ProjectManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.rosemoe.sora.widget.CodeEditor;

@AndroidEntryPoint
public class CodeEditorActivity extends AppCompatActivity implements
        IFileTreeListener,
        IKeyPressedListener {


    @Inject ProjectManager projectManager;



    private ActivityCodeEditorBinding binding;
    private View rootView;
    private RecyclerView bottomPanel;
    private final int minKeyboardHeight = 100;

    private SnippetViewModel snippetViewModel;
    private FilesViewModel filesViewModel;
    private CompileViewModel compileViewModel;

    private final Map<FileItem, CodeEditorFragment> cachedFragments = new HashMap<>();
    private String projectName = null;
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
        getWindow().setNavigationBarColor(getResources().getColor(R.color.activity_bottom));
        projectName = getIntent().getStringExtra("projectName");

        compileViewModel = new ViewModelProvider(this).get(CompileViewModel.class);
        snippetViewModel = new ViewModelProvider(this).get(SnippetViewModel.class);


        snippetViewModel.getSnippets().observe(this, snippets -> {
            var recyclerView = binding.symbolsPanel;
            var adapter = new SnippetsAdapter(this, snippets, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this, RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        });
        snippetViewModel.loadSnippetsAsync();


        rootView = binding.main;
        bottomPanel = binding.symbolsPanel;

        binding.backButton.setOnClickListener(v -> {
            finish();
        });
        binding.openFolderButton.setOnClickListener(v -> {
            hideKeyboard();
            if (currentCodeFragment != null)
                getCurrentCodeView().clearFocus();
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });
        binding.optionsButton.setOnClickListener(v -> {
            showOptionsContext(binding.optionsButton);
        });
        binding.recreateButton.setOnClickListener( v-> {
            Intent intent = new Intent(
                    CodeEditorActivity.this,
                    CodeEditorActivity.class);
            intent.putExtra("projectName", projectName);
            startActivity(intent);
            finish();
        });
        binding.saveButton.setOnClickListener(v -> {
            if (currentCodeFragment == null) return;
            Toast.makeText(this, "File Saved!", Toast.LENGTH_SHORT).show();
            filesViewModel.saveFile(
                    currentCodeFragment.getBoundFileItem().getDirectory(),
                    getCurrentCodeView().getText().toString());
        });
        binding.compilePanelShowButton.setOnClickListener(v -> {
            showCompilePanel();
        });




        setupKeyboardListener();

        // filesys setup
        File projectDir = projectManager.getProjectDir(projectName);
        FileTreeView fileSystemView = new FileTreeView(this);
        fileSystemView.init(this, binding.leftDrawer, projectDir);
        binding.leftDrawer.addView(fileSystemView);
        var drawerLayout = binding.drawerLayout;

        TextView projNameView = findViewById(R.id.projectNameTextView);
        projNameView.setText(projectName);
        fileSystemView.setOnCreateFileButtonListener(v -> {
            onAddFileSelected(new FileItem(
                    projectDir,
                    projectDir.getName(),
                    true,
                    0
            ));
        });
        fileSystemView.setOnClickCloseListener(v -> {
            drawerLayout.close();
        });



        filesViewModel = new ViewModelProvider(
                this, new FileViewModelFactory(projectManager
                .getProjectDir(projectName)))
                .get(FilesViewModel.class);
        filesViewModel.getFiles().observe(this, (fileItems) -> {
            fileSystemView.updateFileItems(this, fileItems);
        });


        binding.runCodeButton.setOnClickListener(v -> {
            String stdin = binding.compileConsole.getText() != null
                    ? binding.compileConsole.getText().toString() : "";
            compileViewModel.compileAsync(projectName, stdin);
            binding.compileProgress.setVisibility(VISIBLE);
        });
        binding.clearConsole.setOnClickListener(v -> {
            binding.compileConsole.setText("");
        });
        compileViewModel.getCompileResult().observe(this, compileResponse -> {
            var startConsoleText = binding.compileConsole.getText().toString();
            String stdinNewLine = startConsoleText.trim().isEmpty() ? "" : "\n";
            binding.compileProgress.setVisibility(INVISIBLE);
            binding.compileConsole.setText(
                    binding.compileConsole.getText() + stdinNewLine +
                    compileResponse.output() + "\n" +
                    compileResponse.errors()
            );
        });

        compileViewModel.getCompileException().observe(this, error -> {
            binding.compileProgress.setVisibility(INVISIBLE);
            binding.compileConsole.setText(error);
        });

        binding.hideCompilePanelBtn.setOnClickListener(v -> {
            hideCompilePanel();
        });


        binding.buttonUndo.setOnClickListener(v -> performUndo());
        binding.buttonRedo.setOnClickListener(v -> performRedo());
//        updateButtonStates();
    }


    private void showCompilePanel() {
        hideKeyboard();
        if (currentCodeFragment != null)
            getCurrentCodeView().clearFocus();


        if (binding.compilePanel.getVisibility() == VISIBLE) return;
        binding.compilePanel.measure(
                View.MeasureSpec.makeMeasureSpec(binding.compilePanel.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        final int targetHeight = binding.compilePanel.getMeasuredHeight();

        binding.compilePanel.setVisibility(VISIBLE);
        binding.compilePanel.setAlpha(0f);
        ViewGroup.LayoutParams layoutParams = binding.compilePanel.getLayoutParams();
        layoutParams.height = 0;
        binding.compilePanel.setLayoutParams(layoutParams);

        ValueAnimator heightAnimator = ValueAnimator.ofInt(0, targetHeight);
        heightAnimator.setDuration(400);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = binding.compilePanel.getLayoutParams();
            params.height = value;
            binding.compilePanel.setLayoutParams(params);
        });
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        alphaAnimator.setDuration(400);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            binding.compilePanel.setAlpha(value);
        });
        heightAnimator.start();
        alphaAnimator.start();
    }

    private void hideCompilePanel() {
        if (binding.compilePanel.getVisibility() != VISIBLE) return;

        final int initialHeight = binding.compilePanel.getHeight();
        ValueAnimator heightAnimator = ValueAnimator.ofInt(initialHeight, 0);
        heightAnimator.setDuration(400);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = binding.compilePanel.getLayoutParams();
            params.height = value;
            binding.compilePanel.setLayoutParams(params);
        });
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(1f, 0f);
        alphaAnimator.setDuration(400);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            binding.compilePanel.setAlpha(value);
        });
        heightAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {}

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                binding.compilePanel.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {}

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {}
        });
        heightAnimator.start();
        alphaAnimator.start();
    }
    private void showOptionsContext(View anchorView) {
        View menuView = LayoutInflater.from(anchorView.getContext())
                .inflate(R.layout.editor_options_menu, null);

        PopupWindow popupWindow = new PopupWindow(
                menuView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setElevation(16f);
//        popupWindow.setAnimationStyle(R.style.CustomPopupAnimation);

        View editor_options = menuView.findViewById(R.id.menu_code_editor_options);

        editor_options.setOnClickListener(v -> {
            startActivity(new Intent(
                    CodeEditorActivity.this,
                    CodeEditorSettingsActivity.class));
            popupWindow.dismiss();
        });

        menuView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                popupWindow.dismiss();
                return true;
            }
            return false;
        });

        popupWindow.showAsDropDown(anchorView);

        menuView.post(() -> {
            popupWindow.update(
                    anchorView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        });
    }





    public void setNewCodeEditorFragment(FileItem fileItem) {
        if (cachedFragments.get(fileItem) == null) {
            currentCodeFragment = new CodeEditorFragment(fileItem, binding.buttonUndo, binding.buttonRedo);
            cachedFragments.put(fileItem, currentCodeFragment);
        }
        else {
            currentCodeFragment = cachedFragments.get(fileItem);
        }
        setCodeFragment(currentCodeFragment);

    }

    public void setCodeFragment(CodeEditorFragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_fade_in, R.anim.slide_down_fade_out);
        fragmentTransaction.replace(binding.codeViewFrame.getId(), newFragment);
        fragmentTransaction.commit();
    }



    private CodeEditor getCurrentCodeView() {
        return currentCodeFragment.editor;
    }


    private void updateButtonStates() {
        CodeEditor editor = getCurrentCodeView();
        if (editor == null) {
            binding.buttonUndo.setEnabled(false);
            binding.buttonRedo.setEnabled(false);
            return;
        }

        boolean canUndo = false;
        boolean canRedo = false;

        try {
            canUndo = editor.canUndo();
            canRedo = editor.canRedo();
        } catch (Throwable ignored) {
            try {
                canUndo = editor.getText().canUndo();
                canRedo = editor.getText().canRedo();
            } catch (Throwable ignored2) {
            }
        }

        binding.buttonUndo.setEnabled(canUndo);
        binding.buttonRedo.setEnabled(canRedo);

        binding.buttonUndo.setAlpha(canUndo ? 1f : 0.45f);
        binding.buttonRedo.setAlpha(canRedo ? 1f : 0.45f);
    }

    private void performUndo() {
        CodeEditor editor = getCurrentCodeView();
        if (editor == null) return;

        editor.undo();
        updateButtonStates();
    }

    private void performRedo() {
        CodeEditor editor = getCurrentCodeView();
        if (editor == null) return;

        editor.redo();
        updateButtonStates();
    }


    private void setupKeyboardListener() {

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            rootView.getWindowVisibleDisplayFrame(rect);

            int screenHeight = rootView.getRootView().getHeight();
            int keyboardHeight = screenHeight - rect.bottom;

            if (keyboardHeight > convertDpToPx(minKeyboardHeight)) {
                int fillerTab = convertDpToPx(21);
                bottomPanel.getLayoutParams().height = keyboardHeight + fillerTab;
                bottomPanel.setVisibility(VISIBLE);

            } else {
                bottomPanel.setVisibility(GONE);
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

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onFileClick(FileItem fileItem) {
        setNewCodeEditorFragment(fileItem);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (binding.fileTipView.getVisibility() == VISIBLE)
            binding.fileTipView.setVisibility(INVISIBLE);
    }

    @Override
    public void onFolderClick(FileItem fileItem) {

    }

    @Override
    public void onFolderExpandedStateChanged(File folder, boolean isExpanded) {
        filesViewModel.updateFolderExpandedState(folder, isExpanded);
    }

    @Override
    public void onRenameSelected(FileItem item) {
        new RenameFileDialog().show(item, filesViewModel, this);
    }

    @Override
    public void onDeleteSelected(FileItem item) {
        new DeleteFileDialog().show(item,
                currentCodeFragment,
                cachedFragments,
                filesViewModel,
                this
        );
    }

    @Override
    public void onCopyPathSelected(FileItem item) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("File path", item.getDirectory().getPath());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Path Copied!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoveSelected(FileItem item) {
        new MoveFileDialog().show(item, filesViewModel, this);
    }

    @Override
    public void onAddFileSelected(FileItem item) {
        new CreateFileDialog().show(item, filesViewModel, this);
    }


    @Override
    public void onArrowPressed(int direction) {
        CodeEditor editor = getCurrentCodeView();
        if (editor == null) return;

        switch (direction) {
            case 0:
                moveCursorLeft(editor);
                break;
            case 1:
                moveCursorRight(editor);
                break;
            case 2:
                moveCursorUp(editor);
                break;
            case 3:
                moveCursorDown(editor);
                break;
        }
    }



    private void moveCursorLeft(CodeEditor editor) {
        if (editor == null) return;

        int line = getCursorLine(editor);
        int column = getCursorColumn(editor);

        if (column > 0) {
            editor.setSelection(line, column - 1);
            return;
        }

        if (line > 0) {
            int prevLine = line - 1;
            int prevLineLength = editor.getText().getColumnCount(prevLine);
            editor.setSelection(prevLine, prevLineLength);
        }
    }

    private void moveCursorRight(CodeEditor editor) {
        if (editor == null) return;

        int line = getCursorLine(editor);
        int column = getCursorColumn(editor);
        int currentLineLength = editor.getText().getColumnCount(line);
        int lastLine = editor.getText().getLineCount() - 1;

        if (column < currentLineLength) {
            editor.setSelection(line, column + 1);
            return;
        }

        if (line < lastLine) {
            editor.setSelection(line + 1, 0);
        }
    }
    private void moveCursorUp(CodeEditor editor) {
        if (editor == null) return;

        int line = getCursorLine(editor);
        int column = getCursorColumn(editor);

        if (line <= 0) return;

        int targetLine = line - 1;
        int targetColumn = Math.min(column, editor.getText().getColumnCount(targetLine));
        editor.setSelection(targetLine, targetColumn);
    }

    private void moveCursorDown(CodeEditor editor) {
        if (editor == null) return;

        int line = getCursorLine(editor);
        int column = getCursorColumn(editor);
        int lastLine = editor.getText().getLineCount() - 1;

        if (line >= lastLine) return;

        int targetLine = line + 1;
        int targetColumn = Math.min(column, editor.getText().getColumnCount(targetLine));
        editor.setSelection(targetLine, targetColumn);
    }

    @Override
    public void onKeyPressed(SnippetModel pressedKey) {
        var currentCodeView = getCurrentCodeView();
        if (currentCodeView == null) {
            Log.e("INIT_E", "codeview is null");
            return;
        }
        writeKeySymbol(pressedKey);
    }

    private void writeKeySymbol(SnippetModel keySymbolItemModel) {
        CodeEditor editor = getCurrentCodeView();
        if (editor == null || keySymbolItemModel == null) {
            Log.e("INIT_E", "key symbol key not inited");
            return;
        }

        String text = keySymbolItemModel.getSymbolValue();
        if (text == null) return;

        insertTextAtCursor(editor, text);
    }

    private void insertTextAtCursor(CodeEditor editor, String text) {
        if (editor == null || text == null) return;

        int line = getCursorLine(editor);
        int column = getCursorColumn(editor);

        editor.getText().insert(line, column, text);

        moveCursorAfterInsertedText(editor, line, column, text);

        updateButtonStates();
    }
    private void moveCursorAfterInsertedText(CodeEditor editor, int startLine, int startColumn, String insertedText) {
        if (editor == null || insertedText == null) return;

        String[] parts = insertedText.split("\n", -1);

        if (parts.length == 1) {
            editor.setSelection(startLine, startColumn + insertedText.length());
            return;
        }

        int newLine = startLine + parts.length - 1;
        int newColumn = parts[parts.length - 1].length();
        editor.setSelection(newLine, newColumn);
    }
    private int getCursorLine(CodeEditor editor) {
        return editor.getCursor().getLeftLine();
    }

    private int getCursorColumn(CodeEditor editor) {
        return editor.getCursor().getLeftColumn();
    }


}