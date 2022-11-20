package edu.northeastern.numad22fateam26.sticker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import edu.northeastern.numad22fateam26.R;

public class Dialog extends AppCompatDialogFragment {
    private EditText edtMessage;
    private EditText edtTitle;
    private DialogListener listener;
    private int position;

    public Dialog(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sticker_dialog, null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String message = edtMessage.getText().toString();
                        String title = edtTitle.getText().toString();
                        listener.applyTexts(title, message, position);

                    }
                });
        edtMessage = view.findViewById(R.id.message);
        edtTitle = view.findViewById(R.id.edtTitle);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement dialog listener");
        }

    }

    public interface DialogListener {
        void applyTexts(String message, String senderName, int position);
    }
}
