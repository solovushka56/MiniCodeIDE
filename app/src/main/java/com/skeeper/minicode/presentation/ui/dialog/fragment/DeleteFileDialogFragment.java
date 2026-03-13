package com.skeeper.minicode.presentation.ui.dialog.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.skeeper.minicode.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteFileDialogFragment extends DialogFragment {

    public interface Listener {
        void onRemoveConfirmed();
    }

    private static final String ARG_KEY = "arg_item_name";

    public static DeleteFileDialogFragment newInstance(String itemName) {
        DeleteFileDialogFragment f = new DeleteFileDialogFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY, itemName);
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
        String itemName = "";
        if (getArguments() != null) itemName = getArguments().getString(ARG_KEY, "");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_remove_file, null);

        TextView textView = view.findViewById(R.id.removeFileText);
        Button positiveButton = view.findViewById(R.id.positiveButton);
        Button negativeButton = view.findViewById(R.id.negativeButton);

        textView.setText("Remove " + itemName + "?");

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(view);
        Dialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            if (listener != null) listener.onRemoveConfirmed();
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }
}