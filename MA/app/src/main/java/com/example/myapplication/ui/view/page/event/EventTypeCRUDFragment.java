package com.example.myapplication.ui.view.page.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.viewmodel.events.EventTypeViewModel;

import java.util.ArrayList;

public class EventTypeCRUDFragment extends Fragment {

    private RecyclerView rvEventTypes;
    private ProgressBar progressBar;
    private Button btnCreateEventType;

    private EventTypeViewModel viewModel;
    private EventTypeAdapter adapter;

    public EventTypeCRUDFragment() {}

    public static EventTypeCRUDFragment newInstance() {
        return new EventTypeCRUDFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_type_c_r_u_d, container, false);

        rvEventTypes = view.findViewById(R.id.rvEventTypes);
        progressBar = view.findViewById(R.id.progressBarEventTypes);
        btnCreateEventType = view.findViewById(R.id.btnCreateEventType);

        viewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);

        adapter = new EventTypeAdapter(new ArrayList<>(), eventType -> {
            if (eventType.getId() == 1) {
                Toast.makeText(requireContext(), "This event type cannot be edited.", Toast.LENGTH_SHORT).show();
                return;
            }
            EventTypeFormFragment fragment = EventTypeFormFragment.newInstance(eventType);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }, eventType -> {
            viewModel.toggleActivation(eventType);
        });

        rvEventTypes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvEventTypes.setAdapter(adapter);

        //pagination
        rvEventTypes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null && lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 2) {
                    viewModel.loadNextPage();
                }
            }
        });

        //observing
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), types -> adapter.updateList(types));
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        btnCreateEventType.setOnClickListener(v -> {
            EventTypeFormFragment fragment = EventTypeFormFragment.newInstance(null);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        //loading first page
        viewModel.fetchFirstPage();

        return view;
    }
}