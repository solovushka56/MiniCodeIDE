package com.skeeper.minicode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingItemView extends ConstraintLayout {

    TextView textView;
    ImageView imageView;



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
            Toast.makeText(getContext(), "amigo", Toast.LENGTH_LONG).show();

        });


    }


    public void setText(String text) {
        textView.setText(text);
    }
    public void setIcon(int iconId) {
        imageView.setImageResource(iconId);
    }

}
