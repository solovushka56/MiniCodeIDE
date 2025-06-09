package com.skeeper.minicode.domain.contracts.other.callbacks;

import com.skeeper.minicode.domain.enums.Direction;
import com.skeeper.minicode.domain.models.SnippetModel;

// for key symbol panel in code editor - observer
public interface IKeyPressedListener {
    void onKeyPressed(SnippetModel keySymbolItemModel);
    void onArrowPressed(int direction);
}
