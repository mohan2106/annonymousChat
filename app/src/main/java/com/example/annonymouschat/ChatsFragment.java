package com.example.annonymouschat;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private YourThreadAdapter adapter;
    private ArrayList<YourThredClass> itemlist;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = v.findViewById(R.id.threads_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemlist = new ArrayList<>();
        itemlist.add(new YourThredClass("mohan","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        itemlist.add(new YourThredClass("nikhil","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        itemlist.add(new YourThredClass("aman","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        itemlist.add(new YourThredClass("sohan","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        itemlist.add(new YourThredClass("sudarshan","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        itemlist.add(new YourThredClass("ram","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        itemlist.add(new YourThredClass("shyam","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","10","26"));
        adapter = new YourThreadAdapter(itemlist,getContext());
        recyclerView.setAdapter(adapter);
        return v;
    }

}
