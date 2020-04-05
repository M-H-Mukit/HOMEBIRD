package com.example.mukit.homebird_v1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.adapter.ArrivedPersonRecyclerViewAdapter;
import com.example.mukit.homebird_v1.adapter.RegisteredPersonRecyclerViewAdapter;
import com.example.mukit.homebird_v1.model.ArrivedPerson;
import com.example.mukit.homebird_v1.model.RegisteredPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisteredPeopleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button appPersonBtn;



    public static int total_reg_count;

    // TODO, need to change here according to my json
    private String URL_JSON="http://18.220.219.13:5001/people";

    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<RegisteredPerson> listPerson ;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_people);

        // ------- for toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //----------------


        // for reg list view -------------

        listPerson = new ArrayList<>();
        recyclerView= findViewById(R.id.reg_person_recycler_view_id);
        jsonrequest();

        //-----------------------

        appPersonBtn= findViewById(R.id.add_person_btn);
        appPersonBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // On button clicked
                Intent selectImg = new Intent(RegisteredPeopleActivity.this, ImageSelectionActivity.class);
                startActivity(selectImg);
            }
        });
    }


    // --------- for list view registered people -----

    public void jsonrequest() {


        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject =null;

                total_reg_count= response.length();

                for (int i=0 ; i< response.length();i++){

                    // TODO, need to change here according to my json
                    try {
                        jsonObject= response.getJSONObject(i);
                        RegisteredPerson registeredPerson = new RegisteredPerson();

                        String person_name= jsonObject.getString("name");
//                        person_name =person_name.substring(0,1).toUpperCase() +
//                                person_name.substring(1);

                        registeredPerson.setName(person_name);

//                        registeredPerson.setName(jsonObject.getString("name"));
                        registeredPerson.setRelation(jsonObject.getString("relation"));
                        registeredPerson.setImage_url(jsonObject.getString("url"));
                        listPerson.add(registeredPerson);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setupRecyclerView(listPerson);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(RegisteredPeopleActivity.this);
        requestQueue.add(ArrayRequest);


    }

    private void setupRecyclerView(List<RegisteredPerson> listPerson) {

        RegisteredPersonRecyclerViewAdapter myAdapter = new RegisteredPersonRecyclerViewAdapter(this, listPerson);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myAdapter);

    }


    // for toolbar ------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        switch (id){
            case R.id.home_id:
                // on click home
                finish();
                Intent returnHome = new Intent(RegisteredPeopleActivity.this, MainActivity.class);
                startActivity(returnHome);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //--------------

    public void showToast(String str){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
}
