package com.bignerdranch.android.gridviewwithpictures;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DlgWithTwoButtons extends AppCompatDialogFragment {
    private String mTitle;
    private String mMess;
    private String mPositiveButtonText;
    private String mNegativeButtonText;
    DoForPositive1 mDoForPositive1;

    public DlgWithTwoButtons(final String title, final String mess,
                             final String positiveButtonText, final String negativeButtonText,
                             DoForPositive1 doForPositive1) {
        mTitle = title;
        mMess = mess;
        mPositiveButtonText = positiveButtonText;
        mNegativeButtonText = negativeButtonText;
        mDoForPositive1 = doForPositive1;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle).setMessage(mMess).setPositiveButton(mPositiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mDoForPositive1.fDo();
            }
        }).setNegativeButton(mNegativeButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        Dialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }
}
