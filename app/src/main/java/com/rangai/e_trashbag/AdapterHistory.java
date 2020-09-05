package com.rangai.e_trashbag;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.Myholder> {

    Context context;
    List<dataHistory> histories;

    public AdapterHistory(Context context, List<dataHistory> histories) {
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_history, parent,false);

        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholder holder, int position) {

        String nama = histories.get(position).getNama();
        String type = histories.get(position).getTypeOrder();
        final String tanggal = histories.get(position).getTimeStamp();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(tanggal));
        String waktu = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.mNama.setText(nama);
        holder.mType.setText(type);
        holder.mTimeStamp.setText(waktu);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detailOrderan.class);
                intent.putExtra("timeStamp",tanggal);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class Myholder extends RecyclerView.ViewHolder{

        TextView mType, mTimeStamp;
        TextView mNama;
        LinearLayout linearLayout;


        public Myholder(@NonNull View itemView) {
            super(itemView);

            mNama = itemView.findViewById(R.id.namaHistory);
            mType = itemView.findViewById(R.id.typeHistory);
            mTimeStamp = itemView.findViewById(R.id.tanggalHistory);
            linearLayout =(LinearLayout) itemView.findViewById(R.id.linearHistory);
        }
    }
}
