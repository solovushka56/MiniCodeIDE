package com.skeeper.minicode.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurePreferences {
    private final SharedPreferences prefs;

    public SecurePreferences(Context context, String prefName) {
        prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        try {
            String encrypted = KeyStoreHelper.encrypt(value);
            prefs.edit().putString(key, encrypted).apply();
        } catch (Exception e) {
            throw new SecurityException("Encryption failed", e);
        }
    }

    public String getString(String key) {
        String encrypted = prefs.getString(key, null);
        if (encrypted == null) return null;

        try {
            return KeyStoreHelper.decrypt(encrypted);
        } catch (Exception e) {
            throw new SecurityException("Decryption failed", e);
        }
    }
}