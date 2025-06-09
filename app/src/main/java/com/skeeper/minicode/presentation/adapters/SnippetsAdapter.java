package com.skeeper.minicode.presentation.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.contracts.other.callbacks.IKeyPressedListener;
import com.skeeper.minicode.utils.helpers.VibrationManager;
import com.skeeper.minicode.domain.models.SnippetModel;

import java.util.List;

public class SnippetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ARROW = 0;
    private static final int TYPE_SNIPPET = 1;



    Context context;
    List<SnippetModel> models;
    IKeyPressedListener listener;

    public SnippetsAdapter(Context context, List<SnippetModel> keySymbols, IKeyPressedListener listener) {
        this.context = context;
        this.models = keySymbols;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position < 4 ? TYPE_ARROW : TYPE_SNIPPET;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ARROW) {
            View view = inflater.inflate(R.layout.snippet_arrow_item, parent, false);
            return new ArrowViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.snippet_button_item, parent, false);
            return new KeySymbolViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArrowViewHolder) {
            ((ArrowViewHolder) holder).bind(position, listener);
            if (position == 0) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        ((ArrowViewHolder) holder).arrowButton.getLayoutParams();
                params.leftMargin = 22;
                ((ArrowViewHolder) holder).arrowButton.setLayoutParams(params);

            }


        } else if (holder instanceof KeySymbolViewHolder) {
            int snippetPosition = position - 4;
            SnippetModel currentModel = models.get(snippetPosition);
            ((KeySymbolViewHolder) holder).bind(currentModel, listener);
        }
    }

    @Override
    public int getItemCount() {
        return models.size() + 4;
    }

    public static class ArrowViewHolder extends RecyclerView.ViewHolder {
        CardView arrowButton;
        TextView arrowTextView;


        public ArrowViewHolder(@NonNull View itemView) {
            super(itemView);
            arrowButton = itemView.findViewById(R.id.keyTextRect);
            arrowTextView = itemView.findViewById(R.id.textButton);
        }

        public void bind(int position, IKeyPressedListener listener) {
            String[] arrows = {"←", "→", "↑", "↓"};
            arrowTextView.setText(arrows[position]);

            arrowButton.setOnClickListener(v -> {
                VibrationManager.vibrate(40L, itemView.getContext());
                listener.onArrowPressed(position);
            });
        }
    }

    public static final class KeySymbolViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView rect;

        public KeySymbolViewHolder(@NonNull View itemView) {
            super(itemView);
            rect = itemView.findViewById(R.id.keyTextRect);
            textView = itemView.findViewById(R.id.textButton);
        }

        public void bind(SnippetModel currentModel, IKeyPressedListener listener) {
            textView.setText(currentModel.getSymbolKey());
            textView.setContentDescription(currentModel.getSymbolValue());

            rect.setOnClickListener(v -> {
                VibrationManager.vibrate(40L, itemView.getContext());
                listener.onKeyPressed(currentModel);
            });
        }
    }
}