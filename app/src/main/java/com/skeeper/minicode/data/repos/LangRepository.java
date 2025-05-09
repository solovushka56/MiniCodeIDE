package com.skeeper.minicode.data.repos;

import android.content.Context;

import com.skeeper.minicode.R;
import com.skeeper.minicode.data.mappers.LangSyntaxParser;
import com.skeeper.minicode.domain.enums.LangType;
import com.skeeper.minicode.domain.contracts.repos.ILangRepository;
import com.skeeper.minicode.domain.models.LangModel;

public class LangRepository implements ILangRepository {

    private final LangModel langModel;
    private final LangType langType;
//    private final LangSyntaxMapper; //todo

    public LangRepository(Context context, LangType langType) {
        this.langModel = LangSyntaxParser.parse(context, getRawFromLangType(langType));
        this.langType = langType;
    }


    @Override
    public LangType getLangType() {
        return langType;
    }
    @Override
    public LangModel getLangModel() {
        return langModel;
    }





    private int getRawFromLangType(LangType type) {
        switch (type)
        {
            case JAVA:
               return R.raw.java_lang_syntax;
            case PYTHON:
                return R.raw.py_lang_syntax;
            default:
                return -1;
        }

    }

}
