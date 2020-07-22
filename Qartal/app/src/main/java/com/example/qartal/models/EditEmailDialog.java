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

public class EditEmailDialog extends AppCompatDialogFragment {

    private EditText email;
    private EditEmailDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_email,null);

        builder.setView(view)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Email = email.getText().toString();
                        listener.TransferEmailText(Email);
                    }
                });

        email = view.findViewById(R.id.email_edit);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditEmailDialogListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "Must Implement EditEmailDialogListener");
        }
    }

    public interface EditEmailDialogListener{
        void TransferEmailText(String Email);
    }


}
