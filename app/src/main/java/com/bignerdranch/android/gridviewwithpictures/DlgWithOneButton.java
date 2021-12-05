package com.bignerdranch.android.gridviewwithpictures;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import static androidx.appcompat.app.AlertDialog.*;

public class DlgWithOneButton extends AppCompatDialogFragment {
    private String mTitle;
    private String mMess;
    private String mPositiveButtonText;

    public DlgWithOneButton(final String title, final String mess,
                            final String positiveButtonText) {
        mTitle = title;
        mMess = mess;
        mPositiveButtonText = positiveButtonText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setTitle(mTitle).setMessage(mMess).setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Закрываем окно
//                dialog.cancel();
            }
        });
        Dialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }
}
