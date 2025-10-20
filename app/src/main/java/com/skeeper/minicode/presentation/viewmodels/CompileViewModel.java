package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.repos.compilation.ICompilerRepository;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.models.CompileRequest;
import com.skeeper.minicode.domain.models.CompileResponse;
import com.skeeper.minicode.domain.contracts.repos.file.IDirectoryRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.usecases.file.GetExtensionUseCase;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

// todo integrate api keys
//

@HiltViewModel
public class CompileViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<CompileResponse> compileResult = new MutableLiveData<>();
    private final MutableLiveData<String> compileException = new MutableLiveData<>();

    private final IFileContentRepository fileContentRepository;
    private final IDirectoryRepository directoryRepository;
    private final ICompilerRepository compilerRepository;

    @Inject ProjectManager projectManager;

    @Inject
    public CompileViewModel(IFileContentRepository fileContentRepository,
                            IDirectoryRepository directoryRepository,
                            ICompilerRepository compilerRepository) {
        this.fileContentRepository = fileContentRepository;
        this.directoryRepository = directoryRepository;
        this.compilerRepository = compilerRepository;
    }

    public void compileAsync(String projectName) {
        executor.execute(() -> compile(projectName));
    }

    private void compile(String projectName) {
        var metadata = projectManager.loadProjectModel(projectName);
        var rootDirectory = projectManager.getProjectDir(projectName);

        var filesMap = directoryRepository.getFilesContentMap(rootDirectory.getPath());
        var mainFile = new File(metadata.mainFilePath());

        if (metadata.mainFilePath().isEmpty() ||
                !mainFile.exists()) {
            compileException.postValue("Main file not selected");
            return;
        }

        var extension = new GetExtensionUseCase().execute(mainFile);
        if (extension != ExtensionType.PYTHON && extension != ExtensionType.JAVA) {
            compileException.postValue("Unsupported language");
            return;
        }


        var request = new CompileRequest(
                extension.name().toLowerCase(), // as extension
                filesMap,
                mainFile.getName(),
                new String[] {}
        );

        compilerRepository.getCompilationResult(request, projectName,
                new ICompilerRepository.ICompileCallback() {
            @Override
            public void onSuccess(CompileResponse response) {
                compileResult.postValue(response);
            }

            @Override
            public void onError(Exception exception) {
                compileException.postValue(exception.getMessage());
            }
        });
    }

    public MutableLiveData<CompileResponse> getCompileResult() {
        return compileResult;
    }

    public MutableLiveData<String> getCompileException() {
        return compileException;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
