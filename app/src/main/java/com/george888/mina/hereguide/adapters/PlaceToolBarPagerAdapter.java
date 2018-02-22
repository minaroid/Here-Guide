package com.george888.mina.hereguide.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.george888.mina.hereguide.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by minageorge on 2/20/18.
 */

public class PlaceToolBarPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;

    private ArrayList<String> images = new ArrayList<>();

    public PlaceToolBarPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.item_place_pager, container, false);
        ImageView im = (ImageView) v.findViewById(R.id.image_view_id);
        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + images.get(position)
                + "&key=" + context.getResources().getString(R.string.google_places_service_api_key) + "";
        Glide.with(context)
                .load(photoUrl)
                .into(im);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public void swapData(ArrayList<String> im){
        images.clear();
        images.addAll(im);
        notifyDataSetChanged();
    }
}
