package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    ActionBar actionBar;
    EditText mEmail,mPassword;
    Button mLogin;

    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.emailLOGIN);
        mPassword = (EditText) findViewById(R.id.passwordLOGIN);
        mLogin = (Button) findViewById(R.id.login_btn);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Emailnya Salah");
                    mEmail.setFocusable(true);
                }else if (password.equals("")){
                    mPassword.setError("Passwordnya Salah");
                    mPassword.setFocusable(true);
                } else {
                    loginUser(email,password);
                }
            }
        });


        pd = new ProgressDialog(this);
        pd.setMessage("Tunggu bentar ya bosque");
        pd.setTitle("Proses Masuk");
        pd.setCancelable(false);


    }

    private void loginUser(String email, String password) {

        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent pindah = new Intent(Login.this, MainActivity.class);
                            startActivity(pindah);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            mPassword.setText("");
                            mPassword.setFocusable(true);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(Login.this,"Passwordnya Salah", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void lupaPW(View view) {
        Intent lupa = new Intent(this, lupa.class);
        startActivity(lupa);
    }

    public void daftar(View view) {
        Intent daftar = new Intent(this, daftarAkun.class);
        startActivity(daftar);
    }

}

