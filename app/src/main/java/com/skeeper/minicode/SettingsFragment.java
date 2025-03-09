package com.skeeper.minicode;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skeeper.minicode.databinding.FragmentProjectsBinding;
import com.skeeper.minicode.databinding.FragmentSettingsBinding;
import com.skeeper.minicode.databinding.SettingsCategoryItemBinding;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {


    FragmentSettingsBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSettingsPanels();

    }

    private void initSettingsPanels() {
        List<View> panels = new ArrayList<>();



        for (int i = 0; i < binding.panelsContainer.getChildCount(); i++) {
            panels.add(binding.panelsContainer.getChildAt(i));
        }

        for (View panel : panels) {
            if (panel instanceof SettingItemView) {
                ((SettingItemView) panel).setIcon(R.drawable.icon);
                ((SettingItemView) panel).setText("hello");
            }
        }
//        binding.editorSetting.settingIcon.setImageResource(R.drawable.settings_icon);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}