package com.skeeper.minicode.data.repos;

import android.content.Context;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.contracts.repos.ILangRepository;
import com.skeeper.minicode.domain.models.LangModel;

import java.util.Map;

public class LangRepository implements ILangRepository {


    private Map<ExtensionType, LangModel> langMap;

    public LangRepository() {
    }



    private int getRawFromLangType(ExtensionType type) {
        return switch (type) {
            case JAVA -> R.raw.java_lang_syntax;
            case PYTHON -> R.raw.py_lang_syntax;
            default -> -1;
        };

    }

}
