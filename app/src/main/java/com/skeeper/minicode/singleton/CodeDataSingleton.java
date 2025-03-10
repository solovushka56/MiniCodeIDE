package com.skeeper.minicode.singleton;

import androidx.annotation.Nullable;

import com.amrdeveloper.codeview.CodeView;

public class CodeDataSingleton {

    private static CodeDataSingleton instance = null;
    private CodeDataSingleton() {}
    public static CodeDataSingleton getInstance() {
        if (instance == null) instance = new CodeDataSingleton();
        return instance;
    }


    @Nullable
    public CodeView currentCodeView = null;


    @Nullable
    public CodeView getCurrentCodeView() {
        return currentCodeView;
    }
    public void setCurrentCodeView(@Nullable CodeView currentCodeView) {
        this.currentCodeView = currentCodeView;
    }



}
