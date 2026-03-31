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
import android.text.Editable;
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

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.domain.contracts.other.callbacks.IKeyPressedListener;
import com.skeeper.minicode.presentation.ui.component.CompilePanelView;
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



    private CodeView getCurrentCodeView() {
        return currentCodeFragment.codeView;
    }

    private UndoRedoManager getCurrentUndoRedoManager() {
        return currentCodeFragment.undoRedoManager;
    }


    private void updateButtonStates() {
        binding.buttonUndo.setEnabled(getCurrentUndoRedoManager().canUndo());
        binding.buttonRedo.setEnabled(getCurrentUndoRedoManager().canRedo());
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
        int cursorPosition = getCurrentCodeView().getSelectionStart();
        switch (direction) {
            case 0:
                if (cursorPosition > 0) getCurrentCodeView().setSelection(cursorPosition - 1);
                break;
            case 1:
                if (cursorPosition < getCurrentCodeView().getText().length())
                    getCurrentCodeView().setSelection(cursorPosition + 1);
                break;
            case 2:
                moveCursorVertically(getCurrentCodeView(), -1);
                break;
            case 3:
                moveCursorVertically(getCurrentCodeView(), 1);
                break;
        }
    }

    private void moveCursorVertically(EditText editText, int direction) {
        int cursorPosition = editText.getSelectionStart();
        Layout layout = editText.getLayout();

        if (layout != null) {
            int line = layout.getLineForOffset(cursorPosition);
            int newLine = line + direction;

            if (newLine >= 0 && newLine < layout.getLineCount()) {
                float x = layout.getPrimaryHorizontal(cursorPosition);
                int newPos = layout.getOffsetForHorizontal(newLine, x);
                editText.setSelection(newPos);
            }
        }
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
        var currentCodeView = getCurrentCodeView();
        if (currentCodeView == null) {
            Log.e("INIT_E", "key symbol key not inited");
            return;
        }
        int cursorPosition = currentCodeView.getSelectionEnd();
        Editable editable = currentCodeView.getText();
        editable.insert(cursorPosition, keySymbolItemModel.getSymbolValue());
        currentCodeView.setSelection(cursorPosition + keySymbolItemModel.getSymbolValue().length());
    }
}