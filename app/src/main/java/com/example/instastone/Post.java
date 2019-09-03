package com.example.instastone;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "postDescription";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getDescription(){

        return getString(KEY_DESCRIPTION); // String associated with postDescription @Parse

    }

    public ParseFile getImage(){

        return getParseFile(KEY_IMAGE); // gets the image @parse

    }

    public ParseUser getUser(){

        return getParseUser(KEY_USER); // this is the object ID for a user

    }

    public void setDescription(String description){

        put(KEY_DESCRIPTION,description); // make a postDescription for a user

    }

    public void setImage(ParseFile image) {

        put(KEY_IMAGE, image); //Set image for the user

    }

    public void setUser(ParseUser user){

        put(KEY_USER, user); //set the user who is adding and posting

    }

}
