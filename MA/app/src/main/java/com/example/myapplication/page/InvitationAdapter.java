package com.example.myapplication.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.invitationDTO.SimpleInvitation;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    private List<SimpleInvitation> invitations;
    private final InvitationActionListener listener;

    public interface InvitationActionListener {
        void onInvitationHandled(SimpleInvitation invitation, String action);
    }

    public InvitationAdapter(List<SimpleInvitation> invitations, InvitationActionListener listener) {
        this.invitations = invitations;
        this.listener = listener;
    }

    public void updateInvitations(List<SimpleInvitation> newInvitations) {
        this.invitations = newInvitations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitation, parent, false);
        return new InvitationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        SimpleInvitation invitation = invitations.get(position);
        holder.bind(invitation);
    }

    @Override
    public int getItemCount() {
        return invitations != null ? invitations.size() : 0;
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, status;
        Button btnAccept, btnDeny;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.invitationTitle);
            description = itemView.findViewById(R.id.invitationDescription);
            status = itemView.findViewById(R.id.invitationStatus);
            btnAccept = itemView.findViewById(R.id.btnAcceptInvitation);
            btnDeny = itemView.findViewById(R.id.btnDenyInvitation);
        }

        void bind(SimpleInvitation invitation) {
            title.setText(invitation.getEvent().getName());
            description.setText(invitation.getEvent().getDescription());
            status.setText(invitation.getStatus());

            // Show buttons only if status is PENDING
            boolean pending = "PENDING".equalsIgnoreCase(invitation.getStatus());
            btnAccept.setVisibility(pending ? View.VISIBLE : View.GONE);
            btnDeny.setVisibility(pending ? View.VISIBLE : View.GONE);

            btnAccept.setOnClickListener(v -> listener.onInvitationHandled(invitation, "ACCEPTED"));
            btnDeny.setOnClickListener(v -> listener.onInvitationHandled(invitation, "DENIED"));
        }
    }
}
