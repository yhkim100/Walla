package com.jbuie.walla;

/**
 * Created by jonbuie on 4/6/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostRequest implements Parcelable {

    private String request;
    private String whoRequested;
    private String additionalDetails;
    private String location;
    //private String tags[];
    private String timeStamp;
    private double longitude;
    private double latitude;

    public PostRequest() {
    }

    public PostRequest(String request, String whoRequested, String additionalDetails, String location, double longitude, double latitude, String timeStamp) {
        this.whoRequested = whoRequested;
        this.request = request;
        this.additionalDetails = additionalDetails;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeStamp = timeStamp;
    }

    protected PostRequest(Parcel in) {
        request = in.readString();
        whoRequested = in.readString();
        additionalDetails = in.readString();
        location = in.readString();
        //tags = in.createStringArray();
        timeStamp = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<PostRequest> CREATOR = new Creator<PostRequest>() {
        @Override
        public PostRequest createFromParcel(Parcel in) {
            return new PostRequest(in);
        }

        @Override
        public PostRequest[] newArray(int size) {
            return new PostRequest[size];
        }
    };

    public String getWhoRequested() {
        return whoRequested;
    }

    public String getRequest() {
        return request;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public String getLocation() {
        return location;
    }

   /* public String[] getTags() {
        return tags;
    }*/

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public String getTimeStamp(){
        return timeStamp;
    }


    //In order to remove from an Array List apparently need to override the equals operation?
    public boolean equals(Object o){
        if(!(o instanceof PostRequest)){
            return false;
        }
        PostRequest other = (PostRequest) o;
        return request.equals(other.request) && location.equals(other.location) && timeStamp.matches(other.timeStamp);
    }

    public int hashCode(){
        return request.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(request);
        dest.writeString(whoRequested);
        dest.writeString(additionalDetails);
        dest.writeString(location);
     //   dest.writeStringArray(tags);
        dest.writeString(timeStamp);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
