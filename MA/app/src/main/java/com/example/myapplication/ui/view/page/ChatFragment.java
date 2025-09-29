package com.example.myapplication.ui.view.page;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.chatDTO.ChatContactDTO;
import com.example.myapplication.data.dto.chatDTO.MessageDTO;
import com.example.myapplication.data.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.services.ChatService;
import com.example.myapplication.data.services.ChatWebsocketService;
import com.example.myapplication.data.services.user.UsersService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    public class ChatContactAdapter extends ArrayAdapter<ChatContactDTO> {

        public ChatContactAdapter(@NonNull Context context, List<ChatContactDTO> contacts) {
            super(context, 0, contacts);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ChatContactDTO contact = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_contact, parent, false);
            }

            TextView usernameText = convertView.findViewById(R.id.usernameText);
            TextView emailText = convertView.findViewById(R.id.emailText);

            if (contact != null) {
                usernameText.setText(contact.isBlocked != null && contact.isBlocked ? "Blocked user" : contact.username);
                emailText.setText(contact.email);
            }

            return convertView;
        }
    }

    public class MessageAdapter extends ArrayAdapter<MessageDTO> {

        public MessageAdapter(@NonNull Context context, List<MessageDTO> contacts) {
            super(context, 0, contacts);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            MessageDTO messageDTO = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_bubble, parent, false);
            }

            LinearLayout container = convertView.findViewById(R.id.msg_container);
            TextView msgText = convertView.findViewById(R.id.msg_text);

            msgText.setText(messageDTO.message);

            if (selectedContact != null && !selectedContact.email.equals(messageDTO.senderEmail)) {
                ViewCompat.setBackgroundTintList(
                        msgText,
                        ContextCompat.getColorStateList(getContext(), R.color.nonchalant_blue)
                );
                msgText.setTextColor(ColorStateList.valueOf(Color.WHITE));
                container.setGravity(Gravity.END);
            } else {
                container.setGravity(Gravity.START);
                msgText.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                msgText.setTextColor(ColorStateList.valueOf(Color.BLACK));
            }

            return convertView;
        }
    }


    private ChatWebsocketService chatWebsocketService = ChatWebsocketService.getInstance();
    private ChatService chatService = new ChatService();

    private ListView contactListView;
    private ListView messageContainer;
    private EditText messageEditText;
    private Button sendButton;
    private LinearLayout messageInputLayout;
    private TextView chatHeader;
    private Button backButton;

    private List<ChatContactDTO> contacts = new ArrayList<>();
    ChatContactAdapter adapter;
    private ChatContactDTO selectedContact = null;

    private List<MessageDTO> messages = new ArrayList<>();
    MessageAdapter msgAdapter;
    private Button blockButton;
    private boolean isBlocked = false;
    private UsersService userService = new UsersService();


    private boolean messagesAreOpen = false;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        contactListView = view.findViewById(R.id.contactListView);
        messageContainer = view.findViewById(R.id.messageContainer);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        backButton = view.findViewById(R.id.backBtn);
        messageInputLayout = view.findViewById(R.id.messageInputLayout);
        chatHeader = view.findViewById(R.id.chatHeader);

        adapter = new ChatContactAdapter(getContext(), contacts);
        contactListView.setAdapter(adapter);
        blockButton = view.findViewById(R.id.blockButton);

        contactListView.setOnItemClickListener((parent, view1, position, id) -> {
            selectedContact = contacts.get(position);
            OpenMessages();
        });


        chatWebsocketService.getMessages().observe(getViewLifecycleOwner(),
                messageDTO -> MessageReceived(messageDTO)
        );
        msgAdapter = new MessageAdapter(getContext(), messages);
        messageContainer.setAdapter(msgAdapter);

        sendButton.setOnClickListener(v -> {
            String text = messageEditText.getText().toString().trim();
            if (!text.isEmpty()) {
                SendMessage(text);
                messageEditText.setText("");
            }
        });

        backButton.setOnClickListener(view1 -> CloseMessages());

        FetchContacts();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if(messagesAreOpen)
                            CloseMessages();
                        else{
                            setEnabled(false);
                            requireActivity().getOnBackPressedDispatcher().onBackPressed();
                        }
                    }
                });

        chatWebsocketService.getOpenChatTarget().observe(getViewLifecycleOwner(),
                chatContactDTO -> {selectedContact = chatContactDTO; OpenMessages();}
                );

        return view;
    }

    private void FetchContacts(){
        chatService.getContacts().enqueue(new Callback<List<ChatContactDTO>>() {
            @Override
            public void onResponse(Call<List<ChatContactDTO>> call, Response<List<ChatContactDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    contacts.clear();
                    contacts.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ChatContactDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void OpenMessages(){
        messagesAreOpen = true;
        chatHeader.setText("Chat with " + selectedContact.username);

        contactListView.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        messageInputLayout.setVisibility(View.VISIBLE);
        messageContainer.setVisibility(View.VISIBLE);

        blockButton.setVisibility(View.VISIBLE);

        userService.getBlockedUsers().enqueue(new Callback<List<GetUserDTO>>() {
            @Override
            public void onResponse(Call<List<GetUserDTO>> call, Response<List<GetUserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean found = false;
                    for (GetUserDTO u : response.body()) {
                        if (u.getEmail().equals(selectedContact.email)) {
                            found = true;
                            break;
                        }
                    }
                    isBlocked = found;
                    blockButton.setText(isBlocked ? "Unblock" : "Block");
                } else {
                    isBlocked = false;
                    blockButton.setText("Block");
                }
            }

            @Override
            public void onFailure(Call<List<GetUserDTO>> call, Throwable t) {
                isBlocked = false;
                blockButton.setText("Block");
            }
        });

        blockButton.setOnClickListener(v -> toggleBlock());
        FetchMessages();
    }


    private void CloseMessages(){
        messagesAreOpen = false;
        chatHeader.setText("Chat");
        contactListView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        messageInputLayout.setVisibility(View.GONE);
        messageContainer.setVisibility(View.GONE);
        blockButton.setVisibility(View.GONE);
        this.selectedContact = null;
        FetchContacts();
    }

    private void FetchMessages(){
        chatService.getMessagesWith(selectedContact.email).enqueue(new Callback<List<MessageDTO>>() {
            @Override
            public void onResponse(Call<List<MessageDTO>> call, Response<List<MessageDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    messages.clear();
                    messages.addAll(response.body());
                    msgAdapter.notifyDataSetChanged();
                    if(messageContainer.getCount() > 0)
                        messageContainer.post(() -> messageContainer.setSelection(messageContainer.getCount()-1));
                }
                else
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<MessageDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void MessageReceived(MessageDTO messageDTO){
        if(selectedContact != null && messageDTO.senderEmail.equals(selectedContact.email)){
            messages.add(messageDTO);
            msgAdapter.notifyDataSetChanged();
            messageContainer.post(() -> messageContainer.smoothScrollToPosition(messageContainer.getCount()-1));
        }
    }
    private void toggleBlock() {
        if (selectedContact == null) return;

        if (!isBlocked) {
            userService.blockUser(selectedContact.email).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        isBlocked = true;
                        blockButton.setText("Unblock");
                        Toast.makeText(getContext(), "User blocked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to block", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userService.unblockUser(selectedContact.email).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        isBlocked = false;
                        blockButton.setText("Block");
                        Toast.makeText(getContext(), "User unblocked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to unblock", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void SendMessage(String text) {
        MessageDTO msg = new MessageDTO();
        msg.message = text;
        msg.senderEmail = "";
        msg.sendDate = "";
        messages.add(msg);

        msgAdapter.notifyDataSetChanged();

        if(messageContainer.getCount() > 0)
            messageContainer.post(() -> messageContainer.setSelection(messageContainer.getCount()-1));


        chatWebsocketService.sendMessage(text, selectedContact.email);
    }
}