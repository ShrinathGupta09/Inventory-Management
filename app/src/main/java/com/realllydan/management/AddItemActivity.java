package com.realllydan.management;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddItemActivity";

    //ui components
    private Toolbar mToolbar;
    private ImageView ivImage;
    private EditText etName, etCost, etQuantity;
    private Button bSubmit;
    private ProgressBar mProgressBar;

    //vars
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    public static final int GALLERY_INTENT = 1;
    private String download_url, random_chars;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Log.d(TAG, "onCreate: called");
        ivImage = findViewById(R.id.ivImage);
        etName = findViewById(R.id.etName);
        etCost = findViewById(R.id.etCost);
        etQuantity = findViewById(R.id.etQuantity);
        bSubmit = findViewById(R.id.bSubmit);
        mProgressBar = findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.GONE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        setupToolbar();

        ivImage.setOnClickListener(this);
        bSubmit.setOnClickListener(this);
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar: setting up toolbar");

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Image");
    }

    private void startChooseImageIntent() {
        Log.d(TAG, "startChooseImageIntent: starting intent");

        Intent gallery_intent = new Intent();
        gallery_intent.setType("image/*");
        gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery_intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called");

        mProgressBar.setVisibility(View.VISIBLE);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: result OK");

            Uri image_uri = data.getData();
            random_chars = mDatabase.push().toString();
            final StorageReference mFilepath = mStorage.child(random_chars);

            mFilepath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: task successful");

                        mFilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri my_download_url = uri;
                                download_url = my_download_url.toString();

                                Log.d(TAG, "onComplete: download_url: " + my_download_url);

                                Glide.with(getApplicationContext())
                                        .load(my_download_url)
                                        .into(ivImage);

                                mProgressBar.setVisibility(View.GONE);
                            }
                        }) ;

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "Task Unsuccessful", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Error: RESULT_NOT_OK", Snackbar.LENGTH_LONG).show();
        }
    }

    private void addImageURLToFirebase() {
        Log.d(TAG, "addImageURLToFirebase: adding image to database");

        mProgressBar.setVisibility(View.VISIBLE);

        if (ivImage.getDrawable() != null &&
                etName.getText() != null &&
                etCost.getText() != null &&
                etQuantity.getText() != null) {

            Merchandise merchandise = new Merchandise();
            merchandise.setMerch_name(etName.getText().toString());
            merchandise.setMerch_cost(etCost.getText().toString());
            merchandise.setMerch_quantity(etQuantity.getText().toString());
            merchandise.setMerch_image_url(download_url);

            if (mAuth.getCurrentUser() != null) {
                uid = mAuth.getCurrentUser().getUid();

            }

            mDatabase.child(uid).child(merchandise.getMerch_name()).setValue(merchandise).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        mProgressBar.setVisibility(View.GONE);
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "Success", Snackbar.LENGTH_LONG).show();
                        finish();
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "Error: mDatabase", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            mProgressBar.setVisibility(View.GONE);
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Please fill all fields", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: clicked a view");

        switch (v.getId()) {
            case R.id.ivImage:
                startChooseImageIntent();
                break;

            case R.id.bSubmit:
                addImageURLToFirebase();
                break;

            default:
                break;
        }
    }
}
