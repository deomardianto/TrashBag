package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class lupa extends AppCompatActivity {

    private EditText lupaPW;
    private Button sendEmail;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Forgot Password");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        lupaPW = findViewById(R.id.emailLupa);
        sendEmail = findViewById(R.id.buttonLupa);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu bentar ya bosque");
        progressDialog.setTitle("Mengirimkan Email");
        progressDialog.setCancelable(false);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lupaPW.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    lupaPW.setError("Emailnya Salah");
                    return;
                }else {

                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(lupa.this,"Coba Cek Emailnya Buat Ganti Password", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(lupa.this,"Error : "+message,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
