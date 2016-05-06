package com.jbuie.walla;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.IndexedNode;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by jonbuie on 4/12/16.
 */
public class MyProfileActivity extends AppCompatActivity {

    boolean truth = false;
    private Firebase mFirebasePostRef;
    private FirebaseListAdapter<PostRequest> myRequestsAdapter;
    private Firebase userAuth;

    private Firebase mFirebaseUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        truth = true;
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_myprofile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        mFirebasePostRef  = new Firebase("https://amber-torch-611.firebaseio.com/Requests/");
        //mFirebasePostRef  = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Requests/");
        userAuth = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles/");




        Query myRequests = mFirebasePostRef.orderByChild("whoRequested")
                .equalTo(userAuth.getAuth().getProviderData().get("email").toString());

        //final View view;
        final TextView ud_email = (TextView)findViewById(R.id.userdata_email);
        final TextView ud_displayname = (TextView)findViewById(R.id.userdata_displayname);
        final TextView ud_karma = (TextView)findViewById(R.id.userdata_karma);
        final TextView ud_phone = (TextView)findViewById(R.id.userdata_phone);

        /*****************************************************************************/
        String dbEmail = userAuth.getAuth().getProviderData().get("email").toString();
        dbEmail = RegisterActivity.setDatabaseEmail(dbEmail);
        mFirebaseUserRef = new Firebase("https://amber-torch-611.firebaseio.com/Users/" + dbEmail);

        mFirebaseUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);
                //User user = (User)object;
                ud_email.setText(user.getEmail());
                ud_displayname.setText(user.getNickname());
                ud_karma.setText(user.getKarma());
                ud_phone.setText(user.getPhoneNumber());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //Query userQuery = mFirebaseUserRef.orderByChild("email").equalTo(dbEmail);
        //userQuery.
        /****************************************************************************/


        final ListView myPostView = (ListView) findViewById(R.id.my_current_requests);

        myRequestsAdapter = new FirebaseListAdapter<PostRequest>(this, PostRequest.class,
                R.layout.request_table_view, myRequests) {
            @Override
            protected void populateView(View view, PostRequest postRequest, int i) {
                ((TextView)view.findViewById(R.id.request_text)).setText(postRequest.getRequest());
                ((TextView)view.findViewById(R.id.location_text)).setText(postRequest.getLocation());
                ((TextView)view.findViewById(R.id.timestamp_text)).setText(postRequest.getTimeStamp());
                ((TextView)view.findViewById(R.id.holla_username_text)).setText(postRequest.getWhoRequested());
            }
        };
        myPostView.setAdapter(myRequestsAdapter);
        myPostView.setItemsCanFocus(true);
        myPostView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*ConversationClass tempConvo = (ConversationClass) convoWithMeView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Conversation Started with you. " +
                        "\nKey Id:" + convosWithMeAdapter.getRef(position).getKey(),
                        Toast.LENGTH_SHORT).show();*/
                /*Intent i = new Intent(getBaseContext(), HollaBackActivity.class);
                i.putExtra("refID", myRequestsAdapter.getRef(position).getKey());
                startActivity(i);*/
                Toast.makeText(getApplicationContext(),"Show Holla back page w/o button",Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton homeBtn = (ImageButton)findViewById(R.id.home_button);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, HomeActivity.class));
                finish();
            }
        });

        ImageButton profileBtn = (ImageButton)findViewById(R.id.profile_button);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do nothing
            }
        });

        ImageButton hollerBtn = (ImageButton)findViewById(R.id.holler_button);
        hollerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, PostRequestActivity.class));
            }
        });

        ImageButton chatBtn = (ImageButton)findViewById(R.id.chat_button);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ConvoHistoryActivity.this, MessageActivity.class));
                //Toast.makeText(getApplicationContext(), "This button should have no function", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyProfileActivity.this, ConvoHistoryActivity.class));
                finish();

            }
        });

        ImageButton settingsBtn = (ImageButton)findViewById(R.id.settings_button);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, SettingsActivity.class));
                finish();
            }
        });



    }



}



