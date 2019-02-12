package com.realllydan.management;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    //ui components
    private TextView tvWelcome, tvWelcome2;
    private Button bRegister, bLogin;
    private Toolbar mToolbar;

    //vars
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bRegister = findViewById(R.id.bRegister);
        bLogin = findViewById(R.id.bLogin);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome2 = findViewById(R.id.tvWelcome2);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(registerIntent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void setupTypeface() {
        Typeface Helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.otf");

        tvWelcome.setTypeface(Helvetica);
        tvWelcome2.setTypeface(Helvetica);
        bRegister.setTypeface(Helvetica);
        bLogin.setTypeface(Helvetica);
    }
}