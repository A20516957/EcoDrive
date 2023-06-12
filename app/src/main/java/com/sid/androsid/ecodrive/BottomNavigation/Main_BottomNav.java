package com.sid.androsid.ecodrive.BottomNavigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.sid.androsid.ecodrive.Chat.users;
import com.sid.androsid.ecodrive.Directions.MapsActivity1;
import com.sid.androsid.ecodrive.Logout;
import com.sid.androsid.ecodrive.PieChart1;
import com.sid.androsid.ecodrive.Points.AllUserPoints;
import com.sid.androsid.ecodrive.R;


/**
 * Created by sid on 2019-02-27.
 */

public class Main_BottomNav extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);



        final OverviewFragment overviewFragment1 = new OverviewFragment();
        final AccountFragment accountFragment1 = new AccountFragment();
        final EcoFragment ecoFragment1 = new EcoFragment();
        final ChatFragment chatFragment1 = new ChatFragment();

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


               switch (menuItem.getItemId()){
                   case R.id.account:
                       Intent intent = new Intent(Main_BottomNav.this, Logout.class);
                       startActivity(intent);
                           break;
                   case R.id.ecodrive:
                       Intent intent2 = new Intent(Main_BottomNav.this,MapsActivity1.class);
                       startActivity(intent2);
                       break;
                   case R.id.overview:
                       Intent intent1 = new Intent(Main_BottomNav.this, PieChart1.class);
                       startActivity(intent1);
                       break;
                   case R.id.chat:
                       Intent intent3 = new Intent(Main_BottomNav.this,users.class);
                       startActivity(intent3);
                       break;
               }
                return false;
            }
        });

    }


//    private void setfragment(Fragment fragment)
//    {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame,fragment);
//        fragmentTransaction.commit();
//    }

}
