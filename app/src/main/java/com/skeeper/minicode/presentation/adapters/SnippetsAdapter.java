package com.skeeper.minicode.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.contracts.other.callbacks.IKeyPressedListener;
import com.skeeper.minicode.utils.helpers.VibrationManager;
import com.skeeper.minicode.domain.models.SnippetModel;

import java.util.List;

public class SnippetsAdapter extends RecyclerView.Adapter<SnippetsAdapter.KeySymbolViewHolder>  {
    Context context;
    List<SnippetModel> models;
    IKeyPressedListener listener;

    public SnippetsAdapter(Context context, List<SnippetModel> keySymbols, IKeyPressedListener listener) {
        this.context = context;
        this.models = keySymbols;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KeySymbolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_button_item, parent, false);
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
            VibrationManager.vibrate(40L, currentButton.getContext());
            listener.onKeyPressed(currentModel);

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
