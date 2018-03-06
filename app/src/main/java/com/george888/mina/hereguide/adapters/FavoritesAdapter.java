package com.george888.mina.hereguide.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.data.sql.FavContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by minageorge on 2/18/18.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.VH> {

    private Cursor myCursor = null;
    private Context mContext;

    public FavoritesAdapter(Context context) {

        this.mContext = context;
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
            holder.placeDistance.setText("5 Km");
            holder.v.setTag(myCursor.getString(myCursor.getColumnIndexOrThrow(FavContract.FavListEntry.COL_PLACE_ID)));
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
