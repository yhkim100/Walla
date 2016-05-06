package com.jbuie.walla;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseListAdapter;
@JsonIgnoreProperties(ignoreUnknown = true)


public class HomeActivity extends AppCompatActivity {
    boolean truth = false;

    private Firebase mFirebasePostRef;
    private Firebase userAuth;

    private ConversationClass convoTest;
    private String userName;

    FirebaseListAdapter<PostRequest> mPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        truth = true;
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mFirebasePostRef  = new Firebase("https://amber-torch-611.firebaseio.com/Requests/");
        //mFirebasePostRef  = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Requests/");
        userAuth = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles");

        final ListView postView = (ListView) findViewById(R.id.requestListView);
        mPostAdapter = new FirebaseListAdapter<PostRequest>(this, PostRequest.class,
                R.layout.request_table_view, mFirebasePostRef) {


            @Override
            protected void populateView(View view, PostRequest postRequest, int i) {

                ((TextView)view.findViewById(R.id.request_text)).setText(postRequest.getRequest());
                ((TextView)view.findViewById(R.id.location_text)).setText(postRequest.getLocation());
                ((TextView)view.findViewById(R.id.timestamp_text)).setText(postRequest.getTimeStamp());
                ((TextView)view.findViewById(R.id.holla_username_text)).setText(postRequest.getWhoRequested());

            }

               /* @Override
                public View getView(int position, View view, ViewGroup viewGroup) {
                    return super.getView(position, view, viewGroup);
                }*/
            };


        postView.setAdapter(mPostAdapter);
        postView.setItemsCanFocus(true);
        postView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostRequest indvRequest = (PostRequest) postView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Here: " + indvRequest.getRequest(), Toast.LENGTH_SHORT).show();

                String tempRef = mPostAdapter.getRef(position).getKey();
                Intent intent = new Intent(HomeActivity.this, HollaBackActivity.class);
                intent.putExtra("myRequest", indvRequest);
                intent.putExtra("postRef", tempRef);
                startActivity(intent);
            }
        });


            ImageButton homeBtn = (ImageButton) findViewById(R.id.home_button);
            homeBtn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                    //Home button does nothing on Home Activity Screen
                }
            });

            ImageButton profileBtn = (ImageButton) findViewById(R.id.profile_button);
            profileBtn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){

                    startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                    finish();

                }
            });

            ImageButton hollerBtn = (ImageButton) findViewById(R.id.holler_button);
            hollerBtn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){

                    startActivity(new Intent(HomeActivity.this, PostRequestActivity.class));

                }

            });

            ImageButton chatBtn = (ImageButton) findViewById(R.id.chat_button);
            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    startActivity(new Intent(HomeActivity.this, ConvoHistoryActivity.class));
                    finish();

                }

            });

            ImageButton settingsBtn = (ImageButton) findViewById(R.id.settings_button);
            settingsBtn.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                    finish();
                }
            });
        }

        @Override
    protected void onStart(){
        super.onStart();
        AuthData authData = userAuth.getAuth();
        if (authData == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
        else {
            userName = userAuth.getAuth().getProviderData().get("email").toString();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPostAdapter.cleanup();
    }
}
