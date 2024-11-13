package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        createEventCard("Diddy party", "Hero of the American government", 100, "https://imgcdn.stablediffusionweb.com/2024/10/3/549dea0c-d027-4e47-ace2-3afaeb12610d.jpg");
        createEventCard("Mango", "Hero of the American government", 100, "https://static.maxi.rs/medias/sys_master/h36/hfd/9058860498974.jpg");
        createEventCard("Baby Gronk vs Rizzler", "Largest boxing match of the century", 100, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRKMZd3ovkDP_rXscWoYyB3ehkMcDQUAoCGw&s");

        createOfferingCard("Baby oil", "Oil up the huzz yn", 10, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTm4VuySLHE4rQOnRVT7wfWjnlDZRdZWNnHIw&s");
        createOfferingCard("Mango", "Finest mango on the market", 5, "https://static.maxi.rs/medias/sys_master/h36/hfd/9058860498974.jpg");
        createOfferingCard("Prime", "Quench your thirst with the new and improved Prime", 10, "https://globaldistribution.rs/wp-content/uploads/2024/05/Prime-Ice-Pop-Hydration-Drink-500ml.jpg");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createEventCard(String title, String description, double price, String imgUrl){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DataCard dc1 = DataCard.newInstance(title, description, price, imgUrl);
        transaction.add(R.id.eventCards, dc1);
        transaction.commit();
    }

    private void createOfferingCard(String title, String description, double price, String imgUrl){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DataCard dc1 = DataCard.newInstance(title, description, price, imgUrl);
        transaction.add(R.id.offeringCards, dc1);
        transaction.commit();
    }
}