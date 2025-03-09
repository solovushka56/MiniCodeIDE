package com.skeeper.minicode.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.CodeEditorActivity;
import com.skeeper.minicode.R;
import com.skeeper.minicode.models.KeySymbolItemModel;

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
        var currentButton = holder.button;


        //content description is text that will be paste in code editor on click
        currentButton.setText(currentModel.getSymbolKey());
        currentButton.setContentDescription(currentModel.getSymbolValue());


        holder.button.setOnClickListener(v -> {
            var symbolView = (CodeEditorActivity) currentButton.getContext();
            symbolView.onSymbolClick(currentButton);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static final class KeySymbolViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        public KeySymbolViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.textButton);
        }
    }
}
