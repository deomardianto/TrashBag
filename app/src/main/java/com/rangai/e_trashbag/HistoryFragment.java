package com.rangai.e_trashbag;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    List<dataHistory> list;
    AdapterHistory adapter;


    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private DatabaseReference reference;

    private FirebaseAuth mAuth;
    FirebaseUser user;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mRecycler = view.findViewById(R.id.history_rcyclerView);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        reference = FirebaseDatabase.getInstance().getReference().child("Order");
        Query query = reference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    dataHistory data = ds.getValue(dataHistory.class);
                    list.add(data);

                }
                adapter = new AdapterHistory(getActivity(),list);
                mRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }
}
