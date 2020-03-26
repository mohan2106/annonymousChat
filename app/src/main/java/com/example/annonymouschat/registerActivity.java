package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class registerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE=1;
    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private EditText name;
    private CircleImageView image;
    private ProgressDialog dialog;
    private Uri imageUri,resultUri;
    private StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("images");
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    public static Activity rA;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rA = this;
        Intent in = getIntent();
        Integer profImg = R.drawable.c1;
        profImg = in.getIntExtra("profImg",R.drawable.c1);
        email=(EditText)findViewById(R.id.register_status);
        name=(EditText)findViewById(R.id.register_name);
        password=(EditText)findViewById(R.id.register_possword);
        login=(Button)findViewById(R.id.back_to_login);
        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        register=(Button)findViewById(R.id.register_button);
        image=(CircleImageView) findViewById(R.id.register_image);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        imageUri=null;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });*/
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerActivity.this,profileImages.class));
            }
        });
        final Integer profImg2 = profImg;
        image.setImageResource(profImg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //============================================================================Sending to select university page first

                final String username=name.getText().toString();
                final String userEmail=email.getText().toString();
                String usserPassword=password.getText().toString();

                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(usserPassword)){
                    Intent intent = new Intent(registerActivity.this,UniversityActivity.class);
                    intent.putExtra("image",profImg2);
                    intent.putExtra("email",userEmail);
                    intent.putExtra("name",username);
                    intent.putExtra("password",usserPassword);
                    startActivity(intent);
                }else{
                    Toast.makeText(registerActivity.this, "Please Provide Valid Information", Toast.LENGTH_SHORT).show();
                }



                //========================================================================

                /*if(imageUri!=null){
                    final String username=name.getText().toString();
                    final String userEmail=email.getText().toString();
                    String usserPassword=password.getText().toString();
                    if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(usserPassword)){
//                        progressBar.setVisibility(View.VISIBLE);
                        dialog.setTitle("SignUp");
                        dialog.setMessage("Signing you up... ");
                        dialog.show();
                        mAuth.createUserWithEmailAndPassword(userEmail, usserPassword)
                                .addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            dialog.setTitle("Signed Up");
                                            dialog.setMessage("Uploading your details...");
                                            File thumb_file = new File(resultUri.getPath());
                                            final FirebaseUser user = mAuth.getCurrentUser();
                                            Bitmap image_bitmap = null;
                                            try {
                                                image_bitmap = new Compressor(registerActivity.this)
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
                                                                            data.put("status","Hey there i am using Annonymous app");
                                                                            data.put("token",token_id);
                                                                            data.put("id",mAuth.getUid());
                                                                            data.put("email",userEmail);
                                                                            data.put("image",image_download);
                                                                            data.put("thumb_image",thumb_download);
                                                                            databaseReference.child(mAuth.getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        dialog.dismiss();
//
                                                                                         Toast.makeText(registerActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                                                         sendToMAin();
                                                                                    }else{
                                                                                        Toast.makeText(registerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                }
                                                                            });

                                                                        } else {
                                                                            // Handle failures
                                                                            // ...
                                                                        }
                                                                    }
                                                                });

                                                                //==================================================================================================

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(registerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(registerActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                dialog.dismiss();
                                                Toast.makeText(registerActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
//                                            Snackbar.make(v,task.getException().toString(),Snackbar.LENGTH_LONG).show();
//                                            progressBar.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(registerActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
//                    Toast.makeText(registerActivity.this, "Please select profile image", Toast.LENGTH_SHORT).show();
                    final String username=name.getText().toString();
                    final String userEmail=email.getText().toString();
                    String usserPassword=password.getText().toString();
                    if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(usserPassword)){
//                        progressBar.setVisibility(View.VISIBLE);
                        dialog.setTitle("SignUp");
                        dialog.setMessage("Signing you up... ");
                        dialog.show();
                        mAuth.createUserWithEmailAndPassword(userEmail, usserPassword)
                                .addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String token_id= FirebaseInstanceId.getInstance().getToken();
                                            Map<String,Object> data=new HashMap<>();
                                            data.put("name",username);
                                            data.put("status","Hey there i am using Annonymous app");
                                            data.put("token",token_id);
                                            data.put("id",mAuth.getUid());
                                            data.put("email",userEmail);
                                            data.put("image","");
                                            data.put("thumb_image","");
                                            data.put("intImage",profImg2);
                                            databaseReference.child(mAuth.getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        dialog.dismiss();
//
                                                        Toast.makeText(registerActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                        sendToMAin();
                                                    }else{
                                                        Toast.makeText(registerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                        else {
//                                            Snackbar.make(v,task.getException().toString(),Snackbar.LENGTH_LONG).show();
//                                            progressBar.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(registerActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                    }
                }*/

            }
        });

    }
    private void sendToMAin(){
        Intent intent=new Intent(registerActivity.this,MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
        else{
            startActivity(intent);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
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
