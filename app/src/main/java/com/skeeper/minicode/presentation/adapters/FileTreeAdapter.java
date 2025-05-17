package com.skeeper.minicode.presentation.adapters;


import android.animation.ObjectAnimator;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.contracts.other.IFileTreeListener;
import com.skeeper.minicode.domain.models.FileItem;

import java.util.ArrayList;
import java.util.List;

public class FileTreeAdapter extends RecyclerView.Adapter<FileTreeAdapter.ViewHolder> {
    private List<FileItem> visibleItems;
    private final List<FileItem> allItems;
    private final SparseBooleanArray expandedStates = new SparseBooleanArray();
    private IFileTreeListener listener;


    public FileTreeAdapter(List<FileItem> items, IFileTreeListener changesListener) {
        this.allItems = items;
        this.visibleItems = generateVisibleList(items);
        this.listener = changesListener;
        saveExpandedStates();
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
            return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
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

    private void saveExpandedStates() {
        expandedStates.clear();
        for (int i = 0; i < visibleItems.size(); i++) {
            FileItem item = visibleItems.get(i);
            if (item.isDirectory()) {
                expandedStates.put(i, item.isExpanded());
            }
        }
    }

    private void restoreExpandedStates(List<FileItem> newVisibleItems) {
        for (int i = 0; i < newVisibleItems.size(); i++) {
            FileItem item = newVisibleItems.get(i);
            if (item.isDirectory() && expandedStates.get(i, false)) {
                item.setExpanded(true);
            }
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

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        params.leftMargin = item.getLevel() * 45;
        holder.itemView.setLayoutParams(params);

        holder.name.setText(item.getName());
        holder.icon.setImageResource(item.isDirectory() ? R.drawable.ic_folder : R.drawable.ic_file);

        if (item.isDirectory()) {
            holder.arrow.setVisibility(View.VISIBLE);
            float targetRotation = item.isExpanded() ? 90 : 0;
            if (holder.arrow.getRotation() != targetRotation) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(holder.arrow, "rotation", targetRotation);
                animator.setDuration(200);
                animator.start();
            }
            holder.itemView.setOnClickListener(v -> toggleItem(position, holder));
        } else {
            holder.arrow.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(v -> listener.onFileClick(item));
        }
    }

    private void toggleItem(int position, ViewHolder holder) {
        FileItem item = visibleItems.get(position);

        if (item.isDirectory()) {
            item.setExpanded(!item.isExpanded());

            List<FileItem> oldVisibleItems = new ArrayList<>(visibleItems);

            visibleItems = generateVisibleList(allItems);

            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FileDiffCallback(oldVisibleItems, visibleItems));
            diffResult.dispatchUpdatesTo(this);
            ObjectAnimator rotation = ObjectAnimator.ofFloat(holder.arrow, "rotation",
                    item.isExpanded() ? 0 : 90, item.isExpanded() ? 90 : 0);
            rotation.setDuration(200);
            rotation.start();
            listener.onFolderClick(item);
        }
        else {
            listener.onFileClick(item);
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