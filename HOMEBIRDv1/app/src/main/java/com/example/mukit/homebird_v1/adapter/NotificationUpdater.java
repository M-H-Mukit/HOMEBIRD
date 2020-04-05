package com.example.mukit.homebird_v1.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.mukit.homebird_v1.activities.MainActivity;
import com.example.mukit.homebird_v1.model.RegisteredPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationUpdater {

    public static int unknown_count;
    public  int total_count;
    private String URL_JSON="http://18.220.219.13:5001/people";

    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<RegisteredPerson> listPerson, previousList ;

    public NotificationUpdater() {
    }

    public NotificationUpdater(int total_count) {

        this.total_count = total_count;
    }

    public static int getUnknown_count() {
        return unknown_count;
    }

    public int getTotal_count() {
        jsonrequest();
        return total_count;
    }

    public void updateList(){
        listPerson = new ArrayList<>();
        previousList = new ArrayList<>();

        jsonrequest();

    }

    private void jsonrequest() {


        // Initialize a new RequestQueue instance

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{


                            Log.d("XXXX", "in try block");


                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                // Get the current student (json object) data
                                String firstName = student.getString("firstname");
                                String lastName = student.getString("lastname");
                                String age = student.getString("age");


                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("XXXX", "error occurred");
                    }
                }
        );



    }



}
