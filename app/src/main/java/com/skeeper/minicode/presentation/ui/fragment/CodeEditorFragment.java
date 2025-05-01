package com.skeeper.minicode.presentation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amrdeveloper.codeview.CodeView;
import com.skeeper.minicode.databinding.FragmentCodeEditorBinding;
import com.skeeper.minicode.domain.models.FileItem;


public class CodeEditorFragment extends Fragment {
    FragmentCodeEditorBinding binding;

    public FileItem boundFileItem = null;
    public CodeView codeView = null;

    public CodeEditorFragment(FileItem fileItem) {
        this.boundFileItem = fileItem;
    }
    public CodeEditorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCodeEditorBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        codeView = binding.codeViewMain;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }






}