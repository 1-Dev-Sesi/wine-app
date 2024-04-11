package com.devsesi.wineapp.ui.activities.login;

import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.saveCredentials;
import static com.devsesi.wineapp.ui.utils.SharedPreferencesUtils.saveUserRoles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devsesi.wineapp.R;
import com.devsesi.wineapp.ui.activities.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonLogin = findViewById(R.id.buttonLoginPage);
        EditText editTextUser = findViewById(R.id.editTextUsernameLogin);
        EditText editTextPassword = findViewById(R.id.editTextPasswordLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUser.getText().toString();
                String password = editTextPassword.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    FirebaseFirestore.getInstance().collection("users")
                            .document(username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String storedPassword = document.getString("password");
                                            if (storedPassword.equals(password)) {
                                                List<String> userRoles = (List<String>) document.get("roles");

                                                Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                                                saveCredentials(LoginActivity.this, username, password);
                                                saveUserRoles(LoginActivity.this, userRoles);

                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Erro ao consultar o Firestore", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha usuário e senha", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}