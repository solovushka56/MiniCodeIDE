package com.skeeper.minicode.domain.contracts.repos.project;

import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.models.LangModel;

public interface ICodeLangRepository {
    public LangModel getLangModel(ExtensionType extensionType);
}
