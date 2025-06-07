package com.skeeper.minicode.presentation.adapters;

import android.animation.ObjectAnimator;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.contracts.other.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTreeAdapter extends RecyclerView.Adapter<FileTreeAdapter.ViewHolder> {
    private List<FileItem> visibleItems;
    private final List<FileItem> allItems;
    private final IFileTreeListener listener;
    private static final int FILE_TREE_TAB_PIXELS = 30;

    public FileTreeAdapter(List<FileItem> items, IFileTreeListener changesListener) {
        this.allItems = items;
        this.visibleItems = generateVisibleList(items);
        this.listener = changesListener;
    }

    private static class FileDiffCallback extends DiffUtil.Callback {
        private final List<FileItem> oldList;
        private final List<FileItem> newList;

        public FileDiffCallback(List<FileItem> oldList, List<FileItem> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }

        @Override
        public int getNewListSize() { return newList.size(); }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getPath().equals(newList.get(newItemPosition).getPath());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            FileItem oldItem = oldList.get(oldItemPosition);
            FileItem newItem = newList.get(newItemPosition);
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.isDirectory() == newItem.isDirectory() &&
                    oldItem.isExpanded() == newItem.isExpanded() &&
                    oldItem.getLevel() == newItem.getLevel();
        }
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

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                holder.itemView.getLayoutParams();
        params.leftMargin = item.getLevel() * FILE_TREE_TAB_PIXELS;
        holder.itemView.setLayoutParams(params);

        holder.name.setText(item.getName());
        holder.icon.setImageResource(item.isDirectory()
                ? R.drawable.ic_folder
                : R.drawable.ic_file);

        if (item.isDirectory()) {
            holder.arrow.setVisibility(View.VISIBLE);
            holder.arrow.setRotation(item.isExpanded() ? 90 : 0);

            holder.itemView.setOnClickListener(v -> toggleItem(item, holder));
        }
        else {
            holder.arrow.setVisibility(View.INVISIBLE);
            holder.arrow.setRotation(0);
            holder.itemView.setOnClickListener(v -> listener.onFileClick(item));
        }
    }

    private void toggleItem(FileItem item, ViewHolder holder) {
        if (!item.isDirectory()) return;

        item.setExpanded(!item.isExpanded());
        List<FileItem> oldVisibleItems = new ArrayList<>(visibleItems);
        visibleItems = generateVisibleList(allItems);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new FileDiffCallback(oldVisibleItems, visibleItems));

        var targetRotation = item.isExpanded() ? 90 : 0;
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(holder.arrow, "rotation", targetRotation)
                .setDuration(180);
        animator.start();


        diffResult.dispatchUpdatesTo(this);

        listener.onFolderClick(item);
    }

    @Override
    public int getItemCount() {
        return visibleItems.size();
    }

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
}