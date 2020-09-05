package com.rangai.e_trashbag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.MyHolder> {

    Context context;
    List<dataAdmin> histori;

    public AdapterAdmin(Context context, List<dataAdmin> histori) {
        this.context = context;
        this.histori = histori;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_admin, parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,int position) {

        String email = histori.get(position).getNama();
        String phone = histori.get(position).getPhone();
        final String type = histori.get(position).getTypeOrder();
        final String tanggal = histori.get(position).getTimeStamp();
        String profile = histori.get(position).getProfile();
        final String mlink = histori.get(position).getLinkMaps();
        String sudah = histori.get(position).getSudahBelum();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(tanggal));
        String waktu = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.mEmail.setText(email);
        holder.mPhone.setText(phone);
        holder.mType.setText(type);
        holder.mWaktu.setText(waktu);

        if (sudah.equals("0")){
            try {
                Picasso.get().load(profile)
                        .placeholder(R.drawable.icon_belum)
                        .into(holder.mProfile);
            }catch (Exception e){
                Picasso.get().load(R.drawable.icon_belum).placeholder(R.drawable.icon_belum).into(holder.mProfile);
            }
        }else {
            try {
                Picasso.get().load(R.drawable.icon_belum)
                        .placeholder(R.drawable.icon_sudah)
                        .into(holder.mProfile);
            }
            catch (Exception e){
                Picasso.get().load(R.drawable.icon_belum).placeholder(R.drawable.icon_belum).into(holder.mProfile);
            }
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option[] = {"Detail Orderan","Ambil Orderan"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Choose Action");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0){

                            Intent intent = new Intent(context,detailOrderan.class);
                            intent.putExtra("timeStamp",tanggal);
                            context.startActivity(intent);


                        }else  if (which == 1){

                            Uri link = Uri.parse("google.navigation:q="+mlink);
                            Intent bukaMaps = new Intent(Intent.ACTION_VIEW, link);
                            bukaMaps.setPackage("com.google.android.apps.maps");
                            context.startActivity(bukaMaps);

                            if (type.equals("Order Sekarang")){

                                HashMap<String, Object> result = new HashMap<>();
                                result.put("sudahBelum", "1");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Order");
                                reference.child(tanggal).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context,"Orderan Terambil",Toast.LENGTH_LONG).show();
                                            }
                                        }).
                                        addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }else {

                                HashMap<String, Object> result = new HashMap<>();
                                result.put("sudahBelum", "0");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Order");
                                reference.child(tanggal).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context,"Orderan Belum Selesai",Toast.LENGTH_LONG).show();
                                            }
                                        }).
                                        addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }


                        }

                    }
                });

                builder.create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return histori.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mProfile;
        TextView mEmail, mPhone, mType, mWaktu;
        public LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mEmail = itemView.findViewById(R.id.admin_nama);
            mPhone = itemView.findViewById(R.id.admin_phone);
            mType= itemView.findViewById(R.id.admin_Type);
            mWaktu = itemView.findViewById(R.id.admin_waktu);
            mProfile = itemView.findViewById(R.id.admin_profile);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.admin_btn);

        }
    }
}
