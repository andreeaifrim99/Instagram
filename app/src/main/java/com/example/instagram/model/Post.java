package com.example.instagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_ID = "objectId";
    public static final String KEY_LIKED_BY = "likedBy";

    public String getObjectID() {
        return getString(KEY_ID);
    }
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public JSONArray getLikedBy() {
        JSONArray a = getJSONArray(KEY_LIKED_BY);

        if (a == null) {
            return new JSONArray();
        } else {
            return getJSONArray(KEY_LIKED_BY);
        }
    }

    public boolean isLiked() {
        JSONArray a = getLikedBy();

        for (int i = 0; i < a.length(); i++) {
            try {
                a.get(i);

                if (a.getJSONObject(i).getString("objectId").equals(ParseUser.getCurrentUser().getObjectId())) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void like() {
        ParseUser u = ParseUser.getCurrentUser();
        add(KEY_LIKED_BY, u);

        //get array of users
    }

    public void unlike() {
        //takes the user out of array of likes
        ParseUser u = ParseUser.getCurrentUser();
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(u);
        removeAll(KEY_LIKED_BY, users);
    }

    public int getNumLikes() {
        //number of users in array
        return getLikedBy().length();
    }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }
}
