package com.example.myapplication.ui.view.page.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.eventDTO.GetEventDetails;
import com.example.myapplication.data.services.event.EventService;
import com.example.myapplication.reviews.ReviewsSectionView;
import com.example.myapplication.ui.viewmodel.events.EventDetailsViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsFragment extends Fragment {

    private int eventId;
    private MapView osmMap;

    private TextView tvEventTitle, tvEventDescription, tvOrganizer, tvLocation, tvStartTime, tvEndTime, tvAttendees, tvEventType;
    private Button btnFavorite, btnPdf;
    private LinearLayout starContainer;
    private ReviewsSectionView reviewsSection;
    private EventDetailsViewModel viewModel;

    public EventDetailsFragment() {
    }

    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEventTitle = view.findViewById(R.id.tvEventTitle);
        tvEventDescription = view.findViewById(R.id.valueDescription);
        tvOrganizer = view.findViewById(R.id.tvOrganizer);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvEndTime = view.findViewById(R.id.tvEndTime);
        tvAttendees = view.findViewById(R.id.tvAttendees);
        tvEventType = view.findViewById(R.id.tvEventType);

        starContainer = view.findViewById(R.id.starContainer);
        reviewsSection = view.findViewById(R.id.reviewsSection);
        osmMap = view.findViewById(R.id.osmMap);

        btnPdf = view.findViewById(R.id.btnPdf);

        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }
        if (eventId == -1) {
            Toast.makeText(getContext(), "Invalid event ID", Toast.LENGTH_LONG).show();
            return;
        }

        viewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);

        //obeserving fetching data
        viewModel.getEventDetailsLiveData().observe(getViewLifecycleOwner(), this::populateEventDetails);
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), msg ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
        );
        //starting fetching
        viewModel.fetchEventDetails(eventId);
        fetchReviews();

        btnPdf.setOnClickListener(v -> {
            EventService eventService = new EventService();
            eventService.getEventDetailsPdf(eventId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            // Save to cache
                            File pdfFile = new File(requireContext().getCacheDir(), "event-details.pdf");
                            FileOutputStream fos = new FileOutputStream(pdfFile);
                            fos.write(response.body().bytes());
                            fos.close();

                            // Open with external PDF viewer
                            Uri pdfUri = FileProvider.getUriForFile(
                                    requireContext(),
                                    requireContext().getPackageName() + ".fileprovider",
                                    pdfFile
                            );

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(pdfUri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            startActivity(intent);

                        } catch (Exception e) {
                            Toast.makeText(requireContext(), "Error opening PDF", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to download PDF", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    private void populateEventDetails(GetEventDetails res) {
        if (res == null) return;

        String organizerFullName = "";
        if (res.getMinimalOrganizer() != null) {
            organizerFullName = res.getMinimalOrganizer().getName() + " " +
                    res.getMinimalOrganizer().getSurname();
        }

        tvOrganizer.setText("Organizer: " + organizerFullName);
        tvEventTitle.setText(res.getName() != null ? res.getName() : "Event Title");
        tvLocation.setText("Place: " + (res.getPlace() != null ? res.getPlace() : "N/A"));
        tvAttendees.setText("Attendees: " + res.getNumOfCurrentlyApplied() + " / " + res.getNumOfAttendees());
        tvEventType.setText("Event Type: " + (res.getMinimalEventType() != null ? res.getMinimalEventType().getName() : "N/A"));
        tvEventDescription.setText(res.getDescription() != null ? res.getDescription() : "No description available.");

        // Format times
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

        // Setup map
        double latitude = res.getLatitude();
        double longitude = res.getLongitude();

        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
        osmMap.setMultiTouchControls(true);
        osmMap.getController().setZoom(14.0);

        GeoPoint location = new GeoPoint(latitude, longitude);
        osmMap.getController().setCenter(location);

        Marker marker = new Marker(osmMap);
        marker.setPosition(location);
        marker.setTitle("Event Location");
        osmMap.getOverlays().add(marker);
    }


    private void fetchReviews() {
        reviewsSection.clearReviews();
        reviewsSection.setEventId(eventId);
    }

    @Override
    public void onDestroyView() {
        if (osmMap != null) {
            osmMap.onDetach();
        }
        super.onDestroyView();
    }
}