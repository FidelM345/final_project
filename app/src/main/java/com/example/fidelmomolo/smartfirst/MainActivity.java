package com.example.fidelmomolo.smartfirst;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    Button date_text;
    String user_id;
    ImageView imageView1;
    TextView Fname,location22,date;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView1=findViewById(R.id.imageView);
        date=findViewById(R.id.main_text);
        date_text=findViewById(R.id.main_time);

        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar=Calendar.getInstance();
                String current= DateFormat.getDateTimeInstance().format(calendar.getTime());

                date.setText(current);


            }
        });



        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();







        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

         user_profile_details();


    }


    public void user_profile_details(){
        if(mAuth.getCurrentUser()==null){

            Intent intent=new Intent(MainActivity.this,Phone_Login.class);
            startActivity(intent);
            finish();


        } else{

            user_id=mAuth.getCurrentUser().getUid();
            firestore.collection("user_table").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()){

                        if (task.getResult().exists()){

                            String fname=task.getResult().getString("fname");
                            String location=task.getResult().getString("location");
                            String image=task.getResult().getString("imageuri");

                            //ou need to inflate the header view as it is not inflated automatically .
                            View header = navigationView.getHeaderView(0);
                            Fname = (TextView) header.findViewById(R.id.fname1);
                            location22=header.findViewById(R.id.location1);
                            imageView1=header.findViewById(R.id.imageView);

                            Fname.setText(fname);
                            location22.setText(location);

                            Toast.makeText(MainActivity.this, "There is data in me  "+fname, Toast.LENGTH_LONG).show();

                            RequestOptions placeHolder=new RequestOptions();
                            placeHolder.placeholder(R.drawable.swallowed_poison);

                            Glide.with(MainActivity.this).setDefaultRequestOptions(placeHolder).load(image).into(imageView1);


                        }
                        else{
                            Toast.makeText(MainActivity.this, "No data has been saved", Toast.LENGTH_LONG).show();

                        }

                    }

                    else{
                        String exception=task.getException().getMessage();

                        Toast.makeText(MainActivity.this, "Data Retreival Error is: "+exception, Toast.LENGTH_LONG).show();

                    }


                }
            });





        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            mAuth.signOut();
            Intent intent=new Intent(MainActivity.this,Phone_Login.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent intent=new Intent(MainActivity.this,BloodActivity.class);
            startActivity(intent);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent(MainActivity.this,Forum_page.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
