package com.realllydan.management;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddItemActivity extends AppCompatActivity {
    private EditText name,cost,quantity;
    private Button submit;
    private String Pname,Pcost,Pquantity;
    private ImageView mAddImageView;
    private static  final String TAG="AddItemActivity";
    //vars
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    public static final int GALLERY_INTENT = 1;
    private String download_url, random_chars;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        name=findViewById(R.id.et_name);
        cost=findViewById(R.id.et_cost);
        quantity=findViewById(R.id.et_quan);
        submit=findViewById(R.id.bt_submit);
        mProgressBar=findViewById(R.id.mprogressbar);
        mProgressBar.setVisibility(View.GONE);
        mAddImageView=findViewById(R.id.iv_image);
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 send_product(name,cost,quantity);
            }
        });
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseImageIntent();
            }
        });
    }
    public  void  send_product(EditText n,EditText c,EditText q)
    {
        Pname=n.getText().toString();
        Pcost=c.getText().toString();
        Pquantity=q.getText().toString();
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
                                        .into(mAddImageView);

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
}
