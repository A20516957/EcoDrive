package com.sid.androsid.ecodrive.Points;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sid.androsid.ecodrive.R;

import java.util.ArrayList;

/**
 * Created by sid on 2019-03-02.
 */

public class AllUserPoints extends AppCompatActivity {

    private DatabaseReference mDatabaseReference,mDatabaseReference1;

    ArrayList<String> mArrayList = new ArrayList<>();
    ArrayAdapter<String> mArrayAdapter ;

    ListView mListView ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);
        mListView =findViewById(R.id.listview);
        retrievePoints();
    }

    public void retrievePoints(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sihsigma.firebaseio.com/UserPoints/");
       // mDatabaseReference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sihsigma.firebaseio.com/UserPoints/");

    mDatabaseReference.orderByValue().addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot Points : dataSnapshot.getChildren()){
                String s = Points.getValue(String.class);
                s = Points.getKey() +" "+s;
                mArrayList.add(s);
            }
          mArrayAdapter = new ArrayAdapter<String>(AllUserPoints.this,android.R.layout.simple_list_item_1,mArrayList);
            mListView.setAdapter(mArrayAdapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });




    }

    public void savePoints(String username , String Points){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("UserPoints").child(username);
        mDatabaseReference.setValue(Points);
    }
}
