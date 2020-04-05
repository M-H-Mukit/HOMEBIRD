package com.example.mukit.homebird_v1.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.model.ModelObject;

/**
 * Created by Boss on 12/17/2016.
 */

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String imgUrl;

    public CustomPagerAdapter(Context context, String img) {

        mContext = context;
        imgUrl = img;

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

//        Toast toast = Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_LONG);
//        toast.show();

        ModelObject modelObject = ModelObject.values()[position];


        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);


//        Toast toast = Toast.makeText(mContext, String.valueOf(modelObject.getLayoutResId()), Toast.LENGTH_LONG);
//        toast.show();

        ImageView img= (ImageView) viewGroup.findViewById(R.id.swipe_img_view);
        Glide.with(mContext)
                .asBitmap()
                .load(imgUrl)
                .into(img);

        collection.addView(viewGroup);
        return viewGroup;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);

    }

    @Override
    public int getCount() {
        return ModelObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

}
