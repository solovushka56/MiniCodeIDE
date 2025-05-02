package com.skeeper.minicode.data.repos;

import android.content.Context;

import com.skeeper.minicode.R;
import com.skeeper.minicode.data.mappers.LangSyntaxParser;
import com.skeeper.minicode.domain.ProgrammingLang;
import com.skeeper.minicode.domain.contracts.repos.ILanguageRepository;
import com.skeeper.minicode.domain.models.LangModel;

import java.util.Collections;
import java.util.List;

public class LanguageRepository implements ILanguageRepository {

    private final LangModel langModel;
    private final ProgrammingLang langType;
//    private final LangSyntaxMapper; //todo

    public LanguageRepository(Context context, ProgrammingLang langType) {
        this.langModel = LangSyntaxParser.parse(context, getRawFromLangType(langType));
        this.langType = langType;
    }


    @Override
    public ProgrammingLang getLangType() {
        return langType;
    }
    @Override
    public LangModel getLangModel() {
        return langModel;
    }





    private int getRawFromLangType(ProgrammingLang type) {
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
