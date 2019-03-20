package com.TCC.webhome;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Cadastrar extends AppCompatActivity {
    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText emailField;
    private EditText senhaField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        Button btn_cadastrar = findViewById(R.id.btn_cadastrar);
        emailField = findViewById(R.id.edt_email);
        senhaField = findViewById(R.id.edt_senha);

        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar(emailField.getText().toString(), senhaField.getText().toString());
            }
        });
    }

    public void cadastrar(String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            statusLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Cadastrar.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            statusLogin(null);
                        }
                    }
                });
    }

    private void statusLogin(Object cadastro) {
        if (cadastro == null) {
            Toast.makeText(getApplicationContext(), "Erro ao cadastrar", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }

}
