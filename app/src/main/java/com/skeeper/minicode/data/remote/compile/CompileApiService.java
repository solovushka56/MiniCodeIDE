package com.skeeper.minicode.data.remote.compile;

import com.skeeper.minicode.domain.models.CompileRequest;
import com.skeeper.minicode.domain.models.CompileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompileApiService {
    @POST("api/compile")
    Call<CompileResponse> compileCode(@Body CompileRequest request);
}
