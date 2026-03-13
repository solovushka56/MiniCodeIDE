package com.skeeper.minicode.presentation.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.presentation.ui.activity.CodeEditorActivity;
import com.skeeper.minicode.presentation.ui.fragment.CodeEditorFragment;
import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;

import java.util.Map;

public class DeleteFileDialog {
    public void show(FileItem item,
                     CodeEditorFragment currentCodeFragment,
                     Map<FileItem, CodeEditorFragment> cachedFragments,
                     FilesViewModel filesViewModel,
                     Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_remove_file, null);

        TextView textView = dialogView.findViewById(R.id.removeFileText);
        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        textView.setText("Remove " + item.getName() + "?");

        positiveButton.setOnClickListener(v -> {
            filesViewModel.deleteFile(item.getDirectory());

            if (currentCodeFragment == cachedFragments.get(item))
            {
                FragmentManager fragmentManager = ((FragmentActivity) context)
                        .getSupportFragmentManager();

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
}
