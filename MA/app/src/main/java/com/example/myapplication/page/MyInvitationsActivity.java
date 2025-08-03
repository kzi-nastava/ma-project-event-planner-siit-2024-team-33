package com.example.myapplication.page;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.invitationDTO.SimpleInvitation;
import com.example.myapplication.services.InvitationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInvitationsActivity extends AppCompatActivity implements InvitationAdapter.InvitationActionListener {

    private RecyclerView recyclerView;
    private InvitationAdapter adapter;
    private Button btnPrev, btnNext, btnDeleteHandled;

    private List<SimpleInvitation> invitations = new ArrayList<>();
    private int currentPage = 0;
    private final int pageSize = 3;

    private InvitationService invitationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invitations);

        recyclerView = findViewById(R.id.recyclerInvitations);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnDeleteHandled = findViewById(R.id.btnDeleteHandled);

        invitationService = new InvitationService();

        adapter = new InvitationAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                updatePagination();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < getTotalPages() - 1) {
                currentPage++;
                updatePagination();
            }
        });

        btnDeleteHandled.setOnClickListener(v -> {
            // Remove all except PENDING
            invitations.removeIf(inv -> !"PENDING".equalsIgnoreCase(inv.getStatus()));
            currentPage = 0;
            updatePagination();
            Toast.makeText(this, "Handled invitations deleted", Toast.LENGTH_SHORT).show();
        });

        loadInvitations();
    }

    private void loadInvitations() {
        invitationService.getMyPendingInvitations(new Callback<List<SimpleInvitation>>() {
            @Override
            public void onResponse(Call<List<SimpleInvitation>> call, Response<List<SimpleInvitation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    invitations = response.body();
                    currentPage = 0;
                    updatePagination();
                } else {
                    Toast.makeText(MyInvitationsActivity.this, "Failed to load invitations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SimpleInvitation>> call, Throwable t) {
                Toast.makeText(MyInvitationsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MyInvitationsActivity", "Error loading invitations", t);
            }
        });

    }

    private void updatePagination() {
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, invitations.size());
        List<SimpleInvitation> pageItems = invitations.subList(start, end);
        adapter.updateInvitations(pageItems);

        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled(currentPage < getTotalPages() - 1);
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) invitations.size() / pageSize);
    }

    // Called from adapter when Accept or Deny clicked
    @Override
    public void onInvitationHandled(SimpleInvitation invitation, String action) {
        // Call updateInvitationStatus via InvitationService
        invitationService.updateInvitationStatus(invitation.getId(), action, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyInvitationsActivity.this, action + " successful", Toast.LENGTH_SHORT).show();
                    // Refresh list
                    loadInvitations();
                } else {
                    Toast.makeText(MyInvitationsActivity.this, action + " failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyInvitationsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
