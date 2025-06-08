package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.models.SnippetModel;

import java.util.List;

public interface ISnippetsRepository {
    void addSnippet(String key, String value);
    void removeSnippet(String key);
    List<SnippetModel> loadSnippets();
    void saveSnippets(List<SnippetModel> snippets);
}