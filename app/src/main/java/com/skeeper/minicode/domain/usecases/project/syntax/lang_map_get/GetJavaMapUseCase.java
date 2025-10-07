package com.skeeper.minicode.domain.usecases.project.syntax.lang_map_get;

import android.graphics.Color;

import com.skeeper.minicode.domain.contracts.repos.project.ICodeLangRepository;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.models.HighlightThemeModel;
import com.skeeper.minicode.domain.models.LangModel;
import com.skeeper.minicode.domain.usecases.project.syntax.GetLangRegexMapUseCase;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GetJavaMapUseCase extends GetLangRegexMapUseCase {

    public GetJavaMapUseCase(ICodeLangRepository codeLangRepository) {
        super(codeLangRepository);
    }

    @Override
    public Map<Pattern, Integer> execute(ExtensionType extension) {
        var langModel = codeLangRepository.getLangModel(extension);

        Map<Pattern, Integer> syntaxPatternsMap = new LinkedHashMap<>();
        HighlightThemeModel highlightModel = new HighlightThemeModel();

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
}
