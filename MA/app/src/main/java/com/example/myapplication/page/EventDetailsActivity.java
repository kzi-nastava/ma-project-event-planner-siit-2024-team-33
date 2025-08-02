package com.example.myapplication.page;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.GetEventDetails;
import com.example.myapplication.dto.ratingDTO.EventRatingDTO;
import com.example.myapplication.reviews.ReviewsSectionView;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.NotificationService;
import com.example.myapplication.services.RatingService;
import com.example.myapplication.services.UsersService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

    private int eventId;
    private GetEventDetails event;
    private GoogleMap mMap;

    private TextView tvEventTitle, tvEventDescription, tvOrganizer, tvLocation, tvStartTime, tvEndTime, tvAttendees, tvEventType;
    private ImageView btnFavorite;

    private LinearLayout starContainer;
    private ReviewsSectionView reviewsSection;

    private EventService eventService;
    private RatingService ratingService;
    private NotificationService notificationService;
    private UsersService userService;
    private int currentReviewPage = 1;
    private int totalReviewPages = 1;
    private final int reviewsPerPage = 5;
    private String organizerName = "";
    private String organizerEmail = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        eventService = new EventService();
        ratingService = new RatingService();
        notificationService = new NotificationService();
        userService = new UsersService();

        eventId = getIntent().getIntExtra("eventId", -1);
        if (eventId == -1) {
            Toast.makeText(this, "Invalid event ID.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindViews();
        fetchEventDetails();
        fetchRatings();
    }

    private void bindViews() {
        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventDescription = findViewById(R.id.valueDescription);
        tvOrganizer = findViewById(R.id.tvOrganizer);
        tvLocation = findViewById(R.id.tvLocation);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvAttendees = findViewById(R.id.tvAttendees);
        tvEventType = findViewById(R.id.tvEventType);

        btnFavorite = findViewById(R.id.btnFavorite);
        starContainer = findViewById(R.id.starContainer);
        reviewsSection = findViewById(R.id.reviewsSection);
    }

    private void fetchEventDetails() {
        eventService.getEventDetails(eventId).enqueue(new Callback<GetEventDetails>() {
            @Override
            public void onResponse(Call<GetEventDetails> call, Response<GetEventDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetEventDetails res = response.body();

                    event = res;

                    String organizerFullName = "";
                    if (res.getMinimalOrganizer() != null) {
                        organizerFullName = res.getMinimalOrganizer().getName() + " " + res.getMinimalOrganizer().getSurname();
                        organizerEmail = res.getMinimalOrganizer().getEmail();
                        organizerName = organizerFullName;
                    }

                    tvOrganizer.setText("Organizer: " + organizerFullName);
                    tvEventTitle.setText(res.getName() != null ? res.getName() : "Event Title");
                    tvLocation.setText("Place: " + (res.getPlace() != null ? res.getPlace() : "N/A"));
                    tvAttendees.setText("Attendees: " + res.getNumOfCurrentlyApplied() + " / " + res.getNumOfAttendees());
                    tvEventType.setText("Event Type: " + (res.getMinimalEventType() != null ? res.getMinimalEventType().getName() : "N/A"));
                    tvEventDescription.setText(res.getDescription() != null ? res.getDescription() : "No description available.");

                    // Parse and format times
                    DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
                    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    try {
                        if (res.getDateOfEvent() != null) {
                            LocalDateTime startDateTime = LocalDateTime.parse(res.getDateOfEvent(), isoFormatter);
                            tvStartTime.setText("Start: " + startDateTime.format(displayFormatter));
                        }
                        if (res.getEndOfEvent() != null) {
                            LocalDateTime endDateTime = LocalDateTime.parse(res.getEndOfEvent(), isoFormatter);
                            tvEndTime.setText("End: " + endDateTime.format(displayFormatter));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Map setup
                    double latitude = res.getLatitude();
                    double longitude = res.getLongitude();
                    SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mapContainer, mapFragment)
                            .commit();

                    mapFragment.getMapAsync(googleMap -> {
                        mMap = googleMap;
                        LatLng location = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(location).title("Event Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                    });

                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to load event.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetEventDetails> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Failed to load event.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void fetchRatings() {
        reviewsSection.clearReviews();      // Clear old reviews immediately
        reviewsSection.setEventId(eventId); // This triggers loading ratings filtered by eventId
    }

}
