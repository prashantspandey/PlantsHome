package com.prashantpandey.nurseryapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class IdealExtras {

    private String name;
    private String weight;
    private String price;
    private String description;
    private String imageUrl;
    private String type;

    public IdealExtras(String name, String price, String weight, String description, String imageUrl, String type) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public IdealExtras() {
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }
    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public static class IdealExtraViewHolder extends RecyclerView.ViewHolder{

        private View extraView;
        private ImageView imageView;
        public IdealExtraViewHolder(View itemView) {
            super(itemView);
            extraView = itemView;
        }
        public void setName(String name){
            TextView nameText = (TextView) extraView.findViewById(R.id.extra_view_nameText);
            nameText.setText(name);
        }
        public void setImage(Context context, String url){
             imageView = (ImageView) extraView.findViewById(R.id.extra_view_imageview);
            Picasso.with(context).load(url).into(imageView);
        }
        public void setPrice(String price){
            TextView priceText = (TextView) extraView.findViewById(R.id.extra_view_priceText);
            priceText.setText(price);
        }
        public void setType(String type){
            TextView typeText = (TextView) extraView.findViewById(R.id.extra_view_typeText);
            typeText.setText(type);
        }
        public void setWeight(String weight){
            TextView weightText = (TextView) extraView.findViewById(R.id.extra_view_weightText);
            weightText.setText(weight);
        }


    }
}
