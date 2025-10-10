package com.skeeper.minicode.data.repos.compilation;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.remote.CompileApiService;
import com.skeeper.minicode.data.remote.CompileRetrofitClient;
import com.skeeper.minicode.domain.contracts.repos.compilation.ICompilerRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IDirectoryRepository;
import com.skeeper.minicode.domain.models.CompileArgs;
import com.skeeper.minicode.domain.models.CompileRequest;
import com.skeeper.minicode.domain.models.CompileResponse;
import com.skeeper.minicode.domain.usecases.file.GetExtensionUseCase;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

public class CompilerRepository implements ICompilerRepository {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    IDirectoryRepository directoryRepository;

    @Inject
    ProjectManager projectManager;


    @Override
    public void getCompilationResult(CompileRequest request,
                                     String projectName,
                                     ICompileCallback callback) {
        executorService.execute(() -> {
            var client = new CompileRetrofitClient();
            CompileApiService api = client.getClient().create(CompileApiService.class);


            Call<CompileResponse> call = api.compileCode(request);

            call.enqueue(new Callback<CompileResponse>() {

                @Override
                public void onResponse(Call<CompileResponse> call,
                                       retrofit2.Response<CompileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CompileResponse result = response.body();
                        callback.onSuccess(result);
                    }
                    else {
                        callback.onError(new Exception(
                                "Server error: " + String.valueOf(response.code())));
                    }
                }

                @Override
                public void onFailure(Call<CompileResponse> call, Throwable t) {
                    callback.onError(new Exception("Network error: " + t.getMessage()));
                }
            });
        });



    }
}
