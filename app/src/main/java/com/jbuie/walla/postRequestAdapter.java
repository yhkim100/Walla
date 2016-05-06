package com.jbuie.walla;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jonbuie on 4/9/16.
 */
public class postRequestAdapter extends ArrayAdapter<PostRequest> {

    private ArrayList<PostRequest> objects;


    /* Here is the override for the constructor for ArrayAdapter
    * the only variable we care about is the ArrayList<PostRequest> objects,
    * because it has all the items we want to display!
    * */
    public postRequestAdapter(Context context, int textViewResourceId, ArrayList<PostRequest> objects){
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        //is the view null? Lets set it up!
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.request_table_view,null);
        }

        //Get the object thats at the current position of the listView
        PostRequest i = objects.get(position);

        if( i != null){
            //We have a post request and now need to fill in the correct fields for the layout

            /*Right now we only fill in the request, location and timestamp
            * we will need to add information for the user as well the photo
            * of the REQUESTING user
            */
            TextView txtReq = (TextView) v.findViewById(R.id.request_text);
            TextView txtTime = (TextView) v.findViewById(R.id.timestamp_text);
            TextView txtLoc = (TextView) v.findViewById(R.id.location_text);

            //TextView txtUser = (TextView) v.findViewById(R.id.holla_username_text);

            if(txtReq != null){

                //Right now not doing a check for Request b/c its a required field
                txtReq.setText(i.getRequest());
            }
            if(txtTime != null){
                String checkTime = txtTime.getText().toString();

                //Need to see if the time field is null b/c original cells didn't have timestamps
                if(checkTime == null){
                    txtTime.setText("");
                }
                else {
                    txtTime.setText(i.getTimeStamp());
                }
            }
            if(txtLoc != null){
                //Right now not doing a check for location b/c its a required field
                txtLoc.setText(i.getLocation());
            }
        }

        // Return view to the activity
        return v;
    }
}
