package com.example.messenger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ExampleBottomSheet extends BottomSheetDialogFragment {

    public interface OnProfileSavedListener {
        void onProfileSaved(
                String name,
                String lastname,
                String age,
                String birthday,
                String city
        );
    }

    private OnProfileSavedListener listener;

    public void setListener(OnProfileSavedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.bottom_sheet_example,
                container,
                false
        );

        EditText name = view.findViewById(R.id.editName);
        EditText lastName = view.findViewById(R.id.editLastName);
        EditText age = view.findViewById(R.id.editAge);
        EditText birthday = view.findViewById(R.id.editBirthday);
        EditText city = view.findViewById(R.id.editCity);
        Button save = view.findViewById(R.id.buttonSave);

        save.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProfileSaved(
                        name.getText().toString(),
                        lastName.getText().toString(),
                        age.getText().toString(),
                        birthday.getText().toString(),
                        city.getText().toString()
                );
            }
            dismiss();
        });

        return view;
    }
}
