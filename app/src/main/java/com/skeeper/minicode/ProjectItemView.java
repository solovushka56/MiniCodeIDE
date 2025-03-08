package com.skeeper.minicode;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.skeeper.minicode.R;

public class ProjectItemView extends ConstraintLayout {
    private TextView projectTitle, projectFilepath;
    private ImageButton btnDelete;
    private OnProjectActionsListener listener;

    public ProjectItemView(Context context) {
        super(context);
        init(context, null);
    }

    public ProjectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.project_item, this, true);

        projectTitle = findViewById(R.id.projectTitle);
        projectFilepath = findViewById(R.id.projectFilepath);
        btnDelete = findViewById(R.id.btn_delete);

        setTitle("Amogus");
        setOnClickListener(v -> {
            Toast.makeText(getContext(), "amigo", Toast.LENGTH_LONG).show();

        });


        btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClicked(this);
        });
    }

    public interface OnProjectActionsListener {
        void onDeleteClicked(ProjectItemView view);

    }

    public void setTitle(String title) {
        projectTitle.setText(title);
    }

    public void setDescription(String description) {
        projectFilepath.setText(description);
    }

    public void setBackgroundColor(@ColorInt int color) {
        findViewById(R.id.parentRectView).setBackgroundColor(color);
    }

    public void setActionsListener(OnProjectActionsListener listener) {
        this.listener = listener;
    }
}