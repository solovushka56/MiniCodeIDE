package com.skeeper.minicode.data.remote;

import java.util.Dictionary;
import java.util.Map;

public record CompileRequest(
        String language,
        Map<String, String> files,
        String mainFile,
        String[] cliArguments
) {}