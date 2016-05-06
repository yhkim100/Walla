package com.jbuie.walla;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by jonbuie on 4/12/16.
 */
public class PrivateMessageActivity extends AppCompatActivity {

    private Firebase chatRef;
    private Firebase currUser;
    FirebaseListAdapter<ChatMessage> mConvoAdapter;
    String userName;
    String convoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            convoID = extras.getString("convoID");
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_pmsg);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title_pmsg);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //chatRef = new Firebase("https://amber-torch-611.firebaseio.com/Dev/Message/"+ convoID);
        chatRef = new Firebase("https://amber-torch-611.firebaseio.com/Message/"+ convoID);
        currUser = new Firebase("https://amber-torch-611.firebaseio.com/userProfiles");

        //final Firebase chatRef = mFirebaseRef.child(childName);

        final EditText textEdit = (EditText) this.findViewById(R.id.text_edit);
        Button sendButton = (Button) this.findViewById(R.id.sendMsg_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                if(text.matches("")) {
                    Toast.makeText(getApplicationContext(), "Say SOMETHING!", Toast.LENGTH_SHORT).show();
                }
                else{
                    ChatMessage message = new ChatMessage(userName, text);
                    chatRef.push().setValue(message);
                    textEdit.setText("");
                    textEdit.setHint("");
                }
            }
        });

        final ListView convoView = (ListView) this.findViewById(R.id.conversationBox);
        mConvoAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                android.R.layout.two_line_list_item, chatRef) {
            @Override
            protected void populateView(View view, ChatMessage chatMessage, int i) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(chatMessage.getName());
                ((TextView)view.findViewById(android.R.id.text2)).setText(chatMessage.getText());
            }
        };
        convoView.setAdapter(mConvoAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userName = currUser.getAuth().getProviderData().get("email").toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConvoAdapter.cleanup();
    }
}

