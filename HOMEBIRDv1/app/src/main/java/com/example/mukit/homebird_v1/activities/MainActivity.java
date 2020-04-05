package com.example.mukit.homebird_v1.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.adapter.NotificationUpdater;
import com.example.mukit.homebird_v1.model.ArrivedPerson;
import com.example.mukit.homebird_v1.model.RegisteredPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.RelativeLayout.TRUE;

public class MainActivity extends Activity {
    // variables
    private Button regBtn,alertBtn;

    public static int final_count;

    public static int getFinal_count() {
        return final_count;
    }

    public static void setFinal_count(int final_count) {
        MainActivity.final_count = final_count;
    }

    public static int notification_counter =0 ;
    private String URL_JSON="http://18.220.219.13:5001/status";
    private List<RegisteredPerson> listPerson, previousList ;
    private JsonArrayRequest ArrayRequest ;

    TextView count_view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        regBtn = findViewById(R.id.regBtn);
        alertBtn= findViewById(R.id.alertBtn);
        count_view = findViewById(R.id.notification_count);

        regBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // On button clicked
                Intent regPeopleActivity = new Intent(MainActivity.this, RegisteredPeopleActivity.class);
                startActivity(regPeopleActivity);
            }
        });

        alertBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // On button clicked
                Intent alertActivity = new Intent(MainActivity.this, AlertActivity.class);
                startActivity(alertActivity);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        notificaitonCounterUpdate();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        notification_counter=0;
    }

    public void notificaitonCounterUpdate(){
        jsonrequest();
        MainActivity ob =new MainActivity();
        if (ob.getFinal_count() > 0){

            count_view.setVisibility(View.VISIBLE);
            count_view.setText(String.valueOf(ob.getFinal_count()));
        }
        else{
            count_view.setVisibility(View.GONE);
        }
    }



    public void jsonrequest() {

        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        notification_counter =0 ;
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

                        JSONObject jsonObject =null;

//                        for (int i=0 ; i< response.length();i++) {
//
//                            try {
//                                jsonObject= response.getJSONObject(i);
//                                String view =jsonObject.getString("view");
//
//                                Log.d("XXXXX",view);
//
//                                if(view.equals("1")){
//                                    notification_counter++;
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }

                        notification_counter=response.length();

                        MainActivity ob= new MainActivity();
                        ob.setFinal_count(notification_counter);



                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        showToast("error response");
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);



    }

    public void showToast(String str){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

}