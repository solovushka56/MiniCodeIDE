package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.remote.CompileApiService;
import com.skeeper.minicode.data.remote.CompileRequest;
import com.skeeper.minicode.data.remote.CompileResponse;
import com.skeeper.minicode.data.remote.CompileRetrofitClient;
import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

// todo integrate api keys
//

@HiltViewModel
public class CompileViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final MutableLiveData<Boolean> compiledResponse = new MutableLiveData<>(false);

    private final MutableLiveData<String> _output = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> _compileTime = new MutableLiveData<>();

    IFileContentRepository fileContentRepository;

    @Inject
    ProjectManager projectManager;

    @Inject
    public CompileViewModel(IFileContentRepository fileContentRepository) {
        this.fileContentRepository = fileContentRepository;
    }

    public void compile(String projectName, List<File> files) {
        _isLoading.setValue(true);
        var client = new CompileRetrofitClient();
        CompileApiService api = client.getClient().create(CompileApiService.class);

        var metadata = projectManager.loadProjectModel(projectName);

        var filesDict = getFilesContent(new File(metadata.path()));
        var request = new CompileRequest(
                "python",
                filesDict,
                "main.py",
                new String[] {});

        Call<CompileResponse> call = api.compileCode(request);

        call.enqueue(new Callback<CompileResponse>() {

            @Override
            public void onResponse(Call<CompileResponse> call,
                                   retrofit2.Response<CompileResponse> response) {
                _isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    CompileResponse result = response.body();
                    if (result.success()) {
                        _output.setValue(result.output());
                    } else {
                        _error.setValue(result.errors());
                    }
                } else {
                    _error.setValue("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CompileResponse> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue("Network error: " + t.getMessage());
            }
        });
    }



    public static List<File> getAllFiles(File directory) {
        List<File> fileList = new ArrayList<>();
        if (directory == null || !directory.exists() || !directory.isDirectory())
            return fileList;

        File[] files = directory.listFiles();
        if (files == null) return fileList;

        for (File file : files) {
            if (file.isFile()) fileList.add(file);
            else if (file.isDirectory()) fileList.addAll(getAllFiles(file));
        }
        return fileList;
    }

    public static Map<String, String> getFilesContent(File rootDir) {
        Map<String, String> resultMap = new HashMap<>();
        List<File> allFiles = getAllFiles(rootDir);

        for (File file : allFiles) {
            if (!file.isFile()) continue;

            try {
                String content = new String(
                        Files.readAllBytes(file.toPath()),
                        StandardCharsets.UTF_8
                );
                resultMap.put(file.getName(), content);
            }
            catch (IOException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

}
