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

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText senhaField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_logar = findViewById(R.id.btn_logar);
        emailField = findViewById(R.id.edt_email);
        senhaField = findViewById(R.id.edt_senha);

        //Toast.makeText(getApplicationContext(), "Bem vindo de volta eeeeaaaa", Toast.LENGTH_LONG).show();

        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mAuth == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar(emailField.getText().toString(), senhaField.getText().toString());
            }
        });

    }
    private void logar(String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Login efeuado com sucesso");
                            FirebaseUser user = mAuth.getCurrentUser();
                            statusLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Falha na Autenticação",
                                    Toast.LENGTH_SHORT).show();
                            statusLogin(null);
                        }
                    }
                });
    }

    private void statusLogin(Object logado) {
        if (logado == null) {
            Toast.makeText(getApplicationContext(), "Senha ou Usuário inválido", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, TelaControle.class);
            startActivity(intent);
            finish();
        }
    }
}
