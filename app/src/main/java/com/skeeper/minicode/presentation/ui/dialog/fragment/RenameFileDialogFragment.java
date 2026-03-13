package com.skeeper.minicode.presentation.ui.dialog.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RenameFileDialogFragment extends DialogFragment {

    public interface Listener {
        void onRenameConfirmed(String newName);
    }

    private static final String ARG_KEY = "arg_current_name";

    public static RenameFileDialogFragment newInstance(String currentName) {
        RenameFileDialogFragment f = new RenameFileDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY, currentName);
        f.setArguments(b);
        return f;
    }

    private Listener listener;

    public void setListener(Listener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String currentName = "";
        if (getArguments() != null) currentName = getArguments()
                .getString(ARG_KEY, "");

        var inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rename_file, null);

        Button positiveButton = view.findViewById(R.id.positiveButton);
        Button negativeButton = view.findViewById(R.id.negativeButton);
        TextInputEditText input = view.findViewById(R.id.newNameTextEdit);

        input.setText(currentName);

        var builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(view);
        Dialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();
            if (listener != null) listener.onRenameConfirmed(newName);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }
}