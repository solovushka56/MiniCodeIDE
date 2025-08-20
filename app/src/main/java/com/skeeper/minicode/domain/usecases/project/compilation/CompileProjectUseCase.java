package com.skeeper.minicode.domain.usecases.project.compilation;

import com.skeeper.minicode.domain.contracts.repos.compilation.ICompilerRepository;
import com.skeeper.minicode.domain.models.CompileRequest;

import javax.inject.Inject;

public class CompileProjectUseCase {

    private final ICompilerRepository compilerRepository;

    @Inject
    public CompileProjectUseCase(ICompilerRepository compilerRepository) {
        this.compilerRepository = compilerRepository;
    }

    public String execute(CompileRequest args) {
//        return compilerRepository.getCompilationResult(args);
        return null;
    }

}
