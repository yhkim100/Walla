package com.jbuie.walla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.firebase.client.Query;

//public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button bRegister;
    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etRepassword;
    EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etRepassword = (EditText)findViewById(R.id.etRepassword);

        bRegister = (Button)findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        final Firebase ref = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles");

        switch(v.getId()){
            case R.id.bRegister:
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhoneNumber.getText().toString();
                String password = etPassword.getText().toString();
                String repassword = etRepassword.getText().toString();
                if(email.matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!isEmailValid(email)){
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!doesEmailExist(email)){
                    Toast.makeText(getApplicationContext(), "This email address is already registered.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(username.matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter a Username", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(password.matches("")){
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!password.matches(repassword)){
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    break;
                }
                final String finalUsername = username;
                final String finalEmail = email;
                final String finalPhone = phone;
                final String finalPassword = password;
                ref.createUser(email, password, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Firebase mFirebaseRef = new Firebase("https://amber-torch-611.firebaseio.com/Users");
                        User tmpUser = new User(finalUsername, finalEmail, finalPhone, "0");
                        mFirebaseRef.child(tmpUser.getEmail()).setValue(tmpUser);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "Could not authenticate with database", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }

    public static boolean doesEmailExist(String email) {
        Firebase mFirebasePostRef  = new Firebase("https://amber-torch-611.firebaseio.com/Users/");
        email = setDatabaseEmail(email);
        Query userQuery = mFirebasePostRef.orderByChild("email").equalTo(email);
        if (userQuery == null) {
            return false;
        }else{
            return true;
        }
    }

    public static String setDatabaseEmail(String email){
        String tmpString = email.replaceAll("@", "AT");
        tmpString = tmpString.replaceAll("\\.", "DOT");
        return tmpString;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
