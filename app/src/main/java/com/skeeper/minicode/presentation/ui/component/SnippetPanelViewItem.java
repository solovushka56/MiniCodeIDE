package com.skeeper.minicode.presentation.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.models.SnippetModel;

public class SnippetPanelViewItem extends ConstraintLayout {


    private SnippetModel boundModel;

    TextView valueText;
    TextView keyText;

    public View removeButton;

    public SnippetPanelViewItem(Context context) {
        super(context);
        init(context, null);
    }

    public SnippetPanelViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.snippet_panel_item, this, true);

        keyText = findViewById(R.id.keyName);
        valueText = findViewById(R.id.keyValue);
        removeButton = findViewById(R.id.snippetRemoveButton);
    }




    public void setKeyValue(String key, String value) {
        keyText.setText(key);
        valueText.setText(value);
    }


    public SnippetModel getBoundModel() {
        return boundModel;
    }
    public void setBoundModel(SnippetModel boundModel) {
        this.boundModel = boundModel;
    }


}
