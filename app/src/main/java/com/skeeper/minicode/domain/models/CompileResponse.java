package com.skeeper.minicode.domain.models;

public record CompileResponse(boolean success,
                              String output,
                              String errors,
                              String executionTime) { }
