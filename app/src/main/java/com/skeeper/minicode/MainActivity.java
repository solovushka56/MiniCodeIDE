package com.skeeper.minicode;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.databinding.ActivityMainBinding;
import com.skeeper.minicode.models.KeySymbolItemModel;
import com.skeeper.minicode.singleton.PanelSnippetsDataSingleton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(Color.TRANSPARENT);



//        SharedPreferences sharedPreferences = getSharedPreferences(
//                "com.skeeper.minicode",
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();

        File keySymbolConfigFile = new File(getFilesDir(), "keySymbolsData.json");
        if (!keySymbolConfigFile.exists()) {
            PanelSnippetsDataSingleton.saveList(
                    this, "keySymbolsData.json", new ArrayList<>(Arrays.asList(

                    new KeySymbolItemModel(1, "tab", "    "),
                    new KeySymbolItemModel(1, "{}", "{}"),
                    new KeySymbolItemModel(1, "[]", "[]"),
                    new KeySymbolItemModel(1, "()", "()"),
                    new KeySymbolItemModel(1, "!", "!"),
                    new KeySymbolItemModel(1, "pb", "public"),
                    new KeySymbolItemModel(1, "pr", "private")
            )));
        }

        binding.startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);

//            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);
//            var bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
//            overridePendingTransition(R.anim.slide_up, R.anim.fade_out);
        });


        

    }
}