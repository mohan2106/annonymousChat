package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ViewPager pager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference quickMessage;
    private DatabaseReference openChat;
    private TabLayout tabLayout;
    private ProgressDialog dialog;
    private FloatingActionButton fab1,fab2,fab3;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen=false;
    public static final String CHANNEL_ID = "mohan";
    private static final String CHANNEL_NAME = "mohan";
    private static final String CHANNEL_DESC = "mohan notification";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(mAuth.getUid() == null){
            finish();
            startActivity(new Intent(MainActivity.this,startActivity.class));

        }else{
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
            databaseReference.child("online").setValue("true");
        }

//        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
//        final String org = sp.getString("org","NULL");
////        final String userid = sp.getString("id",mAuth.getUid());
//
//
////            Toast.makeText(this, "Shared Preference is empty", Toast.LENGTH_SHORT).show();
//            databaseReference2.child("User").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    SharedPreferences sp2 = getSharedPreferences("User", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp2.edit();
//                    editor.putString("name", dataSnapshot.child("name").getValue().toString());
//                    editor.putString("email", dataSnapshot.child("email").getValue().toString());
//                    editor.putString("id", mAuth.getUid());
//                    editor.putInt("image", Integer.parseInt(dataSnapshot.child("intImage").getValue().toString()));
//                    editor.putString("org", dataSnapshot.child("organisation").getValue().toString());
//                    editor.putBoolean("iscouncellor", Boolean.parseBoolean(dataSnapshot.child("isCouncellor").getValue().toString()));
//                    editor.apply();
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });


//        updateUI(currentUser);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(mAuth.getUid() != null){
//            databaseReference.child("online").setValue("false");
//            databaseReference.child("lastSeen").setValue(ServerValue.TIMESTAMP);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        databaseReference.child("online").setValue("true");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        databaseReference.child("online").setValue("false");
//        databaseReference.child("lastSeen").setValue(ServerValue.TIMESTAMP);
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Baatein");
        fab1 = findViewById(R.id.fab_1);
//        fab2 = findViewById(R.id.fab_2);
//        fab3 = findViewById(R.id.fab3);

        //================================testins shared Preference
        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        final String org = sp.getString("name","NULL");


        //========================================================

        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        quickMessage = FirebaseDatabase.getInstance().getReference().child("quickMessage");
        openChat = FirebaseDatabase.getInstance().getReference().child("openChat");
        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_anim);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                animate();
                startActivity(new Intent(MainActivity.this,writeThread.class));
//                startActivity(new Intent(MainActivity.this,profileImages.class));

            }
        });
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                animate();
////                showAlertDialogButtonClicked(1);
//                startActivity(new Intent(MainActivity.this,writeThread.class));
////                Toast.makeText(MainActivity.this, "Only councellors", Toast.LENGTH_SHORT).show();
//            }
//        });
//        fab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                animate();
////                showAlertDialogButtonClicked(2);
//                startActivity(new Intent(MainActivity.this,writeThread.class));
////                Toast.makeText(MainActivity.this, "All Mentors", Toast.LENGTH_SHORT).show();
//            }
//        });

        tabLayout = findViewById(R.id.main_tab);
        pager = findViewById(R.id.main_viewPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mSectionPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.main_logout_btn){
            databaseReference.child("online").setValue("false");
            databaseReference.child("lastSeen").setValue(ServerValue.TIMESTAMP);
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,startActivity.class));
            finish();
        }else if(id == R.id.main_setting){
//            startActivity(new Intent(MainActivity.this,SettingLayout.class));
            Toast.makeText(this, "Setting page is not working at this time", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    private void animate(){
        if(isOpen){
            fab1.startAnimation(rotateBackward);
            fab2.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isOpen = false;
        }else{
            fab1.startAnimation(rotateForward);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isOpen = true;
        }
    }

    public void showAlertDialogButtonClicked(int x) {

        final int y = x;
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.custom_layout,null);

        Button send = v.findViewById(R.id.shareBtn);
        Button can = v.findViewById(R.id.cancel_button);

        if(x==1){
            send.setText("Visible only to Cuouncellers");
        }
        final EditText message = v.findViewById(R.id.problemEditText);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(v);

        final AlertDialog alertDialog = builder.create();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = message.getText().toString();
                alertDialog.cancel();

                    shareMessageToMentors(str,y);

            }
        });

        // create an alert builder


        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    private void shareMessageToMentors(String str,int x){
        dialog.setTitle("Sending");
        dialog.setMessage("Sending your request to our councellors, Don't be panic...");
        dialog.show();
        DatabaseReference messagepush = quickMessage.child("mentor").push();
        String pushedKey = messagepush.getKey();

        Map data = new HashMap();
        data.put("message",str);
        data.put("from",mAuth.getUid());
        data.put("time",ServerValue.TIMESTAMP);
        data.put("receivedBy","false");

        Map toMentor = new HashMap();
        toMentor.put("mentor/"+pushedKey,data);

        Map toCouncellor = new HashMap();
        toCouncellor.put("councellor/"+pushedKey,data);

        Map opendata = new HashMap();
        opendata.put("message",str);
        opendata.put("time",ServerValue.TIMESTAMP);
        opendata.put("receivedBy","false");

        Map ok = new HashMap();
        ok.put(mAuth.getUid()+"/"+pushedKey,opendata);

        if(x==2){
            quickMessage.updateChildren(toMentor, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null){
                        Log.d("CHAT_LOG",databaseError.getMessage().toString());
                    }
                }
            });
        }

        quickMessage.updateChildren(toCouncellor, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError!=null){
                    Log.d("CHAT_LOG",databaseError.getMessage().toString());
                }
            }
        });

        openChat.updateChildren(ok, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError!=null){
                    Toast.makeText(MainActivity.this, "ERROR: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "Successfuly sent the request", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

}
