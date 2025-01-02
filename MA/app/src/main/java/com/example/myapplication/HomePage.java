package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.dto.MinimalEventDTO;
import com.example.myapplication.dto.MinimalEventTypeDTO;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private GestureDetector gestureDetector;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EventService eventService;
    private List<MinimalEventTypeDTO> eventTypes;
    private List<MinimalEventDTO> events;
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        loadTop5Events();

        addingEventCards(view);
        addingProductCards(view);

        return view;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX < 0) {
                    showProfileFragment();
                    return true;
                }
            }
            return false;
        }
    }

    private void showProfileFragment() {
        ShowProfileFragment profileFragment = new ShowProfileFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_right
                )
                .replace(R.id.nav_host_fragment, profileFragment, "ShowProfileFragment")
                .addToBackStack(null)
                .commit();
    }

    private void loadTop5Events() {
        eventService.getTop5Events(2)
                .enqueue(new Callback<List<MinimalEventDTO>>() {
                    @Override
                    public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            events = response.body();
                            addingEventCards(getView());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                        // Handle failure
                    }
                });
    }

    private void addingEventCards(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.eventCardsPlace);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (eventTypes != null) {
            for (MinimalEventDTO event : events) {
                View cardView = inflater.inflate(R.layout.event_card, parentLayout, false);

                addingMargins(cardView);

                TextView itemTitle = cardView.findViewById(R.id.item_title);
                TextView itemText = cardView.findViewById(R.id.item_text);
                ImageView itemImage = cardView.findViewById(R.id.item_image);


                itemTitle.setText(event.getName());
                itemText.setText(event.getDescription());
                itemImage.setImageResource(R.drawable.trumpshot);

                parentLayout.addView(cardView);
            }
        }

        View moreEventsCard = inflater.inflate(R.layout.event_card, parentLayout, false);
        TextView moreEventsTitle = moreEventsCard.findViewById(R.id.item_title);
        moreEventsTitle.setText("+99 more events");
        moreEventsCard.setAlpha(0.7f);
        parentLayout.addView(moreEventsCard);
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