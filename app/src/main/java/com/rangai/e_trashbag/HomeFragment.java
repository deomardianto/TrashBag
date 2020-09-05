package com.rangai.e_trashbag;


import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button langganan, sekarang;

    private FirebaseAuth mAuth;
    FirebaseUser user;


    double mLongitude, mLatitude;
    private static int REQUEST_LOCATION = 1;
    LocationManager lm;

    ProgressDialog progressDialog;

    ActionBar actionBar;

    CompactCalendarView calendarView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

    Event event1, event2, event3, event4, event5, event6, event7, event8, event9, event10;
    Event event11, event12, event13, event14, event15, event16, event17, event18, event19, event20;
    Event event21, event22, event23, event24, event25, event26, event27, event28, event29, event30;
    Event event31, event32, event33, event34, event35, event36, event37, event38, event39, event40;
    Event event41, event42, event43, event44, event45, event46, event47, event48, event49, event50;
    Event event51, event52, event53, event54, event55, event56, event57, event58, event59, event60;
    String sampah = "Jadwal Pengambilan Sampah";


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        langganan = view.findViewById(R.id.but1);
        sekarang = view.findViewById(R.id.but2);
        final TextView month = view.findViewById(R.id.bulan);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        calendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        month.setText("" + dateFormat.format(calendarView.getFirstDayOfCurrentMonth()));

        eventSampah();

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month.setText("" + dateFormat.format(firstDayOfNewMonth));
            }
        });


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        langganan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option[] = {"Bulanan", "Tahunan"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Pilih Paket Langganan");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setTitle("Peringatan");
                            builder1.setMessage("Ingin melanjutkan order ?");
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String tipe = "Order Berlangganan Bulanan";
                                    uploadOrder(tipe);

                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Order dibatalkan", Toast.LENGTH_LONG).show();
                                }
                            });

                            builder1.create().show();

                        } else if (which == 1) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setTitle("Peringatan");
                            builder1.setMessage("Ingin melanjutkan order ?");
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String tipe = "Order Berlangganan Tahunan";
                                    uploadOrder(tipe);

                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Order dibatalkan", Toast.LENGTH_LONG).show();
                                }
                            });

                            builder1.create().show();

                        }

                    }
                });
                builder.create().show();
            }
        });

        sekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Peringatan");
                builder1.setMessage("Ingin melanjutkan order ?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tipe = "Order Sekarang";
                        uploadOrder(tipe);

                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Order dibatalkan", Toast.LENGTH_LONG).show();
                    }
                });

                builder1.create().show();
            }
        });


        return view;
    }

    private void eventSampah() {

        event1 = new Event(Color.BLACK, 1578286800000L, sampah);
        calendarView.addEvent(event1);
        event2 = new Event(Color.BLACK, 1578546000000L, sampah);
        calendarView.addEvent(event2);
        event3 = new Event(Color.BLACK, 1578805200000L, sampah);
        calendarView.addEvent(event3);
        event4 = new Event(Color.BLACK, 1579064400000L, sampah);
        calendarView.addEvent(event4);
        event5 = new Event(Color.BLACK, 1579323600000L, sampah);
        calendarView.addEvent(event5);
        event6 = new Event(Color.BLACK, 1579582800000L, sampah);
        calendarView.addEvent(event6);
        event7 = new Event(Color.BLACK, 1579842000000L, sampah);
        calendarView.addEvent(event7);
        event8 = new Event(Color.BLACK, 1580101200000L, sampah);
        calendarView.addEvent(event8);
        event9 = new Event(Color.BLACK, 1580360400000L, sampah);
        calendarView.addEvent(event9);
        event10 = new Event(Color.BLACK, 1580619600000L, sampah);
        calendarView.addEvent(event10);
        event11 = new Event(Color.BLACK, 1580878800000L, sampah);
        calendarView.addEvent(event11);
        event12 = new Event(Color.BLACK, 1581138000000L, sampah);
        calendarView.addEvent(event12);
        event13 = new Event(Color.BLACK, 1581397200000L, sampah);
        calendarView.addEvent(event13);
        event14 = new Event(Color.BLACK, 1581656400000L, sampah);
        calendarView.addEvent(event14);
        event15 = new Event(Color.BLACK, 1581915600000L, sampah);
        calendarView.addEvent(event15);
        event16 = new Event(Color.BLACK, 1582174800000L, sampah);
        calendarView.addEvent(event16);
        event17 = new Event(Color.BLACK, 1582434000000L, sampah);
        calendarView.addEvent(event17);
        event18 = new Event(Color.BLACK, 1582693200000L, sampah);
        calendarView.addEvent(event18);
        event19 = new Event(Color.BLACK, 1582952400000L, sampah);
        calendarView.addEvent(event19);
        event20 = new Event(Color.BLACK, 1583211600000L, sampah);
        calendarView.addEvent(event20);
        event21 = new Event(Color.BLACK, 1583470800000L, sampah);
        calendarView.addEvent(event21);
        event22 = new Event(Color.BLACK, 1583730000000L, sampah);
        calendarView.addEvent(event22);
        event23 = new Event(Color.BLACK, 1583989200000L, sampah);
        calendarView.addEvent(event23);
        event24 = new Event(Color.BLACK, 1584248400000L, sampah);
        calendarView.addEvent(event24);
        event25 = new Event(Color.BLACK, 1584507600000L, sampah);
        calendarView.addEvent(event25);
        event26 = new Event(Color.BLACK, 1584766800000L, sampah);
        calendarView.addEvent(event26);
        event27 = new Event(Color.BLACK, 1585026000000L, sampah);
        calendarView.addEvent(event27);
        event28 = new Event(Color.BLACK, 1585285200000L, sampah);
        calendarView.addEvent(event28);
        event29 = new Event(Color.BLACK, 1585544400000L, sampah);
        calendarView.addEvent(event29);
        event30 = new Event(Color.BLACK, 1585803600000L, sampah);
        calendarView.addEvent(event30);
        event31 = new Event(Color.BLACK, 1586062800000L, sampah);
        calendarView.addEvent(event31);
        event32 = new Event(Color.BLACK, 1586322000000L, sampah);
        calendarView.addEvent(event32);
        event33 = new Event(Color.BLACK, 1586581200000L, sampah);
        calendarView.addEvent(event33);
        event34 = new Event(Color.BLACK, 1586840400000L, sampah);
        calendarView.addEvent(event34);
        event35 = new Event(Color.BLACK, 1587099600000L, sampah);
        calendarView.addEvent(event35);
        event36 = new Event(Color.BLACK, 1587358800000L, sampah);
        calendarView.addEvent(event36);
        event37 = new Event(Color.BLACK, 1587618000000L, sampah);
        calendarView.addEvent(event37);
        event38 = new Event(Color.BLACK, 1587877200000L, sampah);
        calendarView.addEvent(event38);
        event39 = new Event(Color.BLACK, 1588136400000L, sampah);
        calendarView.addEvent(event39);
        event40 = new Event(Color.BLACK, 1588395600000L, sampah);
        calendarView.addEvent(event40);
        event41 = new Event(Color.BLACK, 1588654800000L, sampah);
        calendarView.addEvent(event41);
        event42 = new Event(Color.BLACK, 1588914000000L, sampah);
        calendarView.addEvent(event42);
        event43 = new Event(Color.BLACK, 1589173200000L, sampah);
        calendarView.addEvent(event43);
        event44 = new Event(Color.BLACK, 1589432400000L, sampah);
        calendarView.addEvent(event44);
        event45 = new Event(Color.BLACK, 1589691600000L, sampah);
        calendarView.addEvent(event45);
        event46 = new Event(Color.BLACK, 1589950800000L, sampah);
        calendarView.addEvent(event46);
        event47 = new Event(Color.BLACK, 1590210000000L, sampah);
        calendarView.addEvent(event47);
        event48 = new Event(Color.BLACK, 1590469200000L, sampah);
        calendarView.addEvent(event48);
        event49 = new Event(Color.BLACK, 1590728400000L, sampah);
        calendarView.addEvent(event49);
        event50 = new Event(Color.BLACK, 1590987600000L, sampah);
        calendarView.addEvent(event50);
        event51 = new Event(Color.BLACK, 1591246800000L, sampah);
        calendarView.addEvent(event51);
        event52 = new Event(Color.BLACK, 1591506000000L, sampah);
        calendarView.addEvent(event52);
        event53 = new Event(Color.BLACK, 1591765200000L, sampah);
        calendarView.addEvent(event53);
        event54 = new Event(Color.BLACK, 1592024400000L, sampah);
        calendarView.addEvent(event54);
        event55 = new Event(Color.BLACK, 1592283600000L, sampah);
        calendarView.addEvent(event55);
        event56 = new Event(Color.BLACK, 1592542800000L, sampah);
        calendarView.addEvent(event56);
        event57 = new Event(Color.BLACK, 1592802000000L, sampah);
        calendarView.addEvent(event57);
        event58 = new Event(Color.BLACK, 1593061200000L, sampah);
        calendarView.addEvent(event58);
        event59 = new Event(Color.BLACK, 1593320400000L, sampah);
        calendarView.addEvent(event59);
        event60 = new Event(Color.BLACK, 1593579600000L, sampah);
        calendarView.addEvent(event60);

    }

    private void uploadOrder(final String tipe) {

        progressDialog.setMessage("Proses Order...");
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        Query query = myRef.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        buildAlertPesanGPSnya();
                    }else if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        getLocation();
                    }

                    String Latitude = String.valueOf(mLatitude);
                    String Longitude = String.valueOf(mLongitude);
                    String uid = user.getUid();
                    String timeStamp = String.valueOf(System.currentTimeMillis());
                    String email = ""+ds.child("email").getValue();
                    String nama = ""+ds.child("nama").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    String image = ""+ds.child("image").getValue();

                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("nama", nama);
                    hashMap.put("phone",phone);
                    hashMap.put("profile",image);
                    hashMap.put("timeStamp", timeStamp);
                    hashMap.put("linkMaps", Latitude+", "+Longitude);
                    hashMap.put("TypeOrder", tipe);
                    hashMap.put("sudahBelum", "0");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("Order");

                    databaseReference.child(timeStamp).setValue(hashMap);

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Order sukses", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void buildAlertPesanGPSnya() {

        AlertDialog.Builder builderGPS = new AlertDialog.Builder(getActivity());
        builderGPS.setMessage("Tolong Aktifkan GPS Kamu")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderGPS.create().show();

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        }else {

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
            }

        }

    }

}
