package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Call methods to add event and product cards after inflating the layout
        addingEventCards(view);
        addingProductCards(view);

        return view;
    }

    private void addingEventCards(View view) {
        //parent layout where we add cards
        LinearLayout parentLayout = view.findViewById(R.id.eventCardsPlace);
        for (int i = 0; i <= 5; i++) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View cardView = inflater.inflate(R.layout.event_card, parentLayout, false);

            addingMargins(cardView);

            TextView itemTitle = cardView.findViewById(R.id.item_title);
            TextView itemText = cardView.findViewById(R.id.item_text);
            ImageView itemImage = cardView.findViewById(R.id.item_image);

            if (i < 5) {
                //setting data for cards
                itemTitle.setText("Event Title " + (i + 1));
                itemText.setText("In a quaint little village nestled between rolling hills, there lived a curious cat n...");
                itemImage.setImageResource(R.drawable.trumpshot);
            } else {
                itemTitle.setText("+99 more events");
                itemText.setVisibility(View.GONE);
                itemImage.setVisibility(View.GONE);
                cardView.setAlpha(0.7f);
            }

            parentLayout.addView(cardView);
        }
    }

    private void addingProductCards(View view) {
        //parent layout where we add cards
        LinearLayout parentLayout = view.findViewById(R.id.offeringCardsPlace);
        for (int i = 0; i <= 4; i++) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View cardView = inflater.inflate(R.layout.offering_card, parentLayout, false);

            addingMargins(cardView);

            TextView itemTitle = cardView.findViewById(R.id.offering_title);
            TextView itemText = cardView.findViewById(R.id.offering_description);
            ImageView itemImage = cardView.findViewById(R.id.offering_image);
            Button itemButton = cardView.findViewById(R.id.offering_button);

            if (i < 4) {
                // Setting data for cards
                itemTitle.setText("Offering Title " + (i + 1));
                itemText.setText("In a quaint little village nestled between rolling hills, there lived a curious cat named Whiskers...");
                itemImage.setImageResource(R.drawable.pic123);
            } else {
                itemTitle.setText("+99 more offerings");
                itemText.setVisibility(View.GONE);
                itemImage.setVisibility(View.GONE);
                itemButton.setVisibility(View.GONE);
                cardView.setAlpha(0.7f);
            }

            parentLayout.addView(cardView);
        }
    }

    private void addingMargins(View cardView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Set the desired margin (in pixels)
        int marginInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, // converting to dp
                14, // desired margin in dp
                getResources().getDisplayMetrics() // getting display metrics
        );

        // Apply margins
        params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        cardView.setLayoutParams(params);
    }
}