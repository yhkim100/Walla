package com.jbuie.walla;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

public class ConvoHistoryActivity extends AppCompatActivity {

    boolean truth = false;
    private Firebase mFirebaseConvoRef;
    FirebaseListAdapter<ConversationClass> myConvosAdapter;
    FirebaseListAdapter<ConversationClass> convosWithMeAdapter;
    private Firebase userAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        truth = true;
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_convo_history);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mFirebaseConvoRef = new Firebase("https://amber-torch-611.firebaseio.com/Conversations/");
        //mFirebaseConvoRef = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Conversations/");

        userAuth = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles/");

        final ListView convoView = (ListView) findViewById(R.id.myConvosListView);
        final ListView convoWithMeView = (ListView) findViewById(R.id.convosWithMeListView);

        final Query myConvos = mFirebaseConvoRef.orderByChild("user1")
                .equalTo(userAuth.getAuth().getProviderData().get("email").toString());

        final Query convosWithMe = mFirebaseConvoRef.orderByChild("user2")
                .equalTo(userAuth.getAuth().getProviderData().get("email").toString());

        myConvosAdapter = new FirebaseListAdapter<ConversationClass>(this, ConversationClass.class,
                R.layout.myconvo_table_layout, myConvos) {
            @Override
            protected void populateView(View view, ConversationClass conversationClass, int i) {
                ((TextView)view.findViewById(R.id.myconvo_username)).setText(conversationClass.getUser2());
            }
        };
        convoView.setAdapter(myConvosAdapter);
        convoView.setItemsCanFocus(true);
        convoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversationClass tempConvo = (ConversationClass) convoView.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(), "Start private Convo with " +
                        tempConvo.getUser2()+"\nKey Id: "+ myConvosAdapter.getRef(position).getKey(),
                        Toast.LENGTH_SHORT).show();*/
                Intent i = new Intent(getBaseContext(),PrivateMessageActivity.class);
                i.putExtra("convoID", myConvosAdapter.getRef(position).getKey());
                startActivity(i);

            }
        });



        convosWithMeAdapter = new FirebaseListAdapter<ConversationClass>(this, ConversationClass.class,
                R.layout.convome_table_layout, convosWithMe) {
            @Override
            protected void populateView(View view, ConversationClass conversationClass, int i) {
                ((TextView)view.findViewById(R.id.convome_username)).setText(conversationClass.getUser1());
            }
        };
        convoWithMeView.setAdapter(convosWithMeAdapter);
        convoWithMeView.setItemsCanFocus(true);
        convoWithMeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*ConversationClass tempConvo = (ConversationClass) convoWithMeView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Conversation Started with you. " +
                        "\nKey Id:" + convosWithMeAdapter.getRef(position).getKey(),
                        Toast.LENGTH_SHORT).show();*/
                Intent i = new Intent(getBaseContext(),PrivateMessageActivity.class);
                i.putExtra("convoID", convosWithMeAdapter.getRef(position).getKey());
                startActivity(i);
            }
        });




        ImageButton homeBtn = (ImageButton)findViewById(R.id.home_button);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConvoHistoryActivity.this, HomeActivity.class));
                finish();
            }
        });

        ImageButton profileBtn = (ImageButton)findViewById(R.id.profile_button);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConvoHistoryActivity.this, MyProfileActivity.class));
                finish();
            }
        });

        ImageButton hollerBtn = (ImageButton)findViewById(R.id.holler_button);
        hollerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConvoHistoryActivity.this, PostRequestActivity.class));
            }
        });

        ImageButton chatBtn = (ImageButton)findViewById(R.id.chat_button);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ConvoHistoryActivity.this, MessageActivity.class));
                //Toast.makeText(getApplicationContext(), "This button should have no function", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton settingsBtn = (ImageButton)findViewById(R.id.settings_button);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConvoHistoryActivity.this, SettingsActivity.class));
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myConvosAdapter.cleanup();
        convosWithMeAdapter.cleanup();
    }

}
