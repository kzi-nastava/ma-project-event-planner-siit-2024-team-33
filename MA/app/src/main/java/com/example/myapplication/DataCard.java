package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataCard extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_PRICE = "price";
    private static final String ARG_IMAGEURL = "imageUrl";

    private String mTitle;
    private String mDescription;
    private double mPrice;
    private String mImageUrl;


    public DataCard() {
    }

    public static DataCard newInstance(String title, String description, double price, String imageUrl) {
        DataCard fragment = new DataCard();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putDouble(ARG_PRICE, price);
        args.putString(ARG_IMAGEURL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mDescription = getArguments().getString(ARG_DESCRIPTION);
            mPrice = getArguments().getDouble(ARG_PRICE);
            mImageUrl = getArguments().getString(ARG_IMAGEURL);
        }
    }

    private Bitmap getBitmapFromURL(String imgUrl){
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_card, container, false);
        ImageView image = rootView.findViewById(R.id.image);
        TextView title = rootView.findViewById(R.id.textTitle);
        TextView description = rootView.findViewById(R.id.textDescription);
        TextView price = rootView.findViewById(R.id.textPrice);

        title.setText(mTitle);
        description.setText(mDescription);
        price.setText(Double.toString(mPrice));
        Handler handler = new Handler(Looper.getMainLooper());
        Thread imgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Thread", "Image thread started");
                Bitmap bmp = getBitmapFromURL(mImageUrl);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Thread", "Image bitmap set");
                        image.setImageBitmap(bmp);
                    }
                });
            }
        });
        imgThread.start();

        return rootView;
    }
}