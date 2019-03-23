package com.example.carstalkeradmin;

import android.app.Dialog;
import android.icu.util.ULocale;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    ListView listView;

    String user;

    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        spinner = findViewById(R.id.progressBar);

        spinner.setVisibility(View.VISIBLE);

        listView = findViewById(R.id.listView);

        final ArrayList<String> arrayList = new ArrayList<>();


        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    //take all the usernames
                    for (DataSnapshot i : dataSnapshot.getChildren()){
                        //check if allready exists because if a user signs up while in this it will break
                        if(!arrayList.contains(i.getKey())){
                            arrayList.add(i.getKey());
                        }
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);

                    listView.setAdapter(arrayAdapter);
                    spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                user = arrayList.get(position);
                setContentView(R.layout.activity_users_chosen);

                final ProgressBar secondSpinner = findViewById(R.id.chosenProgressBar);

                secondSpinner.setVisibility(View.VISIBLE);

                final ListView listViewChosen = findViewById(R.id.chosenListView);

                Toast.makeText(getApplicationContext(),user,Toast.LENGTH_SHORT).show();

                final ArrayList<String> chosenArrayList = new ArrayList<>();

                mDatabase.child("users").child(user).child("events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot i : dataSnapshot.getChildren()){
                                if(!chosenArrayList.contains(i.getKey())){
                                    chosenArrayList.add(i.getKey());
                                }
                            }


                        }else{
                            Toast.makeText(getApplicationContext(),"no entries",Toast.LENGTH_SHORT).show();
                        }
                        ArrayAdapter secondArrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,chosenArrayList);
                        listViewChosen.setAdapter(secondArrayAdapter);

                        secondSpinner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                listViewChosen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(),"yoyoyo",Toast.LENGTH_SHORT).show();
                        // TODO: 23/3/2019  
                    }
                });

            }
        });


    }

}
