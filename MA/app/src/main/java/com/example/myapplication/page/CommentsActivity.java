package com.example.myapplication.page;

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
import com.example.myapplication.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.services.RatingService;

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

    private int currentPage = 1; // For pagination
    private final int itemsPerPage = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page_comments);

        ratingService = new RatingService();

        commentsList = findViewById(R.id.comments_list);
        previousPageButton = findViewById(R.id.previousPageButton);
        nextPageButton = findViewById(R.id.nextPageButton);

        previousPageButton.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                fetchComments();
            }
        });

        nextPageButton.setOnClickListener(v -> {
            currentPage++;
            fetchComments();
        });

        fetchComments();
    }

    private void fetchComments() {
        ratingService.getAllRatings().enqueue(new Callback<List<GetRatingDTO>>() {
            @Override
            public void onResponse(Call<List<GetRatingDTO>> call, Response<List<GetRatingDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayComments(response.body());
                } else {
                    Toast.makeText(CommentsActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetRatingDTO>> call, Throwable t) {
                Log.e(TAG, "Error fetching comments", t);
                Toast.makeText(CommentsActivity.this, "Error fetching comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayComments(List<GetRatingDTO> comments) {
        commentsList.removeAllViews();

        int start = (currentPage - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, comments.size());

        for (int i = start; i < end; i++) {
            GetRatingDTO comment = comments.get(i);
            View commentView = getLayoutInflater().inflate(R.layout.comment_item, null);

            TextView offerName = commentView.findViewById(R.id.offer_name);
            TextView authorName = commentView.findViewById(R.id.author_name);
            TextView ratingValue = commentView.findViewById(R.id.rating_value);
            TextView commentText = commentView.findViewById(R.id.comment_text);
            Button approveButton = commentView.findViewById(R.id.approve_button);
            Button deleteButton = commentView.findViewById(R.id.delete_button);

            offerName.setText("Offer Name: " + comment.getOfferName());
            authorName.setText("Author: " + comment.getAuthorName());
            ratingValue.setText("Rating: " + comment.getValue());
            commentText.setText(comment.getComment());

            approveButton.setOnClickListener(v -> approveComment(comment.getId()));

            deleteButton.setOnClickListener(v -> deleteComment(comment.getId()));

            commentsList.addView(commentView);
        }

        previousPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(end < comments.size());
    }

    private void approveComment(int commentId) {
        ratingService.approveRating(commentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CommentsActivity.this, "Comment approved", Toast.LENGTH_SHORT).show();
                    fetchComments();
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
        ratingService.deleteRating(commentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CommentsActivity.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                    fetchComments();
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
}
