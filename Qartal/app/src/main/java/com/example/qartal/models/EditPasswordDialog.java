package com.example.qartal.models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.qartal.R;

public class EditPasswordDialog extends AppCompatDialogFragment {

    private EditText old_password , new_password , re_new_password;
    private EditPasswordDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_password_old,null);

        builder.setView(view)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Old_Password = old_password.getText().toString();
                        String New_Password = new_password.getText().toString();
                        String Re_New_Password = re_new_password.getText().toString();

                        if(New_Password.equals(Re_New_Password)){
                            listener.TransferPasswordText(Old_Password,New_Password);
                        }else {
                            Toast.makeText(getActivity(),"Password Not match",Toast.LENGTH_LONG).show();
                        }

                    }
                });



        old_password = view.findViewById(R.id.old_password_edit);
        new_password = view.findViewById(R.id.new_password_edit);
        re_new_password = view.findViewById(R.id.re_new_password_edit);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EditPasswordDialogListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() +
                    "Must Implement EditPasswordDialogListener");
        }
    }

    public interface EditPasswordDialogListener{
        void TransferPasswordText(String Old_password, String New_Password);
    }


}
