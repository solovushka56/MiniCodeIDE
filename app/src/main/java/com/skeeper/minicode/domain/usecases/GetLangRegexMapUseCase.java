package com.skeeper.minicode.domain.usecases;

import android.graphics.Color;

import com.skeeper.minicode.domain.models.HighlightColorModel;
import com.skeeper.minicode.domain.models.LangModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GetLangRegexMapUseCase {
    private final LangModel langModel;

    public GetLangRegexMapUseCase(LangModel langModel) {
        this.langModel = langModel;
    }


    public Map<Pattern, Integer> execute() {

        Map<Pattern, Integer> syntaxPatternsMap = new LinkedHashMap<>();
        HighlightColorModel highlightModel = new HighlightColorModel();


        String commentRegex = "//.*";
        syntaxPatternsMap.put(Pattern.compile(commentRegex), Color.parseColor("#7A7E85"));


        String stringRegex = "\"(?:\\\\.|[^\"\\\\])*\"";
        syntaxPatternsMap.put(Pattern.compile(stringRegex), highlightModel.stringColor);

        String charRegex = "'(?:\\\\.|[^'\\\\])*'";
        syntaxPatternsMap.put(Pattern.compile(charRegex), highlightModel.stringColor);


        String keywordsRegex = buildListRegex(langModel.getKeywords());
        syntaxPatternsMap.put(Pattern.compile(keywordsRegex), highlightModel.keywordColor);

        String typeRegex = buildListRegex(langModel.getObjectTypes());
        syntaxPatternsMap.put(Pattern.compile(typeRegex), highlightModel.typeColor);

        String classDeclarationRegex = "(?<=\\bclass\\s)[A-Za-z0-9_]+";
        syntaxPatternsMap.put(Pattern.compile(classDeclarationRegex), highlightModel.classColor);

        String methodCallRegex = "\\b([a-z][a-zA-Z0-9_]*)\\s*(?=\\()";
        syntaxPatternsMap.put(Pattern.compile(methodCallRegex), highlightModel.methodColor);

        String annotationRegex = "@[A-Za-z][\\w.]*";
        syntaxPatternsMap.put(Pattern.compile(annotationRegex), Color.parseColor("#DCDCAA"));


        return syntaxPatternsMap;
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
