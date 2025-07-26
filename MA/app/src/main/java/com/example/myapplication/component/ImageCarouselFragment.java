package com.example.myapplication.component;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ImageCarouselFragment extends Fragment {
    private static final String ARG_IMAGES = "image_uris";

    public static ImageCarouselFragment newInstance(ArrayList<String> imageDataURIs) {
        ImageCarouselFragment fragment = new ImageCarouselFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGES, imageDataURIs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_carousel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        ArrayList<String> imageURIs = getArguments() != null ? getArguments().getStringArrayList(ARG_IMAGES) : new ArrayList<>();

        viewPager.setAdapter(new ImagePagerAdapter(imageURIs));
    }
}