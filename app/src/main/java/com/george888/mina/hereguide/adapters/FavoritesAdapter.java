package com.george888.mina.hereguide.adapters;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.data.sql.FavContract;
import com.george888.mina.hereguide.ui.homeActivity.HomeMvpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by minageorge on 2/18/18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.VH> implements View.OnClickListener {

    private Cursor myCursor = null;
    private Context mContext;
    private HomeMvpView mvpView;
    private HereApp app = null;

    public FavoritesAdapter(Context context) {
        this.mContext = context;
        this.mvpView = (HomeMvpView) context;
        app = ((HereApp) mContext.getApplicationContext());
        app.DistanceType();
    }

    @Override
    public FavoritesAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_fav, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.VH holder, int position) {
        if (myCursor != null) {
            myCursor.moveToPosition(position);
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_PHOTO))
                    + "&key=" + mContext.getResources().getString(R.string.google_places_service_api_key) + "";
            try {
                Glide.with(mContext).load(photoUrl).into(holder.placeImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.placeTitle.setText(myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_NAME)));
            holder.placeRate.setRating(Float.parseFloat(myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_RATE))));
            String placeLat = myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_LAT));
            String placeLng = myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_LNG));
            String dist = calculateDistance(app.getLocatinLatitude(), app.getLocatinLongitude(), placeLat, placeLng) ;
            holder.placeDistance.setText(dist+" "+app.getDistanceType());
            holder.v.setTag(myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_ID)));
            holder.v.setTag(Integer.parseInt(mContext.getString(R.string.place_cursor_pos)), position);
            holder.v.setTag(Integer.parseInt(mContext.getString(R.string.place_dis_tag)), dist);
            holder.v.setOnClickListener(this);

        }
    }

    @Override
    public int getItemCount() {
        if (myCursor == null) {
            return 0;
        } else {
            return myCursor.getCount();
        }
    }

    public void setCursor(Cursor cursor) {
        myCursor = null;
        myCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag(Integer.parseInt(mContext.getString(R.string.place_cursor_pos)));
        String dist = (String) view.getTag(Integer.parseInt(mContext.getString(R.string.place_dis_tag)));
        myCursor.moveToPosition(pos);
        mvpView.openPlaceActivity(myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_ID)),
                myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_NAME)),
                myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_RATE)),
                dist
        );
    }

    private String calculateDistance(String lat1, String lng1, String lat2, String lng2) {
        float[] results = new float[1];
        float c = 1;
        if (app.getDistanceType().equals("km")) {
            c = 1000;
        }

        Location.distanceBetween(Double.parseDouble(lat1),
                Double.parseDouble(lng1),
                Double.parseDouble(lat2),
                Double.parseDouble(lng2), results);

        return String.valueOf((results[0] / c) );

    }

    public class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.im_place)
        CircleImageView placeImage;
        @BindView(R.id.text_place_title)
        TextView placeTitle;
        @BindView(R.id.rate_place)
        RatingBar placeRate;
        @BindView(R.id.text_place_dis)
        TextView placeDistance;
        View v;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.v = itemView;
        }
    }

}
