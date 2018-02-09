package com.example.android.inventoryapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by UFO_24 on 19-01-2018.
 */

public class NumberPickerDialog extends DialogFragment {

    public static NumberPickerDialog initDialog(int dialogId, String title, String buttonText){

        Bundle bundle = new Bundle();
        bundle.putInt("dialogId", dialogId);
        bundle.putString("dialogName", title);
        bundle.putString("btnText",buttonText);

        NumberPickerDialog object = new NumberPickerDialog();
        object.setArguments(bundle);
        return object;
    }

    public static interface OnCompleteListener{
public abstract void onComplete(int dialogId, int quantity);
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            this.mListener = (OnCompleteListener) context;

        }catch (final ClassCastException e){
            throw new ClassCastException(context.toString()+" implement OnCompleteListener ");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        final int  dialogId =  bundle.getInt("dialogId");
        final String dialogName = bundle.getString("dialogName");
        final String buttonText = bundle.getString("btnText");

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.number_picker, null);

        final NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker_id);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(500);
        numberPicker.setWrapSelectorWheel(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Quantity");
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                numberPicker.clearFocus();
                int quantity = numberPicker.getValue();

                mListener.onComplete(dialogId, quantity);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}
