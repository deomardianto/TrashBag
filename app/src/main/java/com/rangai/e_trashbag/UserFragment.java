package com.rangai.e_trashbag;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    StorageReference storageReference;

    String storagePath = "Users_Profile_Cover_Imgs/";

    ImageView mfotoProfil, mcover;
    TextView mnama, memail, mphone;
    FloatingActionButton fab;

    EditText editText;

    ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;
    String profileOrCoverPhoto;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");
        storageReference = getInstance().getReference();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mfotoProfil = view.findViewById(R.id.fotoProfilID);
        mnama = view.findViewById(R.id.nama_profilID);
        memail = view.findViewById(R.id.email_profilID);
        mphone = view.findViewById(R.id.phone_profilID);
        mcover = view.findViewById(R.id.coverID);
        fab = view.findViewById(R.id.fab);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String nama = ""+ds.child("nama").getValue();
                    String email = ""+ds.child("email").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    String image = ""+ds.child("image").getValue();
                    String cover = ""+ds.child("cover").getValue();

                    mnama.setText(nama);
                    memail.setText(email);
                    mphone.setText(phone);
                    try {
                        Picasso.get().load(image).into(mfotoProfil);
                    }
                    catch (Exception e){
                    }
                    try {
                        Picasso.get().load(cover).into(mcover);
                    }
                    catch (Exception e){
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        return view;
    }

    private boolean checkStoragePermission(){


        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return  result;
    }
    private void  requestStoragePermission(){
        requestPermissions(storagePermission,STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermission(){

        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);


        return  result && result1;
    }
    private void  requestCameraPermission(){
        requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
    }



    private void showEditProfileDialog() {

        String option[] = {"Edit Profile","Edit Cover Photo","Edit Nama","Edit Phone Number"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Mau yang edit yang mana");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0){

                    progressDialog.setMessage("Updating Profile Picture");
                    profileOrCoverPhoto = "image";
                    showImagePicDialog();

                }else  if (which == 1){

                    progressDialog.setMessage("Updating Cover Picture");
                    profileOrCoverPhoto = "cover";
                    showImagePicDialog();

                }else if (which == 2){

                    progressDialog.setMessage("Updating Nama");
                    showNamePhoneNumberDialog("nama");

                }else if (which == 3){

                    progressDialog.setMessage("Updating Phone Number");
                    showNamePhoneNumberDialog("phone");

                }

            }
        });

        builder.create().show();

    }

    private void showNamePhoneNumberDialog(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+key);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        editText = new EditText(getActivity());
        editText.setHint("Enter "+key);

        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String value = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(value)){

                    progressDialog.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),"Updated...",Toast.LENGTH_LONG).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            });

                    if (key.equals("nama")){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
                        Query query = reference.orderByChild("uid").equalTo(user.getUid());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    String child = ds.getKey();
                                    dataSnapshot.getRef().child(child).child("nama").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (key.equals("phone")){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
                        Query query = reference.orderByChild("uid").equalTo(user.getUid());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    String child = ds.getKey();
                                    dataSnapshot.getRef().child(child).child("phone").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else {

                    Toast.makeText(getActivity(),"Please Enter "+ key,Toast.LENGTH_LONG).show();

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.create().show();

    }

    private void showImagePicDialog() {

        String option[] = {"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Pick Image From");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0){

                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }

                }else  if (which == 1){

                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }

                }

            }
        });

        builder.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case CAMERA_REQUEST_CODE:{

                if (grantResults.length > 0){
                    boolean cameraAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please enable to camera & storage permission", Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{

                if (grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please enable to storage permission", Toast.LENGTH_LONG).show();
                    }
                }

            }
            break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){

                image_uri = data.getData();

                uploadProfileCoverPhoto(image_uri);

            }else {

                uploadProfileCoverPhoto(image_uri);

            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(final Uri uri) {

        progressDialog.show();

        String filePathAndName = storagePath + "" + profileOrCoverPhoto + "_"+user.getUid();

        StorageReference storageReference2 = storageReference.child(filePathAndName);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        final Uri downloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()){
                            HashMap<String, Object> result = new HashMap<>();
                            result.put(profileOrCoverPhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(result)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),"Image Update...", Toast.LENGTH_LONG).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),"Error Updating Image...", Toast.LENGTH_LONG).show();

                                        }
                                    });

                            if (profileOrCoverPhoto.equals("image")){
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
                                Query query = reference.orderByChild("uid").equalTo(user.getUid());
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            String child = ds.getKey();
                                            dataSnapshot.getRef().child(child).child("image").setValue(downloadUri.toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }


                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Some Error Occured", Toast.LENGTH_LONG).show();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void pickFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }
}
