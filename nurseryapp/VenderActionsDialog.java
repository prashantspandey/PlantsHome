package com.prashantpandey.nurseryapp;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

public class VenderActionsDialog extends DialogFragment{

    private Button orderConfirmation,orderDelivered;
    public vendorDialogInterface dialogInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View vendeorView = getActivity().getLayoutInflater().inflate(R.layout.dialog_vendoractions,null,false);
        orderConfirmation = (Button) vendeorView.findViewById(R.id.dialog_vendor_orderConfirmationButton);
        orderDelivered = (Button) vendeorView.findViewById(R.id.dialog_vendor_orderDeliveredButton);
        AlertDialog dialog =new AlertDialog.Builder(getActivity())
                                .setView(vendeorView)
                                .setNegativeButton("Cancel",null)
                                .setTitle("Order Status")
                                .show();

        orderConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog confirmDialog = new AlertDialog.Builder(getActivity())
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialogInterface.confirmation(true);
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("Cancel",null).setTitle("Do you want to Confirm this order?").show();
            }
        });
        orderDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog deliveredDialog = new AlertDialog.Builder(getActivity())
                                .setPositiveButton("Delivery Successful", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialogInterface.delivered(true);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("Delivery Failed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogInterface.delivered(false);
                            }
                        }).setNeutralButton("Cancel",null).setTitle("Delivery Status").show();
            }
        });

        return dialog;

    }
    public void setConfirmations(vendorDialogInterface dialogInterface){
        this.dialogInterface = dialogInterface;
    }
    public interface vendorDialogInterface{
        void confirmation(boolean confirmed);
        void delivered(boolean delivery);
    }
}
