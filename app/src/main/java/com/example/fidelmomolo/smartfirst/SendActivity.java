package com.example.fidelmomolo.smartfirst;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {
    TextView send_user;
    EditText send_message;
    Button send_button;
    FirebaseFirestore firestore;
    String current_user_id;
    DocumentReference documentReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
         send_user=findViewById(R.id.send_user);
         send_message =findViewById(R.id.send_message);
         send_button=findViewById(R.id.send_button);
         progressBar =findViewById(R.id.send_progressbar);
         progressBar.setVisibility(View.INVISIBLE);


         firestore=FirebaseFirestore.getInstance();
         current_user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

         String muser_id=getIntent().getExtras().getString("userid");

         documentReference=firestore.collection("user_table").document(muser_id);


         send_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String message=send_message.getText().toString().trim();
                 progressBar.setVisibility(View.VISIBLE);

                 if(!TextUtils.isEmpty(message)){
                     Map<String,Object>map=new HashMap<>();
                     map.put("message",message);
                     map.put("user_id",current_user_id);

                     documentReference.collection("notifications").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                         @Override
                         public void onSuccess(DocumentReference documentReference) {

                             Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_LONG).show();
                             send_message.setText(" ");
                             progressBar.setVisibility(View.INVISIBLE);

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(SendActivity.this, "The Notification has not been sent", Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.INVISIBLE);
                         }
                     });





                 }

             }
         });
    }
}
