package com.example.qartal.models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.qartal.R;

public class EditNameDialog extends AppCompatDialogFragment {

    private EditText username;
    private EditNameDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_name,null);

        builder.setView(view)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Username = username.getText().toString();
                        listener.TransferNameText(Username);
                    }
                });

        username = view.findViewById(R.id.username_edit);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditNameDialogListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "Must Implement EditNameDialogListener");
        }
    }

    public interface EditNameDialogListener{
        void TransferNameText(String username);
    }


}
