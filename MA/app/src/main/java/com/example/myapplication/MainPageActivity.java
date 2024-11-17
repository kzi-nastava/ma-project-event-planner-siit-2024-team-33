package com.example.myapplication;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        addingEventCards();
        addingProductCards();

    }

    private void addingEventCards()
    {
        //parent layout where we add cards
        LinearLayout parentLayout = findViewById(R.id.eventCardsPlace);
        for (int i = 0; i <= 5; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View cardView = inflater.inflate(R.layout.event_card, parentLayout, false);

            addingMargins(cardView);

            TextView itemTitle = cardView.findViewById(R.id.item_title);
            TextView itemText = cardView.findViewById(R.id.item_text);
            ImageView itemImage = cardView.findViewById(R.id.item_image);

            if (i < 5)
            {
                //setting data for cards
                itemTitle.setText("Event Title " + (i + 1));
                itemText.setText("In a quaint little village nestled between rolling hills, there lived a curious cat n...");
                itemImage.setImageResource(R.drawable.trumpshot);
            }
            else {
                itemTitle.setText("+99 more events");
                itemText.setVisibility(View.GONE);
                itemImage.setVisibility(View.GONE);
                cardView.setAlpha(0.7f);
            }

            parentLayout.addView(cardView);
        }
    }

    private void addingProductCards()
    {
        //parent layout where we add cards
        LinearLayout parentLayout = findViewById(R.id.offeringCardsPlace);
        for (int i = 0; i <= 4; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View cardView = inflater.inflate(R.layout.offering_card, parentLayout, false);

            addingMargins(cardView);

            TextView itemTitle = cardView.findViewById(R.id.offering_title);
            TextView itemText = cardView.findViewById(R.id.offering_description);
            ImageView itemImage = cardView.findViewById(R.id.offering_image);
            Button itemButton = cardView.findViewById(R.id.offering_button);

            if (i < 4)
            {
                //setting data for cards
                itemTitle.setText("Offering Title " + (i + 1));
                itemText.setText("In a quaint little village nestled between rolling hills, there lived a curious cat name" +
                        "d Whiskers...");
                itemImage.setImageResource(R.drawable.pic123);
            }
            else {
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
                TypedValue.COMPLEX_UNIT_DIP, //converting to dp
                14, //desired margin in dp
                getResources().getDisplayMetrics() //getting display metrics
        );

        //apply margins
        params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        cardView.setLayoutParams(params);
    }

}