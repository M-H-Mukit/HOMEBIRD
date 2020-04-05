package com.example.mukit.homebird_v1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.mukit.homebird_v1.R;

public class ZoomedView extends AppCompatActivity {

    Toolbar toolbar;
    String imageUrl;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomed_view);


        // ------- for toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //----------------

        getIncomingIntent();


    }

    private void getIncomingIntent(){


        if(getIntent().hasExtra("image_url")){

            imageUrl = getIntent().getStringExtra("image_url");

            setImage(imageUrl);
        }
    }

    private void setImage(String imageUrl){

        img= findViewById(R.id.zoomed_img_view);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
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
                Intent returnHome = new Intent(this, MainActivity.class);
                startActivity(returnHome);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------
}
