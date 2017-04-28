package com.prashantpandey.nurseryapp;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyCartDialog extends DialogFragment implements View.OnClickListener {

    private Button modifyDialogDeleteButton;
    private ImageButton modifyDialogDecreaseButton,modifyDialogIncreaseButton;
    private TextView modifyDialogText;
    private modifyItemInterface itemInterface;
    private int prevQuantity;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View modifyDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_modify,null,false);

        modifyDialogDeleteButton = (Button) modifyDialogView.findViewById(R.id.dialog_modify_deleteButton);
        modifyDialogDecreaseButton = (ImageButton) modifyDialogView.findViewById(R.id.dialog_modify_reduceQuantity);
        modifyDialogIncreaseButton = (ImageButton) modifyDialogView.findViewById(R.id.dialog_modify_increaseQuantity);
        modifyDialogText = (TextView) modifyDialogView.findViewById(R.id.dialog_modify_changeQuantityText);

        final AlertDialog modifyDialog = new AlertDialog.Builder(getActivity())
                                        .setView(modifyDialogView)
                                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                itemInterface.changeQuant(modifyDialogText.getText().toString());
                                                dismiss();
                                            }
                                        })
                                        .setNegativeButton("Cancel",null)
                                        .setTitle("Modify Item")
                                        .show();

        prevQuantity = getArguments().getInt("previousQuantity");
        String quantity = String.valueOf(prevQuantity);
        modifyDialogText.setText(quantity);
        modifyDialogDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog sureDialog = new AlertDialog.Builder(getActivity()).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemInterface.delete();
                    }
                }).setNegativeButton("Cancel",null).setTitle("Do you want to delete this item?").show();
                modifyDialog.dismiss();
            }
        });

        modifyDialogIncreaseButton.setOnClickListener(this);
        modifyDialogDecreaseButton.setOnClickListener(this);

        return modifyDialog;
    }
    public void modifyCartItem(modifyItemInterface modifyItemInterface){
        itemInterface = modifyItemInterface;
    }

    @Override
    public void onClick(View v) {
        if (v==modifyDialogIncreaseButton){
            try {
                int i = Integer.parseInt(modifyDialogText.getText().toString());
                int j = i + 1;
                String k = String.valueOf(j);
                modifyDialogText.setText(k);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Quantity can only be a number", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==modifyDialogDecreaseButton){
            try {
                if (Integer.parseInt(modifyDialogText.getText().toString()) <= 1) {
                    Toast.makeText(getActivity(), "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                } else {
                    int i = Integer.parseInt(modifyDialogText.getText().toString());
                    int j = i - 1;
                    String k = String.valueOf(j);
                    modifyDialogText.setText(k);
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Quantity can only be a number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface modifyItemInterface {
        void changeQuant(String result);
        void delete();
    }
}
