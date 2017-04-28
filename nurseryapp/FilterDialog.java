package com.prashantpandey.nurseryapp;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FilterDialog extends DialogFragment implements View.OnClickListener {

    private CheckBox checkSize;
    private RadioGroup sizes;
    private RadioButton sizeSmall,sizeMedium,sizeLarge,sizeXlarge,sizeXXlarge;
    public OnDialogFilter mDialogFilter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null, false);
        checkSize = (CheckBox) dialogView.findViewById(R.id.dialog_check_size);
        sizes = (RadioGroup) dialogView.findViewById(R.id.dialog_size_radiobuttons);
        sizeSmall = (RadioButton) dialogView.findViewById(R.id.dialog_radioSmall);
        sizeMedium = (RadioButton) dialogView.findViewById(R.id.dialog_radioMedium);
        sizeLarge = (RadioButton) dialogView.findViewById(R.id.dialog_raidoLarge);
        sizeXlarge = (RadioButton)dialogView.findViewById(R.id.dialog_radioXlarge);
        sizeXXlarge = (RadioButton) dialogView.findViewById(R.id.dialog_radioXXlarge);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sizeSmall.isChecked()){
                            mDialogFilter.finish("Small");
                        }
                        if (sizeMedium.isChecked()){
                            mDialogFilter.finish("Medium");
                        }
                        if (sizeLarge.isChecked()){
                            mDialogFilter.finish("Large");
                        }
                        if (sizeXlarge.isChecked()){
                            mDialogFilter.finish("X-Large");
                        }
                        if (sizeXXlarge.isChecked()){
                            mDialogFilter.finish("XX-Large");
                        }
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", null)
                .setTitle("Filter Plants")
                .show();

        checkSize.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_check_size) {
            if (checkSize.isChecked()) {

                sizes.setVisibility(View.VISIBLE);
            } else {
                sizes.setVisibility(View.GONE);
            }
        }

        }
    



    public void setFilterBySize(OnDialogFilter dialogFilter){
        mDialogFilter = dialogFilter;
    }
    public interface OnDialogFilter{
        void finish(String result);
    }
}
