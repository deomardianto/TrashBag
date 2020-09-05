package com.rangai.e_trashbag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    int isi = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        firebaseAuth  = FirebaseAuth.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigatio_layout);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        navigationView.setSelectedItemId(R.id.nav_home);
        actionBar.setTitle("Home");
        HomeFragment fragment2 = new HomeFragment();
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.replace(R.id.f1_container, fragment2, "");
        ft2.commit();

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_user:

                            actionBar.setTitle("Profile");
                            UserFragment fragment1 = new UserFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.f1_container, fragment1, "");
                            ft1.commit();

                            return true;
                        case R.id.nav_home:

                            actionBar.setTitle("Home");
                            HomeFragment fragment2 = new HomeFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.f1_container, fragment2, "");
                            ft2.commit();

                            return true;
                        case R.id.nav_history:

                            actionBar.setTitle("History");
                            if (isi == 0){
                                history_kosong fragment4 = new history_kosong();
                                FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                                ft4.replace(R.id.f1_container,fragment4,"");
                                ft4.commit();
                            }else{
                                HistoryFragment fragment3 = new HistoryFragment();
                                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                                ft3.replace(R.id.f1_container, fragment3, "");
                                ft3.commit();
                            }

                            return true;
                    }

                    return false;
                }
            };

    public void checkStatus(){

        FirebaseUser User = firebaseAuth.getCurrentUser();
        if (User != null){

            String email = User.getEmail();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Order");
            Query query = reference.orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        isi+=1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this,""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        checkStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkStatus();
        }else if (id == R.id.admin){
            Intent keAdmin = new Intent(MainActivity.this, LoginAdmin.class);
            startActivity(keAdmin);
        }else if (id == R.id.action_HubungiKami){
            String[] TO = {"deo_mardianto@teknokrat.ac.id"};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.action_informasi){
            Intent pindahInformasi = new Intent(this,informasiAplikasi.class);
            startActivity(pindahInformasi);
        }
        return super.onOptionsItemSelected(item);
    }

}
