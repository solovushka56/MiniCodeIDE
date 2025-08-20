package com.skeeper.minicode.domain.contracts.repos.compilation;

import android.view.PixelCopy;

import com.skeeper.minicode.domain.models.CompileArgs;
import com.skeeper.minicode.domain.models.CompileRequest;
import com.skeeper.minicode.domain.models.CompileResponse;

public interface ICompilerRepository {
    void getCompilationResult(
            CompileRequest request,
            String projectName,
            ICompileCallback callback);


    interface ICompileCallback {
        void onSuccess(CompileResponse response);
        void onError(Exception exception);
    }
}
