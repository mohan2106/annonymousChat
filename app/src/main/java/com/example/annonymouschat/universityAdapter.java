package com.example.annonymouschat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class universityAdapter extends RecyclerView.Adapter<universityAdapter.ViewHolder> {

    private Context context;
    private ArrayList<universityClass> itemList;
    private String name,email,pass;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Integer image;

    public universityAdapter(Context context, ArrayList<universityClass> itemList, String name, String email, String pass, ProgressDialog dialog, Integer image) {
        this.context = context;
        this.itemList = itemList;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.dialog = dialog;
        this.image = image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_university_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        universityClass uc = itemList.get(position);
        holder.name.setText(uc.getName());
        holder.members.setText("Total Members = "+String.valueOf(uc.getMember()));
        final String id = uc.getId();
        final Integer members = uc.getMember();
        final Integer img = image;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);

                final View dialogView = LayoutInflater.from(context).inflate(R.layout.select_user_councellor,null);
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setView(dialogView);

                dialogView.findViewById(R.id.councellor).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        signup(email,name,members,image,id,true);
                    }
                });

                dialogView.findViewById(R.id.normalUser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        signup(email,name,members,image,id,false);
                    }
                });

                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView members;
        private Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.university_single_name);
            members = itemView.findViewById(R.id.university_single_member);
            button = itemView.findViewById(R.id.single_university_btn);
        }
    }

    void signup(final String email,final String name,final Integer members,final Integer image,final String id,final boolean status){
        dialog.setTitle("SignUp");
        dialog.setMessage("Signing you up... ");
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String token_id= FirebaseInstanceId.getInstance().getToken();
                            Map<String,Object> data=new HashMap<>();
                            data.put("name",name);
                            data.put("status","Hey there i am using Annonymous app");
                            data.put("token",token_id);
                            data.put("id",mAuth.getUid());
                            data.put("email",email);
                            data.put("image","");
                            data.put("thumb_image","");
                            data.put("intImage",image);
                            data.put("organisation",id);
                            data.put("isCouncellor",status);
                            databaseReference.child("User").child(mAuth.getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        dialog.setMessage("Adding you in your organisation");
                                        Map<String,Object> data2 = new HashMap<>();
                                        data2.put("members",members +1);
                                        databaseReference.child("organisations").child(id).updateChildren(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Map<String,Object> mp = new HashMap<>();
                                                    mp.put("id",mAuth.getUid());
                                                    DatabaseReference messpush = databaseReference.child(id).child("User").push();
                                                    String st = messpush.getKey();
                                                    if(status){
                                                        databaseReference.child(id).child("councellors").child(st).setValue(mp);
                                                    }
                                                    databaseReference.child(id).child("User").child(st).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                SharedPreferences sharedPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                editor.putString("name",name);
                                                                editor.putString("email",email);
                                                                editor.putString("id",mAuth.getUid());
                                                                editor.putInt("image",image);
                                                                editor.putString("org",id);
                                                                editor.putBoolean("iscouncellor",false);
                                                                editor.commit();
                                                                Toast.makeText(context, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(context,MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                UniversityActivity.activity.finish();
                                                                registerActivity.rA.finish();
                                                            }else{
                                                                Toast.makeText(context, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }else{
                                                    Toast.makeText(context, "ERROR: "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                                    }else{
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                        else {
//                                            Snackbar.make(v,task.getException().toString(),Snackbar.LENGTH_LONG).show();
//                                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }
}
