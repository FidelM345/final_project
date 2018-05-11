package com.example.fidelmomolo.smartfirst;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Donor_Registration extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    EditText email,weight,age;
    Button register,update;
    Spinner blood,donor_gender;
    TextView name,location2001;
    CircleImageView circleImageView;
    String user_id;
    String fname,lname,location,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor__registration);

        blood =findViewById(R.id.spinner_blood);
        donor_gender=findViewById(R.id.donor_gender);
        email =findViewById(R.id.donor_email);
        weight=findViewById(R.id.donor_weight);
        age=findViewById(R.id.donor_age);
        update=findViewById(R.id.donor_update);
        register=findViewById(R.id.donor_register);
        circleImageView=findViewById(R.id.profile_image);
        name=findViewById(R.id.Fname);
        location2001=findViewById(R.id.location200);

        //firebase objects
        mAuth=FirebaseAuth.getInstance();
        user_id=mAuth.getCurrentUser().getUid();//gets user current id
        firestore=FirebaseFirestore.getInstance();

        update.setOnClickListener(this);
        register.setOnClickListener(this);

         update.setVisibility(View.INVISIBLE);

         email.setText(null);

    }


    @Override
    protected void onStart() {
        super.onStart();

        firestore.collection("donor_table").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String blood_group=documentSnapshot.getString("blood_group");
                String age1=documentSnapshot.getString("age");
                String email1=documentSnapshot.getString("email");
                String weight1=documentSnapshot.getString("weight");


                if(blood_group==null){
                  //do nothing
                }else{

                    update.setVisibility(View.VISIBLE);
                    register.setVisibility(View.INVISIBLE);
                    email.setText(email1);
                    weight.setText(weight1);
                    age.setText(age1);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                email.setText("N/A");

                Toast.makeText(Donor_Registration.this, "It has failed", Toast.LENGTH_LONG).show();
            }
        });


        donor_registration();

    }

    public void donor_registration(){


        firestore.collection("user_table").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){

                        fname=task.getResult().getString("fname");
                        lname=task.getResult().getString("lname");
                        location=task.getResult().getString("location");
                        image=task.getResult().getString("imageuri");

                        //ou need to inflate the header view as it is not inflated automatically .

                        name.setText(fname+" "+lname);
                        location2001.setText(location);

                        Toast.makeText(Donor_Registration.this, "There is data in me  "+fname, Toast.LENGTH_LONG).show();

                        RequestOptions placeHolder=new RequestOptions();
                        placeHolder.placeholder(R.drawable.swallowed_poison);

                        Glide.with(Donor_Registration.this).setDefaultRequestOptions(placeHolder).load(image).into(circleImageView);





                    }
                    else{
                        Toast.makeText(Donor_Registration.this, "No data has been saved", Toast.LENGTH_LONG).show();

                        return;

                    }

                }

                else{
                    String exception=task.getException().getMessage();

                    Toast.makeText(Donor_Registration.this, "Data Retreival Error is: "+exception, Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    private void storedata() {

        Map<String,String> user_details=new HashMap<>();
        user_details.put("fname",fname);
        user_details.put("lname",lname);
        user_details.put("location",location);
        user_details.put("imageuri",image);
        user_details.put("blood_group",blood.getSelectedItem().toString().trim());
        user_details.put("gender",donor_gender.getSelectedItem().toString().trim());
        user_details.put("weight",weight.getText().toString().trim());
        user_details.put("age",age.getText().toString().trim());
        user_details.put("email",email.getText().toString().trim());


       // DocumentReference documentReference=firestore.collection("donor_table").document();


       firestore.collection("donor_table").document(user_id).set(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {

               if(task.isSuccessful()){

                   Toast.makeText(Donor_Registration.this,"Data has been saved successfully: ",Toast.LENGTH_LONG).show();

                   Intent intent=new Intent(Donor_Registration.this,BloodActivity.class);
                   startActivity(intent);



               }else{

                   Toast.makeText(Donor_Registration.this,"Error message: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
               }


           }
       });



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case  R.id.donor_register:

                if(!TextUtils.isEmpty(blood.getSelectedItem().toString().trim())&&!TextUtils.isEmpty(weight.getText().toString().trim())&&!TextUtils.isEmpty(age.getText().toString().trim())&&!TextUtils.isEmpty(email.getText().toString().trim())&&!TextUtils.isEmpty(donor_gender.getSelectedItem().toString().trim())){

                    storedata();


                }else{

                    Toast.makeText(Donor_Registration.this,"Please fill all fields",Toast.LENGTH_LONG).show();


                }

            break;

            case  R.id.donor_update:
                Toast.makeText(Donor_Registration.this,"you have clicked me",Toast.LENGTH_LONG).show();
                storedata();
                break;

        }


    }
}
