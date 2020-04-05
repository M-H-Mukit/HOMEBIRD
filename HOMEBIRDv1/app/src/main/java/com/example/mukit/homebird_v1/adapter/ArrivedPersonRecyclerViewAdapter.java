package com.example.mukit.homebird_v1.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mukit.homebird_v1.R;
import com.example.mukit.homebird_v1.activities.AddUnknownFaceActivity;
import com.example.mukit.homebird_v1.activities.SwipeActivity;
import com.example.mukit.homebird_v1.activities.ZoomedView;
import com.example.mukit.homebird_v1.model.ArrivedPerson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class ArrivedPersonRecyclerViewAdapter extends RecyclerView.Adapter<ArrivedPersonRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ArrivedPerson> mData;
    private ArrayList<String> img_url = new ArrayList<>();

    ImageView largeImg;
    LinearLayout layout;


    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    RequestParams params = new RequestParams();

    RequestOptions options;

    public ArrivedPersonRecyclerViewAdapter(Context mContext, List<ArrivedPerson> mData, ImageView im, LinearLayout l) {
        this.mContext = mContext;
        this.mData = mData;
        this.largeImg = im;
        this.layout = l;

        // request options for Glide
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.arrival_person_row_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.person_name.setText(mData.get(position).getName());
        holder.person_date.setText(mData.get(position).getDate());
        holder.arrival_time.setText(mData.get(position).getTime());


        // load image from the internet using Glide
        Glide.with(mContext).load(mData.get(position).getImage_url()).apply(options).into(holder.img_thumbnail);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                LinearLayout snack_layout;

                if (mData.get(position).getName().toLowerCase().equals("unknown")) {

                    Intent intent = new Intent(mContext, SwipeActivity.class);
                    intent.putExtra("image_url", mData.get(position).getImage_url());
                    mContext.startActivity(intent);


                } else {

                    Intent intent = new Intent(mContext, ZoomedView.class);
                    intent.putExtra("image_url", mData.get(position).getImage_url());
                    mContext.startActivity(intent);

                    //TODO


                }

            }
        });
    }




    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView person_name;
        TextView person_date;
        TextView arrival_time;
        ImageView img_thumbnail;
        LinearLayout parent_layout;
        public RelativeLayout viewBackgroundUnknown, viewBackgroundKnown;
        public LinearLayout viewForeground;


        public MyViewHolder(View itemView) {
            super(itemView);

            person_name = itemView.findViewById(R.id.person_name);
            person_date = itemView.findViewById(R.id.date);
            arrival_time = itemView.findViewById(R.id.time);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);
            parent_layout = itemView.findViewById(R.id.view_foreground);
            viewBackgroundUnknown = itemView.findViewById(R.id.view_background_unknown);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewBackgroundKnown = itemView.findViewById(R.id.view_background_known);


        }
    }


    public void onLeftSwiped(final int position) {

        Log.d("XXXX", mData.get(position).getName().toLowerCase());

        params.put("time", mData.get(position).getTimestamp());
        makeHTTPCall();


//        if (mData.get(position).getName().toLowerCase().equals("unknown")) {

//            alertDialog.setTitle("Unknown face!");
//            alertDialog.setMessage("Do you know this person?");
//            alertDialog.setPositiveButton("Don't know", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
        //TODO

//            params.put("time", mData.get(position).getTimestamp());
//            makeHTTPCall();

//        Intent intent = new Intent(mContext, UnknownPersonActivity.class);
//        intent.putExtra("image_url", mData.get(position).getImage_url());


//        mContext.startActivity(intent);

//
//                }
//            });

//        } else {
//            alertDialog.setTitle("Face is known");
//            alertDialog.setMessage("No action required");
//
////            alertDialog.setPositiveButton("Clear alert", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    dialog.cancel();
////
//////                    params.put("time", mData.get(position).getTimestamp());
//////                    makeHTTPCall();
////
//////                    mData.remove(position);
//////                    notifyItemRemoved(position);
////                }
////            });
//
//
//        }
//
//        AlertDialog dialog = alertDialog.create();
//        dialog.show();
    }


    public void makeHTTPCall() {

        AsyncHttpClient client = new AsyncHttpClient();

        // TODO Don't forget to change the IP address to your LAN address. Port no as well.

//        "http://192.168.43.51:6060/imgupload/upload_image.php"

        client.post("http://18.220.219.13:5001/alert",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {

                        // TODO
//                        showToast("Deleted");
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(mContext.getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(mContext.getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    mContext.getApplicationContext(),
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    public void removeItem(int position) {
        mData.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void showToast(String str) {

        Context context = mContext.getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }
}
