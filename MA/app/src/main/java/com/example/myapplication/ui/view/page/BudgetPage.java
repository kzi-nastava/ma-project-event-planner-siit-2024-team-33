package com.example.myapplication.ui.view.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.view.page.home.component.BudgetItemFragment;
import com.example.myapplication.data.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.dto.budgetDTO.BudgetItemDTO;
import com.example.myapplication.data.dto.budgetDTO.BudgetItemWrapperDTO;
import com.example.myapplication.data.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.data.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.data.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.services.BudgetService;
import com.example.myapplication.data.services.event.EventService;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetPage extends Fragment {

    private final EventService eventService = new EventService();
    private final BudgetService budgetService = new BudgetService();

    private GetBudgetDTO budgetData;

    private Spinner eventSpinner;
    private LinearLayout budgetItemsContainer;
    private Button addCategoryBtn;
    private TextView tvBudget;

    public BudgetPage() {
        // Required empty public constructor
    }

    public static BudgetPage newInstance() {
        BudgetPage fragment = new BudgetPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_page, container, false);

        eventSpinner = view.findViewById(R.id.event_spinner);
        budgetItemsContainer = view.findViewById(R.id.budget_items_container);
        addCategoryBtn = view.findViewById(R.id.add_category_button);
        addCategoryBtn.setOnClickListener(v -> PickCategory());
        tvBudget = view.findViewById(R.id.tv_budget);

        LoadEvents();

        return view;
    }

    public void LoadEvents(){
        eventService.getEventsForOrganizerUpdated().enqueue(new Callback<List<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<MinimalEventDTO> organizerEvents = response.body();

                    ArrayAdapter<MinimalEventDTO> adapter = new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_spinner_item,
                            organizerEvents
                    );

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    eventSpinner.setAdapter(adapter);

                    eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            MinimalEventDTO selected = (MinimalEventDTO) parent.getItemAtPosition(position);
                            LoadBudget(selected.getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else
                    Toast.makeText(getContext(), "Error getting events", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error getting events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoadBudget(Integer eventId){
        budgetItemsContainer.removeAllViews();
        
        budgetService.getEventBudget(eventId).enqueue(new Callback<GetBudgetDTO>() {
            @Override
            public void onResponse(Call<GetBudgetDTO> call, Response<GetBudgetDTO> response) {
                if (response.isSuccessful() && response.body() != null){
                    budgetData = response.body();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    for(BudgetItemDTO item : budgetData.takenItems){
                        BudgetItemWrapperDTO wrap = new BudgetItemWrapperDTO();
                        wrap.eventId = eventId;
                        wrap.item = item;
                        wrap.offers = budgetData.takenOffers.stream().filter(o -> o.categoryId == item.offerCategoryID).collect(Collectors.toList());
                        BudgetItemFragment f = BudgetItemFragment.newInstance(wrap);

                        FrameLayout container = new FrameLayout(getContext());
                        int containerId = View.generateViewId(); // ensures unique ID
                        container.setId(containerId);
                        container.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        container.setPadding(0, 16, 0, 16);

                        f.setOnReloadListener(() -> LoadBudget(eventId));
                        budgetItemsContainer.addView(container);

                        fragmentManager.beginTransaction()
                                .add(containerId, f)
                                .commit();
                    }

                    addCategoryBtn.setVisibility(View.VISIBLE);
                    if(budgetData.recommendedOfferTypes.isEmpty())
                        addCategoryBtn.setVisibility(View.GONE);

                    tvBudget.setText("€"+ budgetData.usedBudget + " / €" + budgetData.maxBudget);
                }
                else
                    Toast.makeText(getContext(), "Error getting budget", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetBudgetDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error getting budget", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void PickCategory(){
        String[] categoryNames = new String[budgetData.recommendedOfferTypes.size()];
        for (int i = 0; i < budgetData.recommendedOfferTypes.size(); i++) {
            categoryNames[i] = budgetData.recommendedOfferTypes.get(i).name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a category");
        builder.setItems(categoryNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MinimalOfferCategoryDTO selectedCategory = budgetData.recommendedOfferTypes.get(which);
                PostBudgetItemDTO data = new PostBudgetItemDTO();
                data.maxBudget = 0.0;
                data.offerCategoryID = selectedCategory.id;

                budgetService.addBudgetItem(budgetData.eventID, data).enqueue(new Callback<MinimalBudgetItemDTO>() {
                    @Override
                    public void onResponse(Call<MinimalBudgetItemDTO> call, Response<MinimalBudgetItemDTO> response) {
                        if(response.isSuccessful())
                            LoadBudget(budgetData.eventID);
                        else
                            Toast.makeText(getContext(), "Error adding category", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MinimalBudgetItemDTO> call, Throwable t) {
                        Toast.makeText(getContext(), "Error adding category", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.show();
    }

}