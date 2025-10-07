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

public class GetPythonMapUseCase extends GetLangRegexMapUseCase {

    public GetPythonMapUseCase(ICodeLangRepository codeLangRepository) {
        super(codeLangRepository);
    }

    @Override
    public Map<Pattern, Integer> execute(ExtensionType extension) {
        var langModel = codeLangRepository.getLangModel(extension);
        Map<Pattern, Integer> syntaxPatternsMap = new LinkedHashMap<>();
        HighlightThemeModel highlightModel = new HighlightThemeModel();

        String commentRegex = "#.*";
        syntaxPatternsMap.put(Pattern.compile(commentRegex), Color.parseColor("#7A7E85"));

        String stringSingleRegex = "'(?:\\\\'|[^'])*'";
        String stringDoubleRegex = "\"(?:\\\\\"|[^\"])*\"";
        String tripleSingleRegex = "'''(?:\\\\'|[^'])*'''";
        String tripleDoubleRegex = "\"\"\"(?:\\\\\"|[^\"])*\"\"\"";
        syntaxPatternsMap.put(Pattern.compile(stringSingleRegex), highlightModel.stringColor);
        syntaxPatternsMap.put(Pattern.compile(stringDoubleRegex), highlightModel.stringColor);
        syntaxPatternsMap.put(Pattern.compile(tripleSingleRegex), highlightModel.stringColor);
        syntaxPatternsMap.put(Pattern.compile(tripleDoubleRegex), highlightModel.stringColor);

        if (langModel.getKeywords() != null && !langModel.getKeywords().isEmpty()) {
            String keywordsRegex = "\\b(" + String.join("|", langModel.getKeywords()) + ")\\b";
            syntaxPatternsMap.put(Pattern.compile(keywordsRegex), highlightModel.keywordColor);
        }

        String builtinsRegex = "\\b(str|list|dict|tuple|set|len|range|self|cls)\\b";
        syntaxPatternsMap.put(Pattern.compile(builtinsRegex), highlightModel.typeColor);

        String classDeclarationRegex = "(?<=\\bclass\\s)[A-Za-z_][A-Za-z0-9_]*";
        syntaxPatternsMap.put(Pattern.compile(classDeclarationRegex), highlightModel.classColor);

        String functionDeclarationRegex = "(?<=\\bdef\\s)[a-z_][a-zA-Z0-9_]*";
        syntaxPatternsMap.put(Pattern.compile(functionDeclarationRegex), highlightModel.methodColor);

        String decoratorRegex = "@[A-Za-z_][A-Za-z0-9_]*(?:\\.[A-Za-z_][A-Za-z0-9_]*)*";
        syntaxPatternsMap.put(Pattern.compile(decoratorRegex), Color.parseColor("#DCDCAA"));

        String fStringRegex = "f['\"]";
        syntaxPatternsMap.put(Pattern.compile(fStringRegex), Color.parseColor("#CE9178"));

        return syntaxPatternsMap;
    }
}