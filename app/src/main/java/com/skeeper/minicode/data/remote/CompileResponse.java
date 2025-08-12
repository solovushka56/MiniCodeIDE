package com.skeeper.minicode.data.remote;

public record CompileResponse(boolean success,
                              String output,
                              String errors,
                              String executionTime) { }
