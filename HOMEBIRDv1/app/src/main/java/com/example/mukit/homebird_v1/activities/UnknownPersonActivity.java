package com.example.mukit.homebird_v1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.mukit.homebird_v1.R;

public class UnknownPersonActivity extends AppCompatActivity {


    ImageView img;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unknown_person);
//        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getIncomingIntent();
    }

    private void getIncomingIntent(){


        if(getIntent().hasExtra("image_url")){

            String imageUrl = getIntent().getStringExtra("image_url");

            setImage(imageUrl);
        }
    }

    private void setImage(String imageUrl){

        img= findViewById(R.id.unknown_img_view);
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
                Intent returnHome = new Intent(UnknownPersonActivity.this, MainActivity.class);
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
