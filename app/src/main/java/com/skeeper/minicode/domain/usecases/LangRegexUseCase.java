package com.skeeper.minicode.domain.usecases;

import com.skeeper.minicode.domain.contracts.repos.ILangRepository;
import com.skeeper.minicode.domain.models.HighlightColorModel;
import com.skeeper.minicode.domain.models.LangModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LangRegexUseCase {
    private final ILangRepository repository;

    public LangRegexUseCase(ILangRepository repository) {
        this.repository = repository;
    }


    public Map<Pattern, Integer> execute() {

        Map<Pattern, Integer> syntaxPatternsMap = new LinkedHashMap<>();

        HighlightColorModel highlightModel = new HighlightColorModel();
        LangModel langModel = repository.getLangModel();


//        keywordsRegex = buildListRegex(langModel.getKeywords());
//        syntaxPatternsMap.put(Pattern.compile(keywordsRegex), highlightModel.keywordColor);

        String stringRegex = "\"(?:\\\\.|[^\"\\\\])*\"";
        syntaxPatternsMap.put(Pattern.compile(stringRegex), highlightModel.stringColor);

        String charRegex = "'(?:\\\\.|[^'\\\\])*'";
        syntaxPatternsMap.put(Pattern.compile(charRegex), highlightModel.stringColor);


        String keywordsRegex = "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|"
                + "continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|"
                + "implements|import|instanceof|int|interface|long|native|new|package|private|"
                + "protected|public|return|short|static|strictfp|super|switch|synchronized|this|"
                + "throw|throws|transient|try|void|volatile|while|var|record|sealed|non-sealed|permits|"
                + "true|false|null)\\b";
        syntaxPatternsMap.put(Pattern.compile(keywordsRegex), highlightModel.keywordColor);

        String typeRegex = "\\b(String|Integer|Double|Boolean|Float|Long|Short|Byte|" +
                "Character|Void|Object|Exception|Class|Number|System|Math)\\b";
        syntaxPatternsMap.put(Pattern.compile(typeRegex), highlightModel.typeColor);

        String classDeclarationRegex = "(?<=\\bclass\\s)[A-Za-z0-9_]+";
        syntaxPatternsMap.put(Pattern.compile(classDeclarationRegex), highlightModel.keywordColor);

        String methodCallRegex = "\\b([a-z][a-zA-Z0-9_]*)\\s*(?=\\()";
        syntaxPatternsMap.put(Pattern.compile(methodCallRegex), highlightModel.methodColor);

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
