package com.prashantpandey.nurseryapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ShowImageDialog extends DialogFragment {

    private ImageView imgView;
    private String imgURL;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View imagedialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_image,null,false);

        imgView = (ImageView) imagedialog.findViewById(R.id.dialog_image_imageView);


        AlertDialog imageDialog = new AlertDialog.Builder(getActivity())
                                    .setView(imagedialog).setNegativeButton("Close",null).show();

        imgURL = getArguments().getString("imageLink");
        Picasso.with(getActivity()).load(imgURL).into(imgView);

        return imageDialog;

    }

}
