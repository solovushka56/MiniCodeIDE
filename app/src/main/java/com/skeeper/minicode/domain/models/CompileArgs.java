package com.skeeper.minicode.domain.models;

import java.util.Map;
import java.util.Set;

public record CompileArgs(
        String language,
        Map<String, String> files,
        String mainFile,
        String[] cliArguments
)  {}
