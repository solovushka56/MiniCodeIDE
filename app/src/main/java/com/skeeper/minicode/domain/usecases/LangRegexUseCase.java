package com.skeeper.minicode.domain.usecases;

import android.graphics.Color;

import com.skeeper.minicode.data.repos.LanguageRepository;
import com.skeeper.minicode.domain.contracts.repos.ILanguageRepository;
import com.skeeper.minicode.domain.models.LangModel;

import java.util.List;

public class LangRegexUseCase {
    private final ILanguageRepository repository;

    public LangRegexUseCase(ILanguageRepository repository) {
        this.repository = repository;
    }


    public void execute() {
        int keywordColor = Color.parseColor("#4B70F5"); // make class with these
        int typeColor = Color.parseColor("#4EC9B0"); // to vm
        int classColor = Color.parseColor("#4EC9B0");
        int methodColor = Color.parseColor("#DCDCAA");
        int bracketColor = Color.parseColor("#569CD6");
        int stringColor = Color.parseColor("#CE9178"); //


        String stringRegex = "";
        String charRegex = "";
        String keywordsRegex = "";
        String typeRegex = "";
        String classDeclarationRegex = "";
        String methodCallRegex = "";

        LangModel langModel = repository.getLangModel();
        StringBuilder keywordRegexBuilder = new StringBuilder(keywordsRegex);

        keywordsRegex = buildListRegex(langModel.getKeywords());
        

    }

    private String buildListRegex(List<String> elements) {
        if (elements ==  null || elements.isEmpty()) return "";

        StringBuilder regexBuilder = new StringBuilder("\\b(");
        var elString = String.join("|", elements);
        regexBuilder.append(elString);
        regexBuilder.append(")\\b");
        return regexBuilder.toString();
    }
}
