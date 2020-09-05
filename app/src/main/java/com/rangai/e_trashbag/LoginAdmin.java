package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAdmin extends AppCompatActivity {

    EditText musername, mpassword;

    ProgressDialog progressDialog;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Login Admin");

        musername = (EditText) findViewById(R.id.userNameAdmin);
        mpassword = (EditText) findViewById(R.id.passwordAdmin);
        Button button = findViewById(R.id.login_btn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login Loading");
        progressDialog.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = musername.getText().toString().trim();
                password = mpassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)){
                    musername.setError("Username Salah");
                    musername.setFocusable(true);
                }else if (TextUtils.isEmpty(password)){
                    mpassword.setError("Password Salah");
                    mpassword.setFocusable(true);
                }else {
                    loginAdmin();
                }

            }
        });

    }

    private void loginAdmin() {

        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String user = ""+ds.child("username").getValue();
                    String pass = ""+ds.child("password").getValue();


                    if (username.equals(user) && password.equals(pass)){
                        Intent intent = new Intent(LoginAdmin.this,halamanAdmin.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(LoginAdmin.this, "Login Sukses",Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();

                    }
                    if (!username.equals(user) && password.equals(pass)) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginAdmin.this, "Authentication failed", Toast.LENGTH_LONG).show();
                        musername.setText("");
                        mpassword.setText("");
                        musername.setFocusable(true);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(LoginAdmin.this,""+databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
