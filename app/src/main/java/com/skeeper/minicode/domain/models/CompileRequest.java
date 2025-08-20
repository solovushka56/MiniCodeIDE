package com.skeeper.minicode.domain.models;

import java.util.Map;

public record CompileRequest(
        String language,
        Map<String, String> files,
        String mainFile,
        String[] cliArguments
) {}