package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class halamanAdmin extends AppCompatActivity {

    List<dataAdmin> list;
    AdapterAdmin adapter;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private DatabaseReference mreference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_admin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Admin");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        mRecycler = findViewById(R.id.recycle_admin);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        mreference = FirebaseDatabase.getInstance().getReference().child("Order");
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressDialog.show();
                list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    dataAdmin data = ds.getValue(dataAdmin.class);
                    list.add(data);

                }
                adapter = new AdapterAdmin(halamanAdmin.this,list);
                mRecycler.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
                Toast.makeText(halamanAdmin.this,""+databaseError.getMessage(),Toast.LENGTH_LONG).show();

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
