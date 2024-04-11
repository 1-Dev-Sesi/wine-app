package com.devsesi.wineapp.ui.activities;

import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.clearSavedCredentials;
import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.getSavedPassword;
import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.getSavedUsername;
import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.saveUserRoles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devsesi.wineapp.R;
import com.devsesi.wineapp.ui.activities.home.HomeActivity;
import com.devsesi.wineapp.ui.activities.login.LoginActivity;
import com.devsesi.wineapp.ui.activities.login.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String savedUsername = getSavedUsername(MainActivity.this);
        String savedPassword = getSavedPassword(MainActivity.this);

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
                                        saveUserRoles(MainActivity.this, userRoles);

                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        clearSavedCredentials(MainActivity.this);
                                    }
                                } else {
                                    clearSavedCredentials(MainActivity.this);
                                }
                            } else {
                                clearSavedCredentials(MainActivity.this);
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