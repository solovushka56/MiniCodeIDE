package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.models.LangModel;

public interface ILangRepository {
    ExtensionType getLangType();
    LangModel getLangModel();
}
