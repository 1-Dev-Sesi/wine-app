package com.devsesi.wineapp.ui.activities.home;

import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.clearSavedCredentials;
import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.getSavedUsername;
import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.getUserRoles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devsesi.wineapp.R;
import com.devsesi.wineapp.ui.activities.MainActivity;
import com.devsesi.wineapp.ui.activities.pages.role1.Role1Activity;
import com.devsesi.wineapp.ui.activities.pages.role2.Role2Activity;
import com.devsesi.wineapp.ui.activities.pages.role3.Role3Activity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
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

        String username = getSavedUsername(HomeActivity.this);

        TextView textViewLoggedAs = findViewById(R.id.textViewLoggedAs);
        textViewLoggedAs.setText("Logged as: " + username);

        List<String> userRoles = getUserRoles(HomeActivity.this);
        enableButtonsBasedOnUserRoles(userRoles);

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedCredentials(HomeActivity.this);

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

        buttonCargo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Role1Activity.class);
                startActivity(intent);
            }
        });

        buttonCargo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Role2Activity.class);
                startActivity(intent);
            }
        });

        buttonCargo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Role3Activity.class);
                startActivity(intent);
            }
        });
    }
}