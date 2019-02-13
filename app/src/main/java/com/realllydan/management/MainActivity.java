package com.realllydan.management;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //ui components
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    //vars
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        mFab = findViewById(R.id.mFab);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        navigationView = findViewById(R.id.mNavView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user_email = getUserEmail();

        setupToolbar();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_test:
                                Log.d(TAG, "onNavigationItemSelected: nav_test clicked");
                                break;
                        }

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: mFab clicked");

                startActivity(new Intent(MainActivity.this,AddItemActivity.class) );
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");

        if (mAuth.getCurrentUser() == null) {
            sendBack();
        }
    }

    private void sendBack() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar: setting up");

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
    }

    private void setupNavigationHeader() {
        View header = navigationView.getHeaderView(0);
        final TextView nav_username = header.findViewById(R.id.nav_username);
        final TextView nav_useremail = header.findViewById(R.id.nav_useremail);
        final TextView nav_header_version = header.findViewById(R.id.nav_header_version);
    }

    private String getUserEmail() {
        Log.d(TAG, "getUserEmail: getting user email");

        String user_email;
        if (mAuth.getCurrentUser() != null) {
            user_email = mAuth.getCurrentUser().getEmail();
            return user_email;
        }
        return null;
    }
}
