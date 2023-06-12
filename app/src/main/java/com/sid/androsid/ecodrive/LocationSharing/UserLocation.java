package com.sid.androsid.ecodrive.LocationSharing;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sid on 2019-02-28.
 */

public class UserLocation {
    private DatabaseReference mDatabaseReference;

   public void savePLocation(String username , LatLng source ,LatLng destination){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Plocation").child(username);
        mDatabaseReference.child("source").setValue(source);
        mDatabaseReference.child("destination").setValue(destination);
    }

    public void saveRLocation(String username , LatLng source ,LatLng destination){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Rlocation").child(username);
        mDatabaseReference.child("source").setValue(source);
        mDatabaseReference.child("destination").setValue(destination);
    }
}
