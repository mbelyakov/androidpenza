package com.example.m.androidpenza;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class EditDeleteDialogFragment extends DialogFragment {
    private EditDeleteDialogCallbacks listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditDeleteDialogCallbacks) {
            listener = (EditDeleteDialogCallbacks) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ContactListCallbacks");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_edit_delete)
                .setPositiveButton(R.string.dialog_edit, (dialog, id) -> listener.onDialogEditClick(EditDeleteDialogFragment.this))
                .setNegativeButton(R.string.dialog_delete, (dialog, id) -> listener.onDialogDeleteClick(EditDeleteDialogFragment.this));
        return builder.create();
    }

    public interface EditDeleteDialogCallbacks {
        void onDialogEditClick(DialogFragment dialog);

        void onDialogDeleteClick(DialogFragment dialog);
    }
}
