package com.skeeper.minicode.presentation.ui.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.skeeper.minicode.R;

public class SettingItemView extends ConstraintLayout {

    private TextView textView = null;
    private ImageView imageView = null;
    private Intent onClickIntent = null;


    public SettingItemView(Context context) {
        super(context);
        init(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.settings_category_item, this, true);

        textView = findViewById(R.id.settingName);
        imageView = findViewById(R.id.settingIcon);

        setOnClickListener(v -> {
            if (onClickIntent == null) {
                Toast.makeText(getContext(), "In development...", Toast.LENGTH_SHORT).show();
                return;
            }
            getContext().startActivity(onClickIntent);
        });


    }


    public void setText(String text) {
        textView.setText(text);
    }
    public void setIcon(int iconId) {
        imageView.setImageResource(iconId);
    }

    public void setClickIntent(Intent onClickIntent) {
        this.onClickIntent = onClickIntent;
    }

}
