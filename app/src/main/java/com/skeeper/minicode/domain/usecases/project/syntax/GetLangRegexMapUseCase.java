package com.skeeper.minicode.domain.usecases.project.syntax;

import android.graphics.Color;

import com.skeeper.minicode.domain.contracts.repos.project.ICodeLangRepository;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.models.HighlightThemeModel;
import com.skeeper.minicode.domain.models.LangModel;
import com.skeeper.minicode.domain.usecases.project.syntax.lang_map_get.GetJavaMapUseCase;
import com.skeeper.minicode.domain.usecases.project.syntax.lang_map_get.GetPythonMapUseCase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GetLangRegexMapUseCase {

    protected final ICodeLangRepository codeLangRepository;
    public GetLangRegexMapUseCase(ICodeLangRepository codeLangRepository) {
        this.codeLangRepository = codeLangRepository;
    }


    public Map<Pattern, Integer> execute(ExtensionType extension) {
        return switch (extension) {
            case PYTHON -> new GetPythonMapUseCase(codeLangRepository).execute(extension);
            case JAVA -> new GetJavaMapUseCase(codeLangRepository).execute(extension);
            default -> new LinkedHashMap<>();
        };
    }

    protected String buildListRegex(List<String> elements) {
        if (elements ==  null || elements.isEmpty()) return "";

        StringBuilder regexBuilder = new StringBuilder("\\b(");
        var elString = String.join("|", elements);
        regexBuilder.append(elString);
        regexBuilder.append(")\\b");
        return regexBuilder.toString();
    }
}
