package com.skeeper.minicode.presentation.ui.activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.skeeper.minicode.presentation.ui.fragment.CodeEditorFragment;
import com.skeeper.minicode.presentation.ui.other.FileTreeView;
import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.adapters.SnippetsAdapter;
import com.skeeper.minicode.databinding.ActivityCodeEditorBinding;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;
import com.skeeper.minicode.presentation.viewmodels.factory.FileViewModelFactory;
import com.skeeper.minicode.utils.helpers.UndoRedoManager;
import com.skeeper.minicode.utils.helpers.VibrationManager;
import com.skeeper.minicode.domain.contracts.other.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.SnippetModel;

import com.skeeper.minicode.core.singleton.SnippetsManager;
import com.skeeper.minicode.core.singleton.ProjectManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CodeEditorActivity extends AppCompatActivity
        implements IFileTreeListener, IKeyPressedListener {

    @Inject
    ProjectManager projectManager;

    private ActivityCodeEditorBinding binding;
    private View rootView;
    private RecyclerView bottomPanel;
    private final int minKeyboardHeight = 100;
    private String projectName = null;
    private List<SnippetModel> keySymbolModels;
    private FilesViewModel filesViewModel;
    private CodeEditorFragment currentCodeFragment = null;
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

        keySymbolModels = SnippetsManager.loadList(
                this, "keySymbolsData.json");


//        currentCodeFragment = new CodeEditorFragment(binding.buttonUndo, binding.buttonRedo);
//        setCodeFragment(currentCodeFragment);



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
            currentCodeFragment.saveFile(getCurrentCodeView().getText().toString());
            Toast.makeText(this, "File Saved!", Toast.LENGTH_SHORT).show();
        });
        setupSnippetsRecycler();
        setupKeyboardListener();
//        setupButtonListeners(getCurrentUndoRedoManager(), binding.buttonUndo, binding.buttonRedo);


        // filesys setup
        FileTreeView fileSystemView = new FileTreeView(this);
        fileSystemView.init(this, binding.leftDrawer,
                projectManager.getProjectDir(projectName));
        binding.leftDrawer.addView(fileSystemView);
        TextView projNameView = findViewById(R.id.projectNameTextView);
        projNameView.setText(projectName);




        filesViewModel = new ViewModelProvider(
                this, new FileViewModelFactory(projectManager
                .getProjectDir(projectName)))
                .get(FilesViewModel.class);
        filesViewModel.getFileRepositoryList().observe(this, (fileItems) ->
                fileSystemView.updateFileItems(this, fileItems));

    }


    public void setNewCodeEditorFragment(FileItem fileItem)
    {
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


    private void setupButtonListeners(UndoRedoManager undoRedoManager, ImageButton btnUndo, ImageButton btnRedo) {

        btnUndo.setOnClickListener(v -> {
            if (!undoRedoManager.canUndo()) return;
            VibrationManager.vibrate(30L, this);
            undoRedoManager.undo();
            updateButtonStates();
        });
        btnRedo.setOnClickListener(v -> {
            if (!undoRedoManager.canRedo()) return;
            VibrationManager.vibrate(30L, this);
            undoRedoManager.redo();
            updateButtonStates();
        });
    }
    private void setupTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
//                updateButtonStates();
            }
        });
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
    public void onSymbolClick(View view) {
        Button btn = (Button) view;
        EditText editText = getCurrentCodeView();
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
            binding.symbolsPanel.setVisibility(VISIBLE);
        }
        else {
            binding.symbolsPanel.setVisibility(View.GONE);
        }

    }
    private void setupSnippetsRecycler() {
        var recyclerView = binding.symbolsPanel;
        var adapter = new SnippetsAdapter(this, keySymbolModels, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
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
        Toast.makeText(this, "long folder click", Toast.LENGTH_SHORT).show();

//
//        View popupView = LayoutInflater.from(this)
//                .inflate(R.layout.popup_create_file, null);
//
//        PopupWindow popupWindow = new PopupWindow(
//                popupView,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                true
//        );
//
//        Button closeBtn = popupView.findViewById(R.id.popupButtonConfirm);
//        closeBtn.setOnClickListener(v -> popupWindow.dismiss());
//
//        popupWindow.setTouchInterceptor((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                popupWindow.dismiss();
//                return true;
//            }
//            return false;
//        });
//
//        popupWindow.showAsDropDown(binding.main, 0, 0, Gravity.CENTER);

    }

    @Override
    public void onFileLongClick(FileItem fileItem) {
        Toast.makeText(this, "long file click", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFolderAdd(FileItem fileItem) {

    }

    @Override
    public void onFileAdd(FileItem fileItem) {

    }

    @Override
    public void onRenameSelected(FileItem item) {

    }

    @Override
    public void onDeleteSelected(FileItem item) {

    }

    @Override
    public void onCopyPathSelected(FileItem item) {

    }

    @Override
    public void onMoveSelected(FileItem item, String newPath) {

    }

    @Override
    public void onAddFileSelected(FileItem parentFolder) {

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