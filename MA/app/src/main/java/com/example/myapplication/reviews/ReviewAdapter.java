package com.example.myapplication.reviews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.ratingDTO.GetRatingDTO;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<GetRatingDTO> reviews;
    private OnReviewActionListener listener;

    public interface OnReviewActionListener {
        void onReport(GetRatingDTO review);
    }

    public ReviewAdapter(List<GetRatingDTO> reviews, OnReviewActionListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        GetRatingDTO review = reviews.get(position);
        holder.author.setText(review.getAuthorName());
        holder.comment.setText(review.getComment());

        // Setup stars
        holder.starsLayout.removeAllViews();
        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(holder.itemView.getContext());
            star.setImageResource(R.drawable.ic_star);
            star.setColorFilter(i <= (review.getValue() == null ? 0 : review.getValue()) ?
                    Color.parseColor("#FFD700") : Color.parseColor("#4DFFFFFF"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
            star.setLayoutParams(params);
            holder.starsLayout.addView(star);
        }

        holder.itemView.setOnLongClickListener(v -> {
            listener.onReport(reviews.get(position));
            return true;
        });

        holder.moreButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.moreButton);
            popup.getMenu().add("Report").setOnMenuItemClickListener(item -> {
                listener.onReport(review);
                return true;
            });
            popup.getMenu().add("Chat").setOnMenuItemClickListener(item -> {
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author, comment;
        LinearLayout starsLayout;
        ImageButton moreButton;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author);
            comment = itemView.findViewById(R.id.review_text);
            starsLayout = itemView.findViewById(R.id.review_stars);
            moreButton = itemView.findViewById(R.id.more_button);
        }
    }

    public void updateReviews(List<GetRatingDTO> newReviews) {
        this.reviews.clear();
        this.reviews.addAll(newReviews);
        notifyDataSetChanged();
    }
}
