package com.example.m.androidpenza;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class PhotoSelectDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_photo_select)
                .setPositiveButton(R.string.dialog_gallery, (dialog, id) ->
                        sendSelection(ContactFragment.SELECTION_GALLERY)
                )
                .setNegativeButton(R.string.dialog_camera, (dialog, id) ->
                        sendSelection(ContactFragment.SELECTION_CAMERA)
                );
        return builder.create();
    }

    private void sendSelection(int selection) {
        Intent intent = new Intent();
        intent.putExtra(ContactFragment.PHOTO_SELECT, selection);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

}
