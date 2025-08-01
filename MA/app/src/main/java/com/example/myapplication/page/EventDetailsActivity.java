package com.example.myapplication.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.dto.eventDTO.GetEventDetails;
import com.example.myapplication.dto.ratingDTO.EventRatingDTO;
import com.example.myapplication.models.Rating;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.NotificationService;
import com.example.myapplication.services.RatingService;
import com.example.myapplication.services.UsersService;
import com.example.myapplication.reviews.ReviewsSectionView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {

    private int eventId;
    private GetEventDetails event;

    private TextView tvEventTitle, tvEventDescription, tvOrganizer, tvLocation, tvStartTime, tvEndTime, tvAttendees, tvEventType;
    private ImageView btnFavorite;

    private LinearLayout starContainer;
    private ReviewsSectionView reviewsSection;

    private int selectedStars = 0;
    private boolean isFavorite = false;
    private boolean isJoined = false;

    private EventService eventService;
    private RatingService ratingService;
    private NotificationService notificationService;
    private UsersService userService;

    private String organizerName = "";
    private String organizerEmail = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        // Initialize services with no-arg constructors
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
        checkFavoriteStatus();

        // Removed button listeners here — handled inside ReviewsSectionView now

        setupStarRating();
    }

    private void bindViews() {
        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventDescription = findViewById(R.id.tvDescription);
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

                    // Use the fields from GetEventDetails properly:
                    String organizerFullName = "";
                    if (res.getMinimalOrganizer() != null) {
                        organizerFullName = res.getMinimalOrganizer().getName() + " " + res.getMinimalOrganizer().getSurname();
                    }
                    tvOrganizer.setText("Organizer: " + organizerFullName);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    String startTimeStr = res.getDateOfEvent() != null ? res.getDateOfEvent().format(formatter) : "N/A";
                    String endTimeStr = res.getEndOfEvent() != null ? res.getEndOfEvent().format(formatter) : "N/A";
                    tvStartTime.setText("Start: " + startTimeStr);
                    tvEndTime.setText("End: " + endTimeStr);

                    tvAttendees.setText("Attendees: " + res.getNumOfCurrentlyApplied() + " / " + res.getNumOfAttendees());

                    String eventTypeName = (res.getMinimalEventType() != null) ? res.getMinimalEventType().getName() : "N/A";
                    tvEventType.setText("Event Type: " + eventTypeName);

                    organizerEmail = (res.getMinimalOrganizer() != null) ? res.getMinimalOrganizer().getEmail() : "";
                    organizerName = organizerFullName;

                    // You can set other UI elements similarly...

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
        ratingService.getEventRatingsByEvent(eventId).enqueue(new Callback<List<EventRatingDTO>>() {
            @Override
            public void onResponse(Call<List<EventRatingDTO>> call, Response<List<EventRatingDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EventRatingDTO> ratings = response.body();
                    // convert or pass to your reviewsSection here
                    reviewsSection.setReviews(ratings);
                } else {
                    Toast.makeText(EventDetailsActivity.this, "Failed to load reviews.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventRatingDTO>> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Failed to load reviews.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFavoriteStatus() {
        eventService.isFavorite(eventId, favorite -> {
            isFavorite = favorite;
            btnFavorite.setImageResource(favorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        });
    }

    private void toggleFavorite() {
        if (isFavorite) {
            eventService.removeFavorite(eventId, () -> {
                isFavorite = false;
                btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            });
        } else {
            eventService.addFavorite(eventId, () -> {
                isFavorite = true;
                btnFavorite.setImageResource(R.drawable.ic_favorite);
            });
        }
    }

    private void setupStarRating() {
        for (int i = 1; i <= 5; i++) {
            final int starValue = i;
            ImageView star = new ImageView(this);
            star.setImageResource(R.drawable.ic_star_outline);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
            star.setLayoutParams(params);
            star.setPadding(4, 4, 4, 4);
            star.setOnClickListener(v -> {
                selectedStars = starValue;
                refreshStars();
            });
            starContainer.addView(star);
        }
    }

    private void refreshStars() {
        for (int i = 0; i < starContainer.getChildCount(); i++) {
            ImageView star = (ImageView) starContainer.getChildAt(i);
            star.setImageResource(i < selectedStars ? R.drawable.ic_star : R.drawable.ic_star_outline);
        }
    }
}
