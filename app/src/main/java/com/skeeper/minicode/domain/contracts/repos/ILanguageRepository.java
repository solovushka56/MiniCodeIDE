package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.ProgrammingLang;
import com.skeeper.minicode.domain.models.LangModel;

import java.util.List;

public interface ILanguageRepository {
    ProgrammingLang getLangType();
    LangModel getLangModel();
}
