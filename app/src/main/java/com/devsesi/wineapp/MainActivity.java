package com.devsesi.wineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String getSavedUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    private String getSavedPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    private void clearSavedCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void saveUserRoles(List<String> userRoles) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder rolesStringBuilder = new StringBuilder();
        for (String role : userRoles) {
            rolesStringBuilder.append(role).append(",");
        }

        String rolesString = rolesStringBuilder.deleteCharAt(rolesStringBuilder.length() - 1).toString();
        editor.putString("user_roles", rolesString);
        editor.apply();
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String savedUsername = getSavedUsername();
        String savedPassword = getSavedPassword();

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            db.collection("users")
                    .document(savedUsername)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String storedPassword = document.getString("password");
                                    if (storedPassword.equals(savedPassword)) {
                                        List<String> userRoles = (List<String>) document.get("roles");
                                        saveUserRoles(userRoles);

                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        clearSavedCredentials();
                                    }
                                } else {
                                    clearSavedCredentials();
                                }
                            } else {
                                clearSavedCredentials();
                            }
                        }
                    });
        }

        Button buttonLogin = findViewById(R.id.buttonRegisterPage);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}