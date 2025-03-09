package com.skeeper.minicode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TogglePanelItemView extends ConstraintLayout {

    TextView textView;
    CheckBox checkBox;

    public TogglePanelItemView(Context context) {
        super(context);
        init(context, null);
    }

    public TogglePanelItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.toggle_panel_item, this, true);

        textView = findViewById(R.id.nameText);
        checkBox = findViewById(R.id.checkbox);

        setOnClickListener(v -> {
            Toast.makeText(getContext(), "amigo", Toast.LENGTH_LONG).show();

        });


    }

    public boolean getIsCheckboxTrue() {
        return checkBox.isChecked();
    }

    public void setText(String text) {
        textView.setText(text);
    }


}