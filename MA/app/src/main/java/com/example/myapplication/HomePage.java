package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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

import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.services.AuthenticationService;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.OfferService;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.filefilter.ConditionalFileFilter;

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
    private OfferService offerService;
    private List<MinimalEventTypeDTO> eventTypes;
    private List<MinimalEventDTO> events;
    private List<MinimalOfferDTO> offers;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        eventService = new EventService();
        offerService = new OfferService();

        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        loadTop5Events(view);
        loadTop5Offers(view);
        //addingProductCards(view);

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

    private void loadTop5Events(View view) {
        Log.d("EVENTS_API", "Calling GetTop5EventsAuthorized()");

        Call<List<MinimalEventDTO>> call;
        if (AuthenticationService.isLoggedIn()) {
            call = eventService.GetTop5EventsAuthorized();
        } else {
            call = eventService.GetTop5EventsUnauthorized();
        }

        call.enqueue(new Callback<List<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                Log.d("EVENTS_API", "Response received. Success: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    events = response.body();
                    addingEventCards(view);
                } else {
                    Log.e("EVENTS_API", "Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                Log.e("EVENTS_API", "Request failed", t);
            }
        });
    }


    private void loadTop5Offers(View view) {
        Call<List<MinimalOfferDTO>> call;
        if(AuthenticationService.isLoggedIn()){
            call =offerService.getTop5Offers();
        }else{
            call =offerService.GetTop5OffersUnauthentified();
        }
        call.enqueue(new Callback<List<MinimalOfferDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferDTO>> call, Response<List<MinimalOfferDTO>> response) {
                if (response==null)
                    Log.e("RetroFit","response is null");
                if(response.isSuccessful() && response.body()!=null){
                    offers = response.body();
                    addingProductCards(view);
                }
            }

            @Override
            public void onFailure(Call<List<MinimalOfferDTO>> call, Throwable t) {

            }
        });
    }

    private void addingEventCards(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.eventCardsPlace);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        parentLayout.removeAllViews();

        if (events != null) {
            for (MinimalEventDTO event : events) {
                View cardView = inflater.inflate(R.layout.event_card, parentLayout, false);
                Log.d("","ASDSADSADSASDASDASDASDSA ");
                Log.d("",events.toString());
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

    }

    private void addingProductCards(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.offeringCardsPlace);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        parentLayout.removeAllViews();

        if (offers != null) {
            for (MinimalOfferDTO offer : offers) {
                View cardView = inflater.inflate(R.layout.offering_card, parentLayout, false);

                addingMargins(cardView);

                TextView itemTitle = cardView.findViewById(R.id.offering_title);
                TextView itemText = cardView.findViewById(R.id.offering_description);
                ImageView itemImage = cardView.findViewById(R.id.offering_image);
                Button itemButton = cardView.findViewById(R.id.offering_button);



                itemTitle.setText(offer.getName());
                itemText.setText(offer.getDescription());
                itemImage.setImageResource(R.drawable.trumpshot);
                itemButton.setVisibility(View.GONE);

                parentLayout.addView(cardView);
            }

        }
    }

    private void addingMargins(View cardView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int marginInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14,
                getResources().getDisplayMetrics()
        );

        params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        cardView.setLayoutParams(params);
    }
}