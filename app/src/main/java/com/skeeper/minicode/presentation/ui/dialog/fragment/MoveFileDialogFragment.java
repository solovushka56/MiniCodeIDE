package com.skeeper.minicode.presentation.ui.dialog.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MoveFileDialogFragment extends DialogFragment {

    public interface Listener {
        void onMoveEnd(String newPath);
    }

    private static final String ARG_KEY = "arg_placeholder_path"; //  for bundle

    public static MoveFileDialogFragment newInstance(String placeholderPath) {
        MoveFileDialogFragment f = new MoveFileDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY, placeholderPath);
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
        String placeholder = "";
        if (getArguments() != null) placeholder = getArguments().getString(ARG_KEY, "");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_move_file, null);

        Button positiveButton = view.findViewById(R.id.positiveButton);
        Button negativeButton = view.findViewById(R.id.negativeButton);
        TextInputEditText pathInput = view.findViewById(R.id.enterNewDirectory);

        pathInput.setText(placeholder);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(view);
        Dialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String newPath = pathInput.getText().toString().trim();
            if (listener != null) listener.onMoveEnd(newPath);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }
}