package com.example.mukit.homebird_v1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.adapter.CustomPagerAdapter;

public class SwipeActivity extends AppCompatActivity{

    ImageView img;
    Toolbar toolbar;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getIncomingIntent();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomPagerAdapter(this, imageUrl));
        viewPager.setCurrentItem(1);


    }

    private void getIncomingIntent(){


        if(getIntent().hasExtra("image_url")){

           imageUrl = getIntent().getStringExtra("image_url");

//            setImage(imageUrl);
        }
    }

    private void setImage(String imageUrl){

        img= findViewById(R.id.swipe_img_view);
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
    }

    public void openAppPersonActivity(View view){

        Intent intent = new Intent(this, AddUnknownFaceActivity.class);
        intent.putExtra("image_url",imageUrl);
        startActivity(intent);
        finish();
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
                Intent returnHome = new Intent(SwipeActivity.this, MainActivity.class);
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



    //Swipe methods...........


}
