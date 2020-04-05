package com.example.mukit.homebird_v1.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.adapter.ArrivedPersonRecyclerViewAdapter;
import com.example.mukit.homebird_v1.model.ArrivedPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, RecyclerItemTouchHelperRight.RecyclerItemTouchHelperListener {

    // TODO, need to change here according to my json
    private String URL_JSON="http://18.220.219.13:5001/status";

    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<ArrivedPerson> listPerson ;
    private RecyclerView recyclerView;

    ImageView imgV;
    LinearLayout layoutV;

    ArrivedPersonRecyclerViewAdapter myAdapter;

    Toolbar toolbar;


    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private static int mShortAnimationDuration;

    int x ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);


        // ------- for toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //----------------


        // TODO
        layoutV = findViewById(R.id.layout);
        imgV =findViewById(R.id.zoomed_img_view);


        listPerson = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_id);


        jsonrequest();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);




    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void jsonrequest() {


        ArrayRequest = new JsonArrayRequest(URL_JSON, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject =null;

                for (int i=0 ; i< response.length();i++){

                    // TODO, need to change here according to my json
                    try {
                        jsonObject= response.getJSONObject(i);
                        ArrivedPerson arrivedPerson = new ArrivedPerson();

                        // TODO uppercase first word of name
                        String person_name= jsonObject.getString("name");
                        person_name =person_name.substring(0,1).toUpperCase() +
                                person_name.substring(1);
                        arrivedPerson.setName(person_name);

                        String view=jsonObject.getString("view");
                        arrivedPerson.setView(view);

//                        arrivedPerson.setName(jsonObject.getString("name"));
                        arrivedPerson.setImage_url(jsonObject.getString("url"));

                        // TODO

                        String time, hour, minute, date;
                        time = jsonObject.getString("timestamp");
                        arrivedPerson.setTimestamp(time);

                        date=time.substring(0,10);
                        hour=time.substring(11,13);
                        minute=time.substring(14,16);

                        if( Integer.valueOf(hour) == 0 ){
                            time= "12" +":"+ minute + "am";
                        }

                        else if( Integer.valueOf(hour) > 12 ){
                            time= String.valueOf(Integer.valueOf(hour)-12) + ":"+ minute +"pm";
                        }
                        else {
                            time= String.valueOf(Integer.valueOf(hour))+":"+ minute + "am";
                        }

                        arrivedPerson.setTime(time);

                        arrivedPerson.setDate(date);

//                        arrivedPerson.setTime(jsonObject.getString("timestamp"));
                        listPerson.add(arrivedPerson);

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

        requestQueue = Volley.newRequestQueue(AlertActivity.this);
        requestQueue.add(ArrayRequest);


    }


    private void setupRecyclerView(List<ArrivedPerson> listPerson) {

        myAdapter = new ArrivedPersonRecyclerViewAdapter(this, listPerson, imgV, layoutV);
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
                Intent returnHome = new Intent(AlertActivity.this, MainActivity.class);
                startActivity(returnHome);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {


//         showToast(String.valueOf(direction));
        myAdapter.onLeftSwiped(position);
        myAdapter.removeItem(position);

    }

    public void showToast(String str){

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

}
