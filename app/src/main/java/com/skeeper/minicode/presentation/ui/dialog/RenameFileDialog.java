package com.skeeper.minicode.presentation.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;

import java.io.File;

public class RenameFileDialog {
    public void show(FileItem item, FilesViewModel viewModel, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_rename_file, null);

        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        TextInputEditText input = dialogView.findViewById(R.id.newNameTextEdit);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
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

            viewModel.renameFile(item.getDirectory(), newName);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
