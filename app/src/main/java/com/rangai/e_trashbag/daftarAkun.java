package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class daftarAkun extends AppCompatActivity {

    EditText mNama, mHP, mEmail, mPW, mKonfirmPW;
    Button buttonSubmit;
    ProgressDialog progressDialog;

    /*ImageView imgview;
    ImageButton mProfil_btn;
    Uri FilePathUri;
    int Image_Request_Code = 7;*/


    String TAG = "TAG";
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_akun);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mNama = (EditText) findViewById(R.id.namaID);
        mHP = (EditText) findViewById(R.id.nomorhpID);
        mEmail = (EditText) findViewById(R.id.emailID);
        mPW = (EditText) findViewById(R.id.passwordID);
        mKonfirmPW = (EditText) findViewById(R.id.konfirmPasswordID);

        buttonSubmit = (Button) findViewById(R.id.registerID);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("User Data");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Data");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tunggu bentar ya bosque");
        progressDialog.setTitle("Mendaftarkan Akun");
        progressDialog.setCancelable(false);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nama = mNama.getText().toString().trim();
                String Hp = mHP.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPW.getText().toString().trim();
                String ConfirmPassword = mKonfirmPW.getText().toString().trim();

                if (TextUtils.isEmpty(nama)){
                    mNama.setError("Namanya jangan kosong");
                    return;
                }else if (TextUtils.isEmpty(Hp)){
                    mHP.setError("Nomor hpnya jangan kosong");
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Emailnya yang bener");
                    return;
                }else if (password.length()<8){
                    mPW.setError("Passwordnya jangan kurang dari 8");
                    return;
                }else if (!ConfirmPassword.equals(password)){
                    mKonfirmPW.setError("Passwordnya harus sama");
                    return;
                }else {

                    createUser(email,password);

                }

            }
        });

    }

    private void createUser(String email, String password) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();

                    String uid = user.getUid();
                    String email = user.getEmail();
                    String nama = mNama.getText().toString().trim();
                    String phone = mHP.getText().toString().trim();

                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("nama", nama);
                    hashMap.put("phone",phone);
                    hashMap.put("image", "");
                    hashMap.put("cover", "");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");

                    reference.child(uid).setValue(hashMap);

                    Toast.makeText(daftarAkun.this,"Akun Terdaftar",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    mAuth.signOut();

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(daftarAkun.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(daftarAkun.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
