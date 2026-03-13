package com.skeeper.minicode.presentation.ui.dialog.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skeeper.minicode.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateFileDialogFragment extends DialogFragment {

    public interface Listener {
        void onCreateFile(String fileName, String extension);
        void onCreateFolder(String folderName);
    }

    private static final String ARG_KEY = "arg_dir_path";

    public static CreateFileDialogFragment newInstance(String directoryPath) {
        CreateFileDialogFragment f = new CreateFileDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY, directoryPath);
        f.setArguments(b);
        return f;
    }

    private Listener listener;

    public void setListener(Listener l) {
        this.listener = l;
    }

    private TextInputEditText fileNameInput;
    private TextInputEditText fileExtensionInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_file, null);

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        fileNameInput = dialogView.findViewById(R.id.enterName);
        fileExtensionInput = dialogView.findViewById(R.id.enterExtension);
        TextView dot = dialogView.findViewById(R.id.dotView);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        RadioButton optionFile = dialogView.findViewById(R.id.optionFileCreate);
        RadioButton optionFolder = dialogView.findViewById(R.id.optionFolderCreate);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        TextInputLayout nameInputLayout = (TextInputLayout) fileNameInput.getParent().getParent();
        TextInputLayout extensionInputLayout = (TextInputLayout) fileExtensionInput.getParent().getParent();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == optionFile.getId()) {
                title.setText("Create new File");

                dot.setVisibility(View.VISIBLE);
                extensionInputLayout.setVisibility(View.VISIBLE);
                fileExtensionInput.setText("");

                LinearLayout.LayoutParams nameParams = (LinearLayout.LayoutParams) nameInputLayout.getLayoutParams();
                nameParams.width = 0;
                nameParams.weight = 2.0f;
                nameInputLayout.setLayoutParams(nameParams);

                LinearLayout.LayoutParams extensionParams = (LinearLayout.LayoutParams) extensionInputLayout.getLayoutParams();
                extensionParams.width = 0;
                extensionParams.weight = 1.0f;
                extensionInputLayout.setLayoutParams(extensionParams);

                LinearLayout.LayoutParams dotParams = (LinearLayout.LayoutParams) dot.getLayoutParams();
                dotParams.width = 10;
                dotParams.weight = 0.01f;
                dot.setLayoutParams(dotParams);

            } else if (checkedId == optionFolder.getId()) {
                title.setText("Create new Folder");

                dot.setVisibility(View.GONE);
                extensionInputLayout.setVisibility(View.GONE);
                fileExtensionInput.setText("");

                LinearLayout.LayoutParams nameParams = (LinearLayout.LayoutParams) nameInputLayout.getLayoutParams();
                nameParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                nameParams.weight = 0;
                nameInputLayout.setLayoutParams(nameParams);
            }
        });

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(dialogView);
        Dialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String fileName = fileNameInput.getText().toString().trim();
            String fileExt = fileExtensionInput.getText().toString().trim();

            if (fileName.isEmpty()) {
                fileNameInput.setError("Enter file name");
                return;
            }

            if (optionFile.isChecked()) {
                if (fileExt.isEmpty()) {
                    fileExtensionInput.setError("Add file extension");
                    return;
                }
                if (listener != null) listener.onCreateFile(fileName, fileExt);
            } else if (optionFolder.isChecked()) {
                if (listener != null) listener.onCreateFolder(fileName);
            }

            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.setOnShowListener(d -> {
            fileNameInput.requestFocus();
            new Handler().postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) requireContext()
                        .getSystemService(requireContext().INPUT_METHOD_SERVICE);
                if (imm != null) imm.showSoftInput(fileNameInput, InputMethodManager.SHOW_IMPLICIT);
            }, 100);
        });

        return dialog;
    }
}