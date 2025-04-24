package com.skeeper.minicode.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.models.FileItem;

import java.util.ArrayList;
import java.util.List;

public class FileTreeAdapter extends RecyclerView.Adapter<FileTreeAdapter.ViewHolder> {
    private List<FileItem> visibleItems;
    private final List<FileItem> allItems;

    public FileTreeAdapter(List<FileItem> items) {
        this.allItems = items;
        this.visibleItems = generateVisibleList(items);
    }

    private List<FileItem> generateVisibleList(List<FileItem> items) {
        List<FileItem> result = new ArrayList<>();
        for (FileItem item : items) {
            result.add(item);
            if (item.isDirectory() && item.isExpanded()) {
                result.addAll(generateVisibleList(item.getChildren()));
            }
        }
        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem item = visibleItems.get(position);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        params.leftMargin = item.getLevel() * 50;
        holder.itemView.setLayoutParams(params);

        holder.name.setText(item.getName());
        holder.icon.setImageResource(item.isDirectory() ? R.drawable.ic_folder : R.drawable.ic_file);


        if (item.isDirectory()) {
            holder.arrow.setVisibility(View.VISIBLE);
            holder.arrow.setRotation(item.isExpanded() ? 90 : 0);
            holder.panel.setOnClickListener(v -> toggleItem(position));
        }
        else {
            holder.arrow.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleItem(int position) {
        FileItem item = visibleItems.get(position);
        if (item.isDirectory()) {
            item.setExpanded(!item.isExpanded());
            visibleItems = generateVisibleList(allItems);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() { return visibleItems.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        ImageView arrow;
        View panel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            arrow = itemView.findViewById(R.id.arrow);
            panel = itemView.findViewById(R.id.panelBody);
        }
    }

    private void animateRotation(@NonNull ViewHolder holder, int position) {
        FileItem item = visibleItems.get(position);
        ViewPropertyAnimator animator = holder.arrow.animate();
        animator.rotation(item.isExpanded() ? 90 : 0);
        animator.setDuration(300).setInterpolator((t) -> {
            t *= 2;
            float overshootDefault = 1.75f;
            if (t < 1) {
                return 0.5f * (t * t * ((overshootDefault + 1) * t - overshootDefault));
            } else {
                t -= 2;
                return 0.5f * (t * t * ((overshootDefault + 1) * t + overshootDefault) + 2);
            }
        }).start();
    }

}