package com.skeeper.minicode.data.remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CompileApiService {
    @POST("api/compile")
    Call<CompileResponse> compileCode(@Body CompileRequest request);
}
