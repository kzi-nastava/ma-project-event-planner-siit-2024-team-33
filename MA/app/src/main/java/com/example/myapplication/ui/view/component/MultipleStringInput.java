package com.example.myapplication.ui.view.component;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MultipleStringInput extends Fragment {

    public MultipleStringInput() {
    }

    public static MultipleStringInput newInstance() {
        MultipleStringInput fragment = new MultipleStringInput();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText editTextInput;
    private Button buttonAdd;
    private LinearLayout listContainer;

    private final List<String> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_multiple_string_input, container, false);

        editTextInput = view.findViewById(R.id.editTextInput);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        listContainer = view.findViewById(R.id.listContainer);

        buttonAdd.setOnClickListener(v -> {
            String text = editTextInput.getText().toString().trim();
            if (!text.isEmpty()) {
                itemList.add(text);
                editTextInput.setText("");
                refreshList();
            }
        });

        return view;
    }

    private void refreshList() {
        listContainer.removeAllViews();

        for (int i = 0; i < itemList.size(); i++) {
            String item = itemList.get(i);
            int index = i;

            LinearLayout itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(0, 8, 0, 8);

            TextView textView = new TextView(getContext());
            textView.setText(item);
            textView.setLayoutParams(new LinearLayout.LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            Button removeButton = new Button(getContext());
            removeButton.setText("Remove");
            removeButton.setOnClickListener(v -> {
                itemList.remove(index);
                refreshList();
            });

            itemLayout.addView(textView);
            itemLayout.addView(removeButton);
            listContainer.addView(itemLayout);
        }
    }

    public List<String> getItemList(){
        return itemList;
    }
}