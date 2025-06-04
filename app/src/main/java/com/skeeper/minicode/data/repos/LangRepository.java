package com.skeeper.minicode.data.repos;

import android.content.Context;

import com.skeeper.minicode.R;
import com.skeeper.minicode.data.mappers.LangSyntaxParser;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.contracts.repos.ILangRepository;
import com.skeeper.minicode.domain.models.LangModel;

public class LangRepository implements ILangRepository {

    private final LangModel langModel;
    private final ExtensionType langType;
//    private final LangSyntaxMapper; //todo

    public LangRepository(Context context, ExtensionType langType) {
        this.langModel = LangSyntaxParser.parse(context, getRawFromLangType(langType));
        this.langType = langType;
    }


    @Override
    public ExtensionType getLangType() {
        return langType;
    }
    @Override
    public LangModel getLangModel() {
        return langModel;
    }





    private int getRawFromLangType(ExtensionType type) {
        return switch (type) {
            case JAVA -> R.raw.java_lang_syntax;
            case PYTHON -> R.raw.py_lang_syntax;
            default -> -1;
        };

    }

}
