package com.skeeper.minicode.adapters;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.helpers.VibrationManager;
import com.skeeper.minicode.models.KeySymbolItemModel;
import com.skeeper.minicode.singleton.CodeDataSingleton;

import java.util.List;

public class KeySymbolAdapter extends RecyclerView.Adapter<KeySymbolAdapter.KeySymbolViewHolder>  {
    Context context;
    List<KeySymbolItemModel> models;


    public KeySymbolAdapter(Context context, List<KeySymbolItemModel> keySymbols) {
        this.context = context;
        this.models = keySymbols;
    }

    @NonNull
    @Override
    public KeySymbolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.key_symbol_button_item, parent, false);
        return new KeySymbolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeySymbolViewHolder holder, int position) {
        var currentModel = models.get(position);
        TextView currentButton = holder.textView;

        //content description is text that will be paste in code editor on click
        currentButton.setText(currentModel.getSymbolKey());
        currentButton.setContentDescription(currentModel.getSymbolValue());


        holder.rect.setOnClickListener(v -> {
            var currentCodeView = CodeDataSingleton.getInstance().currentCodeView;
            VibrationManager.vibrate(40L, currentButton.getContext());
            if (currentCodeView != null) {
                int cursorPosition = currentCodeView.getSelectionEnd();
                Editable editable = currentCodeView.getText();
                editable.insert(cursorPosition, currentModel.getSymbolValue());
                currentCodeView.setSelection(cursorPosition + currentModel.getSymbolValue().length());
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static final class KeySymbolViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CardView rect;
        public KeySymbolViewHolder(@NonNull View itemView) {
            super(itemView);
            rect = itemView.findViewById(R.id.keyTextRect);
            textView = itemView.findViewById(R.id.textButton);
        }
    }
}
