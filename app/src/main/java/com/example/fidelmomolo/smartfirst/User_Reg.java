package com.example.fidelmomolo.smartfirst;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Reg extends AppCompatActivity implements View.OnClickListener {

    StorageReference mstorageReference;
    FirebaseAuth mAuth;
    FirebaseFirestore mfirestore;
    EditText Fname, Lname;
    Spinner location;
    Button Okbutton;
    CircleImageView circleImageView;
    Uri mainImageUri=null;
    boolean isChaanged = false;
    ProgressBar progressBar;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__reg);

        //initializing firebase critical objects
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

        Fname = findViewById(R.id.userreg_fname);
        Lname = findViewById(R.id.userreg_lname);
        location = findViewById(R.id.userreg_location);
        Okbutton = findViewById(R.id.userreg_ok_button);
        circleImageView = findViewById(R.id.profile_image);
        progressBar= findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        circleImageView.setOnClickListener(this);
        Okbutton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profile_image:

                //allow user to select profile pic from phone memory
                profilePicPicker();

                break;

            case R.id.userreg_ok_button:

                if(isChaanged){ //becomes true when new image is selected
                    final String first_name=Fname.getText().toString().trim();
                    final String last_name=Lname.getText().toString().trim();
                    final String user_location=location.getSelectedItem().toString().trim();
                    user_id=mAuth.getCurrentUser().getUid();

                    progressBar.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(first_name)&&!TextUtils.isEmpty(last_name)&&!TextUtils.isEmpty(user_location) && mainImageUri !=null){


                        StorageReference image_path=mstorageReference.child("Profile_image").child(user_id+".jpg");

                        image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()){


                                    storeFirestore(task,first_name,last_name,user_location);//used for storing image and the user name in firebase
                                    Toast.makeText(User_Reg.this, "Your Accounts settings have been updated", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(User_Reg.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {

                                    String exception=task.getException().getMessage();

                                    Toast.makeText(User_Reg.this, "Image Error is: "+exception, Toast.LENGTH_LONG).show();
                                }

                            }
                        });



                    }

                }else {

                    Toast.makeText(User_Reg.this, "Please select image first and then press enter ", Toast.LENGTH_LONG).show();

                  }


        }
    }

    private void storeFirestore(Task<UploadTask.TaskSnapshot> task, String first_name, String last_name, String user_location) {

              Uri download_uri;
              download_uri=task.getResult().getDownloadUrl();
              progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(User_Reg.this, "The image has been uploaded", Toast.LENGTH_LONG).show();

        Map<String,String> user_details=new HashMap<>();
        user_details.put("fname",first_name);
        user_details.put("lname",last_name);
        user_details.put("location",user_location);
        user_details.put("imageuri",download_uri.toString());

        mfirestore.collection("user_table").document(user_id).set(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){


                    gotMainActivity();

                }else{

                    String exception=task.getException().getMessage();

                    Toast.makeText(User_Reg.this, "Text Error is: "+exception, Toast.LENGTH_LONG).show();
                }

            }
        });





    }

    private void gotMainActivity() {
        Intent intent=new Intent(User_Reg.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void profilePicPicker() {
        // the first if statement checks whether the user is running android Mash mellow and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(User_Reg.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // if the permission has been denied allow user to request for the permission

                ActivityCompat.requestPermissions(User_Reg.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

                Toast.makeText(this, "The Beast", Toast.LENGTH_LONG).show();

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            }
        } else {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageUri = result.getUri();
                circleImageView.setImageURI(mainImageUri);
                isChaanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }


        }
    }

}
