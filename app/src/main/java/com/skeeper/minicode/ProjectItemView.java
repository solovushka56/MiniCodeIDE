package com.skeeper.minicode;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.skeeper.minicode.R;

public class ProjectItemView extends ConstraintLayout {
    private OnProjectActionsListener listener;


    private TextView projectTitleView;
    private TextView projectFilepathView;
    private ImageButton btnDeleteView;
//    public View mainRectView;
//    public View innerRectView;

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

        projectTitleView = findViewById(R.id.projectTitle);
        projectFilepathView = findViewById(R.id.projectFilepath);
        btnDeleteView = findViewById(R.id.btn_delete);



        setOnClickListener(v -> {
            Toast.makeText(getContext(), "amigo", Toast.LENGTH_LONG).show();

        });




        btnDeleteView.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClicked(this);
        });
    }


    public void setMainRectColor(int color) {
        findViewById(R.id.parentRectView).setBackgroundTintList(ColorStateList.valueOf(color));
    }
    public void setInnerRectColor(int color) {
        findViewById(R.id.projectFilepath).setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setProjectName(String name) {
        projectTitleView.setText(name);
    }
    public void setProjectFilepathText(String filepath) {
        projectFilepathView.setText(filepath);
    }

    private interface OnProjectActionsListener {
        void onDeleteClicked(ProjectItemView view);

    }
    private void setActionsListener(OnProjectActionsListener listener) {
        this.listener = listener;
    }
}