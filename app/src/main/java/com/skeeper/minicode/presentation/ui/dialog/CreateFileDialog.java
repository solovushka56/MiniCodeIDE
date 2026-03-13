package com.skeeper.minicode.presentation.ui.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.presentation.ui.fragment.CodeEditorFragment;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;

import java.io.File;
import java.util.Map;

public class CreateFileDialog {

    public void show(FileItem item,
                     FilesViewModel filesViewModel,
                     Context context) {
        if (!item.isDirectory()) return;

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_create_file, null);

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        TextInputEditText fileNameInput = dialogView.findViewById(R.id.enterName);
        TextInputEditText fileExtensionInput = dialogView.findViewById(R.id.enterExtension);
        TextView dot = dialogView.findViewById(R.id.dotView);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        RadioButton optionFile = dialogView.findViewById(R.id.optionFileCreate);
        RadioButton optionFolder = dialogView.findViewById(R.id.optionFolderCreate);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        TextInputLayout nameInputLayout = (TextInputLayout)
                fileNameInput.getParent().getParent(); // idk, potential bug
        TextInputLayout extensionInputLayout = (TextInputLayout)
                fileExtensionInput.getParent().getParent();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == optionFile.getId()) {
                title.setText("Create new File");

                dot.setVisibility(View.VISIBLE);
                extensionInputLayout.setVisibility(View.VISIBLE);
                fileExtensionInput.setText("");

                LinearLayout.LayoutParams nameParams = (LinearLayout.LayoutParams)
                        nameInputLayout.getLayoutParams();
                nameParams.width = 0;
                nameParams.weight = 2.0f;
                nameInputLayout.setLayoutParams(nameParams);

                LinearLayout.LayoutParams extensionParams = (LinearLayout.LayoutParams)
                        extensionInputLayout.getLayoutParams();
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

                LinearLayout.LayoutParams nameParams = (LinearLayout.LayoutParams)
                        nameInputLayout.getLayoutParams();
                nameParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                nameParams.weight = 0;
                nameInputLayout.setLayoutParams(nameParams);
            }
        });

        positiveButton.setOnClickListener(v -> {
            String fileName = fileNameInput.getText().toString().trim();
            String fileExt = fileExtensionInput.getText().toString().trim();
            String path = item.getDirectory().getPath();

            if (fileName.isEmpty()) {
                fileNameInput.setError("Enter file name");
                return;
            }

            if (optionFile.isChecked()) {
                if (fileExt.isEmpty()) {
                    fileExtensionInput.setError("Add file extension");
                    return;
                }
                String fullFileName = fileName + "." + fileExt;
                filesViewModel.createFile(new File(path, fullFileName));
            } else if (optionFolder.isChecked()) {
                filesViewModel.createFolder(new File(path), fileName);
            }

            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        fileNameInput.requestFocus();
        fileNameInput.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) context.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(fileNameInput, InputMethodManager.SHOW_IMPLICIT);
        }, 100);
    }

}
