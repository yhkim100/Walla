package com.jbuie.walla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

/**
 * Created by jonbuie on 4/12/16.
 */
public class SettingsActivity extends AppCompatActivity {


    private Firebase userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        userAuth = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles");


        final Button logoutBtn = (Button) findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 userAuth.unauth();
                 startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                 finish();
             }
         });

        final Button userSettingsButton = (Button) findViewById(R.id.btn_user_settings);
        userSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, UserSettingsActivity.class));
                finish(); //!!!
            }
        });

                ImageButton homeBtn = (ImageButton) findViewById(R.id.home_button);
        homeBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                finish();
            }
        });

        ImageButton profileBtn = (ImageButton) findViewById(R.id.profile_button);
        profileBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                startActivity(new Intent(SettingsActivity.this, MyProfileActivity.class));
                finish();
            }
        });

        ImageButton hollerBtn = (ImageButton) findViewById(R.id.holler_button);
        hollerBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                startActivity(new Intent(SettingsActivity.this, PostRequestActivity.class));

            }

        });

        ImageButton chatBtn = (ImageButton) findViewById(R.id.chat_button);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                startActivity(new Intent(SettingsActivity.this, ConvoHistoryActivity.class));
                finish();

            }

        });



        ImageButton settingsBtn = (ImageButton) findViewById(R.id.settings_button);
        settingsBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                //This button does nothing
            }
        });


    }
}
