package com.skeeper.minicode.presentation.ui.activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.domain.contracts.other.callbacks.IKeyPressedListener;
import com.skeeper.minicode.presentation.ui.fragment.CodeEditorFragment;
import com.skeeper.minicode.presentation.ui.other.FileTreeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.adapters.SnippetsAdapter;
import com.skeeper.minicode.databinding.ActivityCodeEditorBinding;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;
import com.skeeper.minicode.presentation.viewmodels.SnippetViewModel;
import com.skeeper.minicode.presentation.viewmodels.factory.FileViewModelFactory;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.domain.contracts.other.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.SnippetModel;

import com.skeeper.minicode.core.singleton.SnippetsManager;
import com.skeeper.minicode.core.singleton.ProjectManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

// todo add viewmodel
@AndroidEntryPoint
public class CodeEditorActivity extends AppCompatActivity
        implements IFileTreeListener, IKeyPressedListener {

    @Inject ProjectManager projectManager;



    private ActivityCodeEditorBinding binding;
    private View rootView;
    private RecyclerView bottomPanel;
    private final int minKeyboardHeight = 100;
    private String projectName = null;
    private CodeEditorFragment currentCodeFragment = null;

    private SnippetViewModel snippetViewModel;
    private FilesViewModel filesViewModel;

    private final Map<FileItem, CodeEditorFragment> cachedFragments = new HashMap<>();

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
        getWindow().setNavigationBarColor(getResources().getColor(R.color.transparent));
        projectName = getIntent().getStringExtra("projectName");


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
            startActivity(new Intent(
                    CodeEditorActivity.this,
                    CodeEditorSettingsActivity.class));
        });
        binding.recreateButton.setOnClickListener( v-> {
            recreate();
        });
        binding.saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "File Saved!", Toast.LENGTH_SHORT).show();
            filesViewModel.saveFile(
                    currentCodeFragment.getBoundFileItem().getDirectory(),
                    getCurrentCodeView().getText().toString());
        });
        setupKeyboardListener();

        // filesys setup
        File projectDir = projectManager.getProjectDir(projectName);
        FileTreeView fileSystemView = new FileTreeView(this);
        fileSystemView.init(this, binding.leftDrawer, projectDir);
        binding.leftDrawer.addView(fileSystemView);

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


        filesViewModel = new ViewModelProvider(
                this, new FileViewModelFactory(projectManager
                .getProjectDir(projectName)))
                .get(FilesViewModel.class);
        filesViewModel.getFiles().observe(this, (fileItems) ->
                fileSystemView.updateFileItems(this, fileItems));

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
                int fillerTab = 40;
                bottomPanel.getLayoutParams().height = keyboardHeight + fillerTab;
                bottomPanel.setVisibility(VISIBLE);

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


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
    public void onFileRename(FileItem fileItem) {

    }

    @Override
    public void onFolderLongClick(FileItem fileItem) {
    }

    @Override
    public void onFileLongClick(FileItem fileItem) {
    }

    @Override
    public void onFolderAdd(FileItem fileItem) {

    }

    @Override
    public void onFileAdd(FileItem fileItem) {

    }

    @Override
    public void onRenameSelected(FileItem item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rename_file, null);

        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        TextInputEditText input = dialogView.findViewById(R.id.newNameTextEdit);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        input.setText(item.getName());

        positiveButton.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();
            File parentDir = item.getDirectory().getParentFile();

            File renamedFile = new File(parentDir, newName);

            if (renamedFile.exists()) {
                input.setError("Item with this name already exists!");
                return;
            }

            filesViewModel.renameFile(item.getDirectory(), newName);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onDeleteSelected(FileItem item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_remove_file, null);

        TextView textView = dialogView.findViewById(R.id.removeFileText);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        textView.setText("Remove " + item.getName() + "?");

        positiveButton.setOnClickListener(v -> {
            filesViewModel.deleteFile(item.getDirectory());

            if (currentCodeFragment == cachedFragments.get(item))
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (currentCodeFragment != null) {
                    fragmentManager.beginTransaction()
                            .remove(currentCodeFragment)
                            .commit();
                }
            }
            cachedFragments.remove(item);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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
//        var builder = new MaterialAlertDialogBuilder(this);
//        builder.setTitle("Move " + item.getName());
//
//        EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        input.setHint("New directory path");
//        builder.setView(input);
//
//        builder.setPositiveButton("Move", (dialog, w) -> {
//            String newPath = input.getText().toString().trim();
//            File newDir = new File(newPath);
//            if (newDir.isDirectory()) {
//                filesViewModel.moveFile(item.getDirectory(), new File(newPath));
//            }
//            else {
//                Toast.makeText(this,
//                        "The folder directory is entered incorrectly!",
//                        Toast.LENGTH_SHORT).show();
//                input.setText("");
//                onMoveSelected(item);
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_move_file, null);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);

        TextInputEditText pathInput = dialogView.findViewById(R.id.enterNewDirectory);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String newPath = pathInput.getText().toString().trim();
            if (!new File(newPath).exists()) {
                pathInput.setError("Directory is incorrect");
                return;
            }
            filesViewModel.moveFile(item.getDirectory(), new File(newPath));
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }
    
    @Override
    public void onAddFileSelected(FileItem item) {
        if (!item.isDirectory()) return;

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_create_file, null);

        TextView title =  dialogView.findViewById(R.id.dialogTitle);
        TextInputEditText fileNameInput = dialogView.findViewById(R.id.enterName);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        RadioButton optionFile = dialogView.findViewById(R.id.optionFileCreate);
        RadioButton optionFolder = dialogView.findViewById(R.id.optionFolderCreate);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == optionFile.getId()) {
                title.setText("Create new File");
            } else if (checkedId == optionFolder.getId()) {
                title.setText("Create new Folder");
            }
        });

        positiveButton.setOnClickListener(v -> {
            String fileName = fileNameInput.getText().toString().trim();
            String path = item.getDirectory().getPath();

            if (fileName.isEmpty()) {
                fileNameInput.setError("Enter file name");
                return;
            }

            if (optionFile.isChecked() && !fileName.contains(".")) {
                fileNameInput.setError("Add file extension");
                return;
            }

            if (optionFile.isChecked())
                filesViewModel.createFile(new File(path, fileName));
            else if(optionFolder.isChecked())
                filesViewModel.createFolder(new File(path), fileName);

            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        fileNameInput.requestFocus();
        fileNameInput.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(fileNameInput, InputMethodManager.SHOW_IMPLICIT);
        }, 100);
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