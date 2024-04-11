package com.devsesi.wineapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private String getSavedUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private List<String> getUserRoles() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String userRolesString = sharedPreferences.getString("user_roles", "");

        return Arrays.asList(userRolesString.split(","));
    }
    private void clearSavedCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String username = getSavedUsername();

        // Atualizar o TextView com o nome de usuário
        TextView textViewLoggedAs = findViewById(R.id.textViewLoggedAs);
        textViewLoggedAs.setText("Logged as: " + username);

        List<String> userRoles = getUserRoles();
        enableButtonsBasedOnUserRoles(userRoles);

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedCredentials();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void enableButtonsBasedOnUserRoles(List<String> userRoles) {
        Button buttonCargo1 = findViewById(R.id.buttonPageCargo1);
        Button buttonCargo2 = findViewById(R.id.buttonPageCargo2);
        Button buttonCargo3 = findViewById(R.id.buttonPageCargo3);

        buttonCargo1.setEnabled(userRoles.contains("Cargo 1"));
        buttonCargo2.setEnabled(userRoles.contains("Cargo 2"));
        buttonCargo3.setEnabled(userRoles.contains("Cargo 3"));
    }
}