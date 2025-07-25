package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// todo integrate api keys
//

public class CompileViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final MutableLiveData<String> compileResult = new MutableLiveData<>();



    public void compile(String serverUrl, List<File> files) {
        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("language", "java");

            for (File file : files) {
                builder.addFormDataPart(
                        "files",
                        file.getName(),
                        RequestBody.create(file, MediaType.parse("text/plain"))
                );
            }

            Request request = new Request.Builder()
                    .url(serverUrl)
                    .post(builder.build())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                compileResult.postValue(response.body().string());
            } catch (IOException e) {
                compileResult.postValue(e.getMessage());
            }
        });
    }

    public MutableLiveData<String> getCompileResult() {
        return compileResult;
    }

}
