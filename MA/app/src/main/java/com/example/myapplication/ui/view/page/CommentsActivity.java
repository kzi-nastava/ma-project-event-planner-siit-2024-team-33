package com.example.myapplication.ui.view.page;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.ratingDTO.EventRatingDTO;
import com.example.myapplication.data.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.data.services.RatingService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends Activity {
    private static final String TAG = "CommentsActivity";
    private RatingService ratingService;
    private LinearLayout commentsList;
    private Button previousPageButton;
    private Button nextPageButton;

    private final int itemsPerPage = 10;

    private int combinedCurrentPage = 1;

    private int normalRatingsPage = 0;
    private int eventRatingsPage = 0;

    private final List<GetRatingDTO> allNormalRatings = new ArrayList<>();
    private final List<EventRatingDTO> allEventRatings = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page_comments);

        ratingService = new RatingService();

        commentsList = findViewById(R.id.comments_list);
        previousPageButton = findViewById(R.id.previousPageButton);
        nextPageButton = findViewById(R.id.nextPageButton);

        previousPageButton.setOnClickListener(v -> {
            if (combinedCurrentPage > 1) {
                combinedCurrentPage--;
                fetchComments();
            }
        });

        nextPageButton.setOnClickListener(v -> {
            combinedCurrentPage++;
            fetchComments();
        });

        fetchComments();
    }

    private void fetchComments() {
        int neededItems = combinedCurrentPage * itemsPerPage;

        boolean needMoreNormal = allNormalRatings.size() < neededItems;
        boolean needMoreEvent = allEventRatings.size() < neededItems;

        if (needMoreNormal) {
            ratingService.getAllRatings(normalRatingsPage, itemsPerPage * 2).enqueue(new Callback<PageResponse<GetRatingDTO>>() {
                @Override
                public void onResponse(Call<PageResponse<GetRatingDTO>> call, Response<PageResponse<GetRatingDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        allNormalRatings.addAll(response.body().getContent());
                        normalRatingsPage++;
                        checkAndDisplayCombined();
                    } else {
                        Toast.makeText(CommentsActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                        if (normalRatingsPage > 0) normalRatingsPage--;
                    }
                }

                @Override
                public void onFailure(Call<PageResponse<GetRatingDTO>> call, Throwable t) {
                    Log.e(TAG, "Error fetching comments", t);
                    Toast.makeText(CommentsActivity.this, "Error fetching comments", Toast.LENGTH_SHORT).show();
                    if (normalRatingsPage > 0) normalRatingsPage--;
                }
            });
        }

        if (needMoreEvent) {
            ratingService.getAllEventRatings(eventRatingsPage, itemsPerPage * 2).enqueue(new Callback<PageResponse<EventRatingDTO>>() {
                @Override
                public void onResponse(Call<PageResponse<EventRatingDTO>> call, Response<PageResponse<EventRatingDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        allEventRatings.addAll(response.body().getContent());
                        eventRatingsPage++;
                        checkAndDisplayCombined();
                    } else {
                        Toast.makeText(CommentsActivity.this, "Failed to load event ratings", Toast.LENGTH_SHORT).show();
                        if (eventRatingsPage > 0) eventRatingsPage--;
                    }
                }

                @Override
                public void onFailure(Call<PageResponse<EventRatingDTO>> call, Throwable t) {
                    Log.e(TAG, "Error fetching event ratings", t);
                    Toast.makeText(CommentsActivity.this, "Error fetching event ratings", Toast.LENGTH_SHORT).show();
                    if (eventRatingsPage > 0) eventRatingsPage--;
                }
            });
        }

        if (!needMoreNormal && !needMoreEvent) {
            checkAndDisplayCombined();
        }
    }

    private void checkAndDisplayCombined() {
        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(allNormalRatings);
        combinedList.addAll(allEventRatings);

        combinedList.sort((a, b) -> {
            int idA = a instanceof GetRatingDTO ? ((GetRatingDTO) a).getId() : ((EventRatingDTO) a).getId();
            int idB = b instanceof GetRatingDTO ? ((GetRatingDTO) b).getId() : ((EventRatingDTO) b).getId();
            return Integer.compare(idB, idA);
        });

        int startIndex = (combinedCurrentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, combinedList.size());

        if (startIndex >= combinedList.size() && combinedCurrentPage > 1) {
            combinedCurrentPage--;
            checkAndDisplayCombined();
            return;
        }

        List<Object> pageItems = combinedList.subList(startIndex, endIndex);

        commentsList.removeAllViews();

        for (Object item : pageItems) {
            View commentView = getLayoutInflater().inflate(R.layout.comment_item, null);

            TextView offerName = commentView.findViewById(R.id.offer_name);
            TextView authorName = commentView.findViewById(R.id.author_name);
            TextView ratingValue = commentView.findViewById(R.id.rating_value);
            TextView commentText = commentView.findViewById(R.id.comment_text);
            Button approveButton = commentView.findViewById(R.id.approve_button);
            Button deleteButton = commentView.findViewById(R.id.delete_button);

            if (item instanceof GetRatingDTO) {
                GetRatingDTO comment = (GetRatingDTO) item;
                offerName.setText("Offer Name: " + comment.getOfferName());
                authorName.setText("Author: " + comment.getAuthorName());
                ratingValue.setText("Rating: " + comment.getValue());
                commentText.setText(comment.getComment());

                approveButton.setOnClickListener(v -> approveComment(comment.getId()));
                deleteButton.setOnClickListener(v -> deleteComment(comment.getId()));
            } else if (item instanceof EventRatingDTO) {
                EventRatingDTO comment = (EventRatingDTO) item;
                offerName.setText("Event Name: " + comment.getEventName());
                authorName.setText("Author: " + comment.getAuthorEmail());
                ratingValue.setText("Rating: " + comment.getRatingValue());
                commentText.setText(comment.getComment());

                approveButton.setOnClickListener(v -> approveComment(comment.getId()));
                deleteButton.setOnClickListener(v -> deleteComment(comment.getId()));
            }

            commentsList.addView(commentView);
        }

        previousPageButton.setEnabled(combinedCurrentPage > 1);
        nextPageButton.setEnabled(endIndex < combinedList.size());
    }

    private void approveComment(int commentId) {
        boolean isEventRating = allEventRatings.stream().anyMatch(r -> r.getId() == commentId);

        Call<Void> call;
        if (isEventRating) {
            call = ratingService.approveEventRating(commentId);
        } else {
            call = ratingService.approveRating(commentId);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CommentsActivity.this, "Comment approved", Toast.LENGTH_SHORT).show();
                    resetPaginationAndFetch();
                } else {
                    Toast.makeText(CommentsActivity.this, "Failed to approve comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error approving comment", t);
                Toast.makeText(CommentsActivity.this, "Error approving comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteComment(int commentId) {
        boolean isEventRating = allEventRatings.stream().anyMatch(r -> r.getId() == commentId);

        Call<Void> call;
        if (isEventRating) {
            call = ratingService.deleteEventRating(commentId);
        } else {
            call = ratingService.deleteRating(commentId);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CommentsActivity.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                    resetPaginationAndFetch();
                } else {
                    Toast.makeText(CommentsActivity.this, "Failed to delete comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error deleting comment", t);
                Toast.makeText(CommentsActivity.this, "Error deleting comment", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void resetPaginationAndFetch() {
        combinedCurrentPage = 1;
        normalRatingsPage = 0;
        eventRatingsPage = 0;
        allNormalRatings.clear();
        allEventRatings.clear();
        fetchComments();
    }
}