package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingLayout extends AppCompatActivity {

    private static final int PICK_IMAGE=1;
    private EditText status;
    private Button update;
    private EditText name;
    private CircleImageView image;
    private ProgressDialog dialog;
    private Uri imageUri,resultUri;
    private StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("images");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
        status=(EditText)findViewById(R.id.register_status);
        name=(EditText)findViewById(R.id.register_name);
//        password=(EditText)findViewById(R.id.register_possword);
        update=(Button)findViewById(R.id.update_btn);
        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
//        register=(Button)findViewById(R.id.register_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Profile");
        image=(CircleImageView) findViewById(R.id.register_image);
        imageUri=null;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        dialog.setMessage("Getting Your details");
        dialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String str = dataSnapshot.child("image").getValue().toString();
                String nm = dataSnapshot.child("name").getValue().toString();
                String sta = dataSnapshot.child("status").getValue().toString();
                if(!str.equals("default")){
                    Picasso.get().load(str).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(str).placeholder(R.drawable.profile_image).into(image);
                        }
                    });

                }

                name.setText(nm);
                status.setText(sta);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingLayout.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
//        firestore.collection("Users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                final String str = documentSnapshot.get("image").toString();
//                String nm = documentSnapshot.get("name").toString();
////                String sta = documentSnapshot.get("status").toString();
//                if(!str.equals("default")){
//                    Picasso.get().load(str).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(image, new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            Picasso.get().load(str).placeholder(R.drawable.profile_image).into(image);
//                        }
//                    });
//
//                }
//
//                name.setText(nm);
//                status.setText("Hey there i am using Annonymous app");
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(SettingLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingLayout.this);*/

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String username=name.getText().toString();
                final String sta=status.getText().toString();
                if(imageUri!=null){

                    if(!TextUtils.isEmpty(username) || !TextUtils.isEmpty(sta)){
//                        progressBar.setVisibility(View.VISIBLE);
                        dialog.setTitle("Updating");
                        dialog.setMessage("Please wait. Updating your profile.");
                        dialog.show();
                        File thumb_file = new File(resultUri.getPath());
                        final FirebaseUser user = mAuth.getCurrentUser();
                        Bitmap image_bitmap = null;
                        try {
                            image_bitmap = new Compressor(SettingLayout.this)
                                    .setMaxWidth(200)
                                    .setMaxHeight(200)
                                    .setQuality(75)
                                    .compressToBitmap(thumb_file);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            final byte[] thumb_byte = baos.toByteArray();
                            final StorageReference thumbStorage = mStorage.child("thumb").child(user+".jpg");


                            final StorageReference user_profile=mStorage.child(user+".jpg");
                            user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            UploadTask uploadTask = thumbStorage.putBytes(thumb_byte);
                                            final Uri x = uri;

                                            //==================================================================================================

                                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                @Override
                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                    if (!task.isSuccessful()) {
                                                        throw task.getException();
                                                    }

                                                    // Continue with the task to get the download URL
                                                    return thumbStorage.getDownloadUrl();
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        Uri downloadUri = task.getResult();
                                                        String thumb_download = downloadUri.toString();
                                                        final String image_download=x.toString();
                                                        String token_id= FirebaseInstanceId.getInstance().getToken();
                                                        Map<String,Object> data=new HashMap<>();
                                                        data.put("name",username);
                                                        data.put("status",sta);
                                                        data.put("image",image_download);
                                                        data.put("thumb_image",thumb_download);
                                                        databaseReference.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(SettingLayout.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                                                                }else{
                                                                    Toast.makeText(SettingLayout.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                                dialog.dismiss();
                                                            }
                                                        });
//                                                        firestore.collection("Users").document(mAuth.getUid()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
////                                                                                                                    progressBar.setVisibility(View.INVISIBLE);
//                                                                dialog.dismiss();
//                                                                Toast.makeText(SettingLayout.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
//
//                                                            }
//                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                Toast.makeText(SettingLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                dialog.dismiss();
//                                                            }
//                                                        });

                                                    } else {
                                                        // Handle failures
                                                        // ...
                                                    }
                                                }
                                            });

                                            //==================================================================================================
                                        /*uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    String thumb_download = task.getResult().toString();
//                                                    Task<Uri> thumb_do = thumbStorage.getDownloadUrl();
                                                    final String image_download=x.toString();
                                                    String token_id= FirebaseInstanceId.getInstance().getToken();
                                                    Map<String,Object> data=new HashMap<>();
                                                    data.put("name",username);
                                                    data.put("status",sta);
                                                    data.put("image",image_download);
                                                    firestore.collection("Users").document(mAuth.getUid()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                            dialog.dismiss();
                                                            Toast.makeText(SettingLayout.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SettingLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        });*/

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SettingLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingLayout.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            Toast.makeText(SettingLayout.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(SettingLayout.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Map<String,Object> data=new HashMap<>();
                    data.put("name",username);
                    data.put("status",sta);
                    firestore.collection("Users").document(mAuth.getUid()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                                                                                                                    progressBar.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                            Toast.makeText(SettingLayout.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingLayout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageUri=data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                image.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
