package com.example.kelei.gridimagesearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kelei on 2/28/15.
 */
public class ImageResult implements Serializable {
    public String fullUrl;
    public String thumbUrl;
    public String title;
    public int width;
    public int height;

    ImageResult(JSONObject json) {
        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> imageResults = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                imageResults.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {

            }
        }
        return imageResults;
    }
}
