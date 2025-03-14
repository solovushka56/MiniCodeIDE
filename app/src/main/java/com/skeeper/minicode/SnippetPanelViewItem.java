package com.skeeper.minicode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.skeeper.minicode.models.KeySymbolItemModel;

public class SnippetPanelViewItem extends ConstraintLayout {


    private KeySymbolItemModel boundModel;

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
        LayoutInflater.from(context).inflate(R.layout.panel_snippet_item, this, true);

        keyText = findViewById(R.id.keyName);
        valueText = findViewById(R.id.keyValue);
        removeButton = findViewById(R.id.snippetRemoveButton);
    }




    public void setKeyValue(String key, String value) {
        keyText.setText(key);
        valueText.setText(value);
    }


    public KeySymbolItemModel getBoundModel() {
        return boundModel;
    }
    public void setBoundModel(KeySymbolItemModel boundModel) {
        this.boundModel = boundModel;
    }


}
