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
import com.example.myapplication.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.dto.ratingDTO.PostRatingDTO;
import com.example.myapplication.models.Rating;
import com.example.myapplication.services.RatingService;


import java.util.ArrayList;
import java.util.List;

public class ReviewsSectionView extends LinearLayout {
    private int offerId = -1;
    private RatingService ratingService = new RatingService();
    private RecyclerView reviewList;
    private ReviewAdapter adapter;
    private List<ImageView> stars = new ArrayList<>();
    private EditText commentText;
    private Button submitButton;
    private int selectedStars = 0;

    public void setOfferId(int offerId) {
        this.offerId = offerId;
        loadRatings();
    }

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
                Toast.makeText(getContext(), "Reporting " + review.getAuthorName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChat(GetRatingDTO review) {
                Toast.makeText(getContext(), "Chat with " + review.getAuthorName(), Toast.LENGTH_SHORT).show();
            }
        });

        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewList.setAdapter(adapter);

        submitButton.setOnClickListener(v -> submitReview());
        loadRatings();
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

        if (offerId == -1) {
            Toast.makeText(getContext(), "Offer doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }

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
                    loadRatings();
                } else {
                    Toast.makeText(getContext(), "Failed to submit rating. Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<GetRatingDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to submit rating: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRatings() {
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

    private GetRatingDTO createDummy(int id, String offer, int authorId, String name, int value, String comment) {
        GetRatingDTO dto = new GetRatingDTO();
        try {
            java.lang.reflect.Field f;

            f = GetRatingDTO.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(dto, id);

            f = GetRatingDTO.class.getDeclaredField("offerName");
            f.setAccessible(true);
            f.set(dto, offer);

            f = GetRatingDTO.class.getDeclaredField("authorId");
            f.setAccessible(true);
            f.set(dto, authorId);

            f = GetRatingDTO.class.getDeclaredField("authorName");
            f.setAccessible(true);
            f.set(dto, name);

            f = GetRatingDTO.class.getDeclaredField("value");
            f.setAccessible(true);
            f.set(dto, value);

            f = GetRatingDTO.class.getDeclaredField("comment");
            f.setAccessible(true);
            f.set(dto, comment);

            f = GetRatingDTO.class.getDeclaredField("isAccepted");
            f.setAccessible(true);
            f.set(dto, true);

            f = GetRatingDTO.class.getDeclaredField("isDeleted");
            f.setAccessible(true);
            f.set(dto, false);
        } catch (Exception ignored) {}
        return dto;
    }
}
