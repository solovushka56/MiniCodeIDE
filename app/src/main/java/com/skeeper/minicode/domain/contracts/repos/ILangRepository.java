package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.enums.LangType;
import com.skeeper.minicode.domain.models.LangModel;

public interface ILangRepository {
    LangType getLangType();
    LangModel getLangModel();
}
