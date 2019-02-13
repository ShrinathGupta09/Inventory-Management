package com.realllydan.management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MerchandiseAdapter.OnMerchClickListener {

    private static final String TAG = "MainActivity";

    //ui components
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    //vars
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uid, user_email;
    private ArrayList<Merchandise> mMerchandise = new ArrayList<>();

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
        setupData();
        setupRecyclerView();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_test:
                                FirebaseAuth.getInstance().signOut();
                                finish();
                                sendBack();
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

                startActivity(new Intent(MainActivity.this, AddItemActivity.class) );
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

    private void setupData() {
        Log.d(TAG, "setupData: setting up data");

        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        }

        mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMerchandise.clear();

                Merchandise merchandise = dataSnapshot.getValue(Merchandise.class);
                mMerchandise.add(merchandise);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupNavigationHeader() {
        View header = navigationView.getHeaderView(0);
        final TextView nav_username = header.findViewById(R.id.nav_username);
        final TextView nav_useremail = header.findViewById(R.id.nav_useremail);
        final TextView nav_header_version = header.findViewById(R.id.nav_header_version);
    }

    public void setupRecyclerView(){
        Log.d(TAG, "setupRecyclerView: setting up recyclerview");
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        MerchandiseAdapter adapter = new MerchandiseAdapter(this, mMerchandise, this);
        mRecyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
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

    @Override
    public void onMerchClick(int position) {
        Log.d(TAG, "onMerchClick: clicked");
    }
}
