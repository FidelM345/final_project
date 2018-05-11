package com.example.fidelmomolo.smartfirst;

/**
 * Created by Fidel M Omolo on 5/5/2018.
 */

public class Forum_Adapter_model extends BlogPostId {

    String description,user_id,imageUri, thumbUri,title;


    public Forum_Adapter_model() {

    }

    public Forum_Adapter_model(String description, String user_id, String imageUri, String thumbUri, String title) {
        this.description = description;
        this.user_id = user_id;
        this.imageUri = imageUri;
        this.thumbUri = thumbUri;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
