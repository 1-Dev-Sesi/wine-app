package com.devsesi.wineapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private void saveCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
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

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonRegister = findViewById(R.id.buttonRegisterPage);
        EditText editTextUser = findViewById(R.id.editTextUsernameRegister);
        EditText editTextPassword = findViewById(R.id.editTextPasswordRegister);

        MultiAutoCompleteTextView multiAutoCompleteTextViewRoles = findViewById(R.id.multiAutoCompleteTextViewRoles);

        List<String> rolesList = new ArrayList<>();
        rolesList.add("Cargo 1");
        rolesList.add("Cargo 2");
        rolesList.add("Cargo 3");

        ArrayAdapter<String> rolesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, rolesList);

        multiAutoCompleteTextViewRoles.setAdapter(rolesAdapter);
        multiAutoCompleteTextViewRoles.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUser.getText().toString();
                String password = editTextPassword.getText().toString();
                String selectedRoles = multiAutoCompleteTextViewRoles.getText().toString();

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
                                            Toast.makeText(RegisterActivity.this, "Nome de usuário já está em uso", Toast.LENGTH_SHORT).show();
                                        } else {
                                            List<String> roles = Arrays.asList(selectedRoles.split("\\s*,\\s*"));

                                            User newUser = new User(username, password, roles);

                                            FirebaseFirestore.getInstance().collection("users")
                                                    .document(username)
                                                    .set(newUser)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(RegisterActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                                                                Log.d("Registro", "Usuário registrado com sucesso!");

                                                                List<String> userRoles = (List<String>) document.get("roles");

                                                                saveCredentials(username, password);
                                                                saveUserRoles(userRoles);

                                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Log.e("Registro", "Erro ao registrar usuário: " + task.getException().getMessage());
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Log.e("Registro", "Erro ao consultar Firestore: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Por favor, preencha usuário e senha", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
