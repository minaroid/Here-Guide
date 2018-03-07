package com.george888.mina.hereguide.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.pojo.ResultsPlace;
import com.george888.mina.hereguide.ui.resultsActivity.ResultsMvpView;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by minageorge on 1/29/18.
 */

public class ResultsRecyclerAdapter extends
        RecyclerView.Adapter<ResultsRecyclerAdapter.VH> implements View.OnClickListener {

    private ArrayList<ResultsPlace> mArrayList = new ArrayList<ResultsPlace>();
    private GridLayoutManager gridLayoutManager = null;
    private Context mContext;
    private ResultsMvpView resultsMvpView = null;
    private static String TAG = ResultsRecyclerAdapter.class.getSimpleName();
    private HereApp app = null;
    private String disType;

    public ResultsRecyclerAdapter(Context context, GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
        this.mContext = context;
        this.resultsMvpView = (ResultsMvpView) context;

        app = ((HereApp) context.getApplicationContext());
        app.DistanceType();
        String arr[] = context.getResources().getStringArray(R.array.dist_titles);

        if (app.getDistanceType().equals("km")) {
            disType = arr[0];
        } else {
            disType = arr[1];
        }

    }

    @Override
    public ResultsRecyclerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_result, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        ResultsPlace p = mArrayList.get(position);
        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + p.getPhoto_reference()
                + "&key=" + mContext.getResources().getString(R.string.google_places_service_api_key) + "";
        Log.d(TAG, photoUrl);
        try {
            Glide.with(mContext).load(photoUrl).into(holder.placeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.placeTitle.setText(p.getName());
        holder.placeRate.setRating(Float.parseFloat(p.getRate()));
        holder.placeDistance.setText(p.getDistance() + " "+disType);
        if (p.getOpen() == "true") {
            holder.badge.setBackground(mContext.getDrawable(R.drawable.online_badge));
        }
        holder.v.setOnClickListener(this);
        holder.v.setTag(position);
    }

    public void swapData(Collection<ResultsPlace> data) {
        this.mArrayList.clear();
        this.mArrayList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        resultsMvpView.openPlaceActivity(mArrayList.get(pos).getId(), mArrayList.get(pos).getName(),
                mArrayList.get(pos).getRate(), mArrayList.get(pos).getDistance(), mArrayList.get(pos).getPhoto_reference(),
                mArrayList.get(pos).getLat(),mArrayList.get(pos).getLng());
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
        @BindView(R.id.badge)
        View badge;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.v = itemView;

        }
    }


}
