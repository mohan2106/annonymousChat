package com.example.annonymouschat;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationLog extends AppCompatActivity {
    private RecyclerView recyclerView;
    private notiAdapter adapter;
    private ArrayList<notiClass> itemList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_log);

        recyclerView = findViewById(R.id.notiLogRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
//        itemList.add(new notiClass("kjhdfj","title","body of the notification"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notification"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificaefbhtion"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notifjfhication"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notification"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of the notificationdkjsb"));
//        itemList.add(new notiClass("kjhdfj","title","body of thdjhge notification"));
//        itemList.add(new notiClass("kjhdfj","title","body of the djbjhdsnotification"));
//        itemList.add(new notiClass("kjhdfj","title","body ofkjdfjk the notification"));
//        itemList.add(new notiClass("kjhdfj","title","body of the dkffjhkjhnotification"));
        adapter = new notiAdapter(itemList,this);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("notificationLog").document("id").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String x= documentSnapshot.getString("id");
                final int t = Integer.valueOf(x);
                firebaseFirestore.collection("notificationLog").document("admin").collection("notifications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                String able=documentSnapshot.get("id").toString();
                                int y = Integer.valueOf(able);
                                if((t-y)<100){
                                    itemList.add(new notiClass("kjhdsjkh",documentSnapshot.get("title").toString(),documentSnapshot.get("body").toString()));
                                }

                            }

                            adapter.notifyDataSetChanged();
                        }
                        else{
                            String error=task.getException().getMessage();
                            Toast.makeText(NotificationLog.this, error, Toast.LENGTH_SHORT).show();
                        }
//                bar.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NotificationLog.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });





    }
}
