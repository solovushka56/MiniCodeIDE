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

public class MoveFileDialog {
    public void show(FileItem item,
                     FilesViewModel filesViewModel,
                     Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_move_file, null);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);

        TextInputEditText pathInput = dialogView.findViewById(R.id.enterNewDirectory);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String newPath = pathInput.getText().toString().trim();
            File newPathFile = new File(newPath);
            if (!newPathFile.exists()) {
                pathInput.setError("Directory is incorrect");
                return;
            }
            if (new File(newPathFile, item.getName()).exists()) {
                pathInput.setError("File with the same name already exists in the new folder");
                return;
            }
            filesViewModel.moveFile(item.getDirectory(), new File(newPath));
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }
}
