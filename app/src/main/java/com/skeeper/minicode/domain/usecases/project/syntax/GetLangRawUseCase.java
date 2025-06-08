package com.skeeper.minicode.domain.usecases.project.syntax;

import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.enums.ExtensionType;


public class GetLangRawUseCase {
    public int execute(ExtensionType type) {
        return switch (type) {
            case JAVA -> R.raw.java_lang_syntax;
            case PYTHON -> R.raw.py_lang_syntax;
            case XML -> R.raw.xml_lang_syntax;
            case HTML ->  R.raw.html_lang_syntax;
            default -> -1;
        };
    }
}
