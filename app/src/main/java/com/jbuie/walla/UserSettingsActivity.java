
package com.jbuie.walla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.jbuie.walla.RegisterActivity;
import com.firebase.client.Firebase;
import com.jbuie.walla.User;
import org.w3c.dom.Text;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    TextView etKarma;
    EditText etOldEmail;
    EditText etUserProfileEmail;
    EditText etNickname;
    EditText etPhoneNumber;
    EditText etPassword;
    Button bUserProfileSave;
    Button bUserProfileCancel;

    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_settings);

        etKarma = (TextView)findViewById(R.id.etKarma);
        etUserProfileEmail = (EditText)findViewById(R.id.etUserProfileEmail);
        etNickname = (EditText)findViewById(R.id.etNickname);
        etPhoneNumber =  (EditText)findViewById(R.id.etPhoneNumber);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etOldEmail = (EditText)findViewById(R.id.etOldEmail);
        bUserProfileSave = (Button)findViewById(R.id.bUserProfileSave);
        bUserProfileCancel = (Button)findViewById(R.id.bUserProfileCancel);

        bUserProfileCancel.setOnClickListener(this);
        bUserProfileSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bUserProfileSave:
                String email = etUserProfileEmail.getText().toString();

                String nickname = etNickname.getText().toString();
                String phone = etPhoneNumber.getText().toString();
                String karma = etKarma.getText().toString();
                String password = etPassword.getText().toString();
                String oldEmail = etOldEmail.getText().toString();
                if(!RegisterActivity.doesEmailExist(email)){
                    Toast.makeText(getApplicationContext(), "This email address is already registered.", Toast.LENGTH_SHORT).show();
                    break;
                }
                final String finalOldEmail = RegisterActivity.setDatabaseEmail(oldEmail);
                final String finalNewEmail = email;
                Firebase ref = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles");
                ref.changeEmail(finalOldEmail, password, email, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        mFirebaseRef = new Firebase("https://amber-torch-611.firebaseio.com/Users");
                        mFirebaseRef.child(finalOldEmail).child("email").setValue(finalNewEmail);
                        Toast.makeText(getApplicationContext(), "Your email address has been changed.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserSettingsActivity.this, SettingsActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "This email address could not be registered.", Toast.LENGTH_SHORT).show();
                    }
                });


                break;

            case R.id.bUserProfileCancel:
                startActivity(new Intent(UserSettingsActivity.this, SettingsActivity.class));
                finish();
                break;
        }
    }



}