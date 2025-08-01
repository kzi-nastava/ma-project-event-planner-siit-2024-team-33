package com.example.myapplication.reviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.ratingDTO.EventRatingDTO;
import com.example.myapplication.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.dto.ratingDTO.PostRatingDTO;
import com.example.myapplication.reports.ReportDialog;
import com.example.myapplication.services.RatingService;
import com.example.myapplication.services.ReportService;

import java.util.ArrayList;
import java.util.List;

public class ReviewsSectionView extends LinearLayout {
    private int offerId = -1;
    private int eventId = -1;

    private RatingService ratingService = new RatingService();
    private RecyclerView reviewList;
    private ReviewAdapter adapter;
    private List<ImageView> stars = new ArrayList<>();
    private EditText commentText;
    private Button submitButton;
    private int selectedStars = 0;

    public ReviewsSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.reviews_section, this, true);

        reviewList = findViewById(R.id.review_list);
        commentText = findViewById(R.id.comment_text);
        submitButton = findViewById(R.id.submit_review);

        stars.add(findViewById(R.id.star1));
        stars.add(findViewById(R.id.star2));
        stars.add(findViewById(R.id.star3));
        stars.add(findViewById(R.id.star4));
        stars.add(findViewById(R.id.star5));

        setupStarSelector();

        adapter = new ReviewAdapter(new ArrayList<>(), new ReviewAdapter.OnReviewActionListener() {
            @Override
            public void onReport(GetRatingDTO review) {
                ReportDialog reportDialog = new ReportDialog(
                        getContext(),
                        new ReportService(),
                        review.getAuthorId()
                );
                reportDialog.show();
            }

            @Override
            public void onChat(GetRatingDTO review) {
                Toast.makeText(getContext(), "Chat with majmuneeeee", Toast.LENGTH_SHORT).show();
            }
        });

        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewList.setAdapter(adapter);

        submitButton.setOnClickListener(v -> submitReview());
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
        this.eventId = -1;
        loadOfferRatings();
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        this.offerId = -1;
        loadEventRatings();
    }

    private void loadOfferRatings() {
        if (offerId == -1) return;

        ratingService.getRatingsByOffer(offerId).enqueue(new retrofit2.Callback<List<GetRatingDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<List<GetRatingDTO>> call, retrofit2.Response<List<GetRatingDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateReviews(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load ratings. Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<GetRatingDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load ratings: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEventRatings() {
        if (eventId == -1) return;

        ratingService.getEventRatingsByEvent(eventId).enqueue(new retrofit2.Callback<List<EventRatingDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<List<EventRatingDTO>> call, retrofit2.Response<List<EventRatingDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GetRatingDTO> converted = convertEventRatings(response.body());
                    adapter.updateReviews(converted);
                } else {
                    Toast.makeText(getContext(), "Failed to load event ratings. Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<EventRatingDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load event ratings: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<GetRatingDTO> convertEventRatings(List<EventRatingDTO> eventRatings) {
        List<GetRatingDTO> result = new ArrayList<>();
        for (EventRatingDTO e : eventRatings) {
            GetRatingDTO r = new GetRatingDTO();
            r.setId(e.getId());
            r.setValue(e.getRatingValue());
            r.setComment(e.getComment());
            r.setAccepted(e.getAccepted());
            r.setDeleted(e.getIsDeleted());
            r.setAuthorId(e.getAuthorId());
            r.setAuthorName(e.getAuthorEmail());
            // If needed, map eventId or other fields
            result.add(r);
        }
        return result;
    }

    private void setupStarSelector() {
        for (int i = 0; i < stars.size(); i++) {
            final int index = i;
            stars.get(i).setOnClickListener(v -> {
                selectedStars = index + 1;
                updateStarColors();
            });
        }
    }

    private void updateStarColors() {
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setColorFilter(
                    i < selectedStars ? Color.parseColor("#FFD700") : Color.parseColor("#4DFFFFFF")
            );
        }
    }

    private void submitReview() {
        String comment = commentText.getText().toString();
        if (selectedStars == 0 || comment.isEmpty()) {
            Toast.makeText(getContext(), "Please select stars and comment", Toast.LENGTH_SHORT).show();
            return;
        }

        if (offerId == -1 && eventId == -1) {
            Toast.makeText(getContext(), "Offer or Event doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }

        if (offerId != -1) {
            PostRatingDTO dto = new PostRatingDTO();
            dto.setValue(selectedStars);
            dto.setComment(comment);

            ratingService.submitRating(dto, offerId).enqueue(new retrofit2.Callback<GetRatingDTO>() {
                @Override
                public void onResponse(retrofit2.Call<GetRatingDTO> call, retrofit2.Response<GetRatingDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Rating submitted!", Toast.LENGTH_SHORT).show();
                        commentText.setText("");
                        selectedStars = 0;
                        updateStarColors();
                        loadOfferRatings();
                    } else {
                        Toast.makeText(getContext(), "Failed to submit rating. Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<GetRatingDTO> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to submit rating: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            EventRatingDTO dto = new EventRatingDTO();
            dto.setRatingValue(selectedStars);
            dto.setComment(comment);

            ratingService.submitEventRating(dto, eventId).enqueue(new retrofit2.Callback<EventRatingDTO>() {
                @Override
                public void onResponse(retrofit2.Call<EventRatingDTO> call, retrofit2.Response<EventRatingDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Event rating submitted!", Toast.LENGTH_SHORT).show();
                        commentText.setText("");
                        selectedStars = 0;
                        updateStarColors();
                        loadEventRatings();
                    } else {
                        Toast.makeText(getContext(), "Failed to submit event rating. Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<EventRatingDTO> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to submit event rating: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
