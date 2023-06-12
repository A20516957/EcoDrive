package com.sid.androsid.ecodrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Logout extends AppCompatActivity {
Button logout1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_logout);
        logout1 = findViewById(R.id.logoutb);

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                finish();
                Intent intent = new Intent(Logout.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}
