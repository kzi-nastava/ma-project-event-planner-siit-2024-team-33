package com.example.myapplication.ui.view.page.offers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.view.page.provider.ProviderDetailsFragment;
import com.example.myapplication.ui.view.page.home.component.ImageCarouselFragment;
import com.example.myapplication.ui.view.dialog.ProductBookingDialog;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.dto.productDTO.GetProductDTO;
import com.example.myapplication.data.models.Availability;
import com.example.myapplication.reviews.ReviewsSectionView;
import com.example.myapplication.data.services.FavoritesService;
import com.example.myapplication.data.services.ProductService;
import com.example.myapplication.data.services.user.UsersService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment {
    private ReviewsSectionView reviewsSection;
    private final ProductService productService = new ProductService();
    private final UsersService usersService = new UsersService();
    private final FavoritesService favoritesService = new FavoritesService();

    private Integer productId;
    private GetProductDTO product;
    private Boolean isFavorite;


    TextView tvProductName;
    TextView valueProvider;
    TextView valueCategory;
    TextView valuePrice;
    TextView valueDiscount;
    TextView valueAvailability;
    TextView valueDescription;
    LinearLayout containerValidTypes;

    Button visitProvider;
    Button editBtn;
    Button deleteBtn;
    Button bookBtn;

    ImageButton favoriteButton;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    public static ProductDetailsFragment newInstance(Integer serviceId) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id", serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        tvProductName = view.findViewById(R.id.tvProductName);
        valueProvider = view.findViewById(R.id.valueProvider);
        valueCategory = view.findViewById(R.id.valueCategory);
        valuePrice = view.findViewById(R.id.valuePrice);
        valueDiscount = view.findViewById(R.id.valueDiscount);
        valueAvailability = view.findViewById(R.id.valueAvailability);
        valueDescription = view.findViewById(R.id.valueDescription);
        containerValidTypes = view.findViewById(R.id.containerValidTypes);
        favoriteButton = view.findViewById(R.id.btnFavorite);
        favoriteButton.setOnClickListener(v -> {toggleIsFavorite();});
        visitProvider = view.findViewById(R.id.btnVisit);
        editBtn = view.findViewById(R.id.btnEditProduct);
        deleteBtn = view.findViewById(R.id.btnDeleteProduct);
        bookBtn = view.findViewById(R.id.btnBook);
        bookBtn.setOnClickListener(v -> showBookingDialog());
        bookBtn.setEnabled(false);
        reviewsSection = view.findViewById(R.id.reviewsSection);

        loadData();

        return view;
    }

    public void loadData(){
        productService.getProductDetails(productId).enqueue(new Callback<GetProductDTO>() {
            @Override
            public void onResponse(Call<GetProductDTO> call, Response<GetProductDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
                    product = response.body();
                    updateTextBoxes();
                    bookBtn.setEnabled(true);
                    visitProvider.setOnClickListener(v -> {
                        Fragment f = ProviderDetailsFragment.newInstance(product.providerId);
                        FragmentTransaction transaction = requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction();

                        transaction.replace(R.id.nav_host_fragment, f);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
                }
                else
                    Toast.makeText(getContext(), "Error loading Service :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetProductDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading Service :(", Toast.LENGTH_SHORT).show();
            }
        });
        loadIsFavorite();
    }

    public void loadIsFavorite(){
        favoritesService.isOfferFavorite(productId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body())
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    else
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    isFavorite = response.body();
                }
                else
                    Toast.makeText(getContext(), "Error loading favorite :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading favorite :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toggleIsFavorite(){
        Callback<Void> reloadCallback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadIsFavorite();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error toggling favorite :(", Toast.LENGTH_SHORT).show();
            }
        };

        if(isFavorite)
            favoritesService.removeOfferFromFavorites(productId).enqueue(reloadCallback);
        else
            favoritesService.addOfferToFavorites(productId).enqueue(reloadCallback);
    }

    public void updateTextBoxes(){
        tvProductName.setText(product.name);
        valueProvider.setText(product.providerName);
        valueCategory.setText(product.category != null ? product.category.name : "—");
        valuePrice.setText(String.format("€%.2f", product.price));
        valueAvailability.setText(product.availability == Availability.AVAILABLE ? "Available" : "Unavailable");
        valueDiscount.setText(String.format("€%.2f", product.discount));
        valueDescription.setText(product.description != null ? product.description : "No description provided");

        containerValidTypes.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (MinimalEventTypeDTO event : product.validEvents) {
            TextView chip = new TextView(getContext());
            chip.setText(event.getName());
            chip.setPadding(24, 12, 24, 12);
            chip.setTextColor(getResources().getColor(R.color.nonchalant_blue));
            chip.setBackgroundResource(R.drawable.rounded_corners); // Use drawable with rounded bg
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(8, 0, 8, 0);
            chip.setLayoutParams(lp);
            chip.setTextSize(20.0f);
            containerValidTypes.addView(chip);
        }

        ImageCarouselFragment carouselFrag = ImageCarouselFragment.newInstance(new ArrayList<>(product.picturesDataURI));

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.carouselContainer, carouselFrag)
                .commit();
        reviewsSection.setOfferId(productId);
    }

    private void showBookingDialog() {
        if (product == null) {
            Toast.makeText(getContext(), "Product data incomplete.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductBookingDialog dialog = ProductBookingDialog.newInstance(productId);
        dialog.show(getParentFragmentManager(), "bookingDialog");
    }
}