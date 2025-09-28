package com.example.myapplication.ui.view.page.events;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.eventDTO.GetEventDetails;
import com.example.myapplication.reviews.ReviewsSectionView;
import com.example.myapplication.data.services.event.EventService;
import com.example.myapplication.data.services.NotificationService;
import com.example.myapplication.data.services.RatingService;
import com.example.myapplication.data.services.user.UsersService;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

    private int eventId;
    private GetEventDetails event;
    private MapView osmMap;

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

                    double latitude = res.getLatitude();
                    double longitude = res.getLongitude();

                    osmMap = findViewById(R.id.osmMap);

                    Configuration.getInstance().setUserAgentValue(getPackageName());

                    osmMap.setMultiTouchControls(true);
                    osmMap.getController().setZoom(14.0);
                    GeoPoint location = new GeoPoint(latitude, longitude);
                    osmMap.getController().setCenter(location);

                    Marker marker = new Marker(osmMap);
                    marker.setPosition(location);
                    marker.setTitle("Event Location");
                    osmMap.getOverlays().add(marker);


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
    @Override
    protected void onDestroy() {
        if (osmMap != null) {
            osmMap.onDetach();
        }
        super.onDestroy();
    }

    private void fetchRatings() {
        reviewsSection.clearReviews();      // Clear old reviews immediately
        reviewsSection.setEventId(eventId); // This triggers loading ratings filtered by eventId
    }

}
