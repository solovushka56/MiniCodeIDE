package com.skeeper.minicode.services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilesystemService {


    public void saveText(String text, String filename, Context context) {
        FileOutputStream outputStream = null;

        try {

            outputStream = context.openFileOutput(filename, MODE_PRIVATE);

            outputStream.write(text.getBytes());
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Nullable
    public String getTextFromFile(String filename, Context context) {
        String text = null;

        FileInputStream fin = null;
        try {
            fin = context.openFileInput(filename);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String(bytes);
        }
        catch (IOException ex) {

            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return text;
    }




}