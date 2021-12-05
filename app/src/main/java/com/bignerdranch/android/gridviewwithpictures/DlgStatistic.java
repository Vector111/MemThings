package com.bignerdranch.android.gridviewwithpictures;

        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.app.AppCompatDialogFragment;

        import static androidx.appcompat.app.AlertDialog.*;

public class DlgStatistic extends AppCompatDialogFragment {
    private View mView;
    private String mPositiveButtonText;
    private TextView textViewGames, textViewRecords, textViewLastResults;

    public DlgStatistic(final String positiveButtonText, final View view) {
        mPositiveButtonText = positiveButtonText;
        mView = view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setView(mView).setPositiveButton(mPositiveButtonText, null);
        Dialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }
}
