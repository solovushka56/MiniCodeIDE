package com.skeeper.minicode.data.repos;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Singleton
public class UserRepository {
    private final SharedPreferences sharedPreferences;

    @Inject
    public UserRepository(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("fld", e);
        }
    }

    public void saveUserCredentials(String username, String pat, String email) {
        String hashedPat = BCrypt.withDefaults().hashToString(12, pat.toCharArray());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("pat", hashedPat);
        editor.putString("email", email);
        editor.apply();
    }

    public boolean verifyUserCredentials(String username, String password, String email) {
        String savedUsername = sharedPreferences.getString("username", "");
        String savedHashedPat = sharedPreferences.getString("password", "");
        String savedEmail = sharedPreferences.getString("email", "");

        if (!username.equals(savedUsername))
            return false;
        if (!email.equals(savedEmail)) return false;
        // todo

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), savedHashedPat);
        return result.verified;
    }
}