package com.skeeper.minicode.data.mappers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.models.LangModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LangSyntaxParser {
    public static LangModel parse(Context context, int langRawRes) {
        try (InputStream inputStream = context.getResources().openRawResource(langRawRes)) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            var gson = new Gson();
            return gson.fromJson(reader, LangModel.class);
        }
        catch (Exception e) {
            Log.e("PARSING", "json dont parse in " + "LangSyntaxParser");
            return null;
        }
    }

}