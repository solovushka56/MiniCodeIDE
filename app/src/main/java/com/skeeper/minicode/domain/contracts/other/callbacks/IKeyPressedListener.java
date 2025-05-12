package com.skeeper.minicode.domain.contracts.other.callbacks;

import com.skeeper.minicode.domain.models.KeySymbolItemModel;

// for key symbol panel in code editor - observer
public interface IKeyPressedListener {
    void onKeyPressed(KeySymbolItemModel keySymbolItemModel);
}
