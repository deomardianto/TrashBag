package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class detailOrderan extends AppCompatActivity {

    TextView mNama, mEmail, mPhone, mType, mTimeStamp, mKode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_orderan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Detail Orderan");

        mNama = findViewById(R.id.detail_nama);
        mEmail = findViewById(R.id.detail_email);
        mPhone = findViewById(R.id.detail_phone);
        mType = findViewById(R.id.detail_type);
        mTimeStamp = findViewById(R.id.detail_waktu);
        mKode = findViewById(R.id.detail_lokasi);

        Intent intent = getIntent();
        String timeStamp = intent.getStringExtra("timeStamp");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Order");
        Query query = reference.orderByChild("timeStamp").equalTo(timeStamp);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String nama = ""+ds.child("nama").getValue();
                    String email = ""+ds.child("email").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    String type = ""+ds.child("TypeOrder").getValue();
                    String kode = ""+ds.child("linkMaps").getValue();
                    String timeStamp = ""+ds.child("timeStamp").getValue();

                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(timeStamp));
                    String waktu = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

                    mNama.setText(nama);
                    mEmail.setText(email);
                    mPhone.setText(phone);
                    mType.setText(type);
                    mKode.setText(kode);
                    mTimeStamp.setText(waktu);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(detailOrderan.this,""+databaseError.getMessage(),Toast.LENGTH_LONG).show();

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
