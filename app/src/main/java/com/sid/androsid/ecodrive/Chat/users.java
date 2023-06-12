package com.sid.androsid.ecodrive.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.sid.androsid.ecodrive.BottomNavigation.BottomNavigationViewHelper;
import com.sid.androsid.ecodrive.Directions.MapsActivity1;
import com.sid.androsid.ecodrive.R;
//import com.sid.androsid.ecodrive.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by sid on 2019-02-25.
 */

public class users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        Firebase.setAndroidContext(this);
        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sihsigma.firebaseio.com/users/");

        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,databaseReference) {

            @Override
            protected void populateView(View v, String model, int position) {
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setText(model);
                al.add(model);
            }
        };

        usersList.setAdapter(firebaseListAdapter);


        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                UserDetails.username = email.substring(0, email.indexOf('@'));
                startActivity(new Intent(users.this, Chats.class));
            }
        });
    }
}


















