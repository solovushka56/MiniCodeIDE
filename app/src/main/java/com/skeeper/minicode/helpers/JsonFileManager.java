package com.skeeper.minicode.helpers;

import android.content.Context;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.io.IOException;

public class JsonFileManager {
    private static final String FILE_NAME = "user_data.json";

//    public static void saveData(Context context, String name, int age) {
//        UserData userData = new UserData(name, age);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(userData);
//
//        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
//            fos.write(json.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}