package com.george888.mina.hereguide.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.pojo.HomeType;
import com.george888.mina.hereguide.ui.homeActivity.HomeMvpView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by minageorge on 1/22/18.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.VH>
        implements View.OnClickListener {

    private ArrayList<HomeType> mArrayList = new ArrayList<HomeType>();
    private GridLayoutManager gridLayoutManager = null;
    private HomeMvpView homeMvpView;
    private static String TAG = HomeRecyclerAdapter.class.getSimpleName();

    public HomeRecyclerAdapter(Context context) {
        this.homeMvpView = (HomeMvpView) context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeRecyclerAdapter.VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int spanCount = gridLayoutManager.getSpanCount();
        HomeType place = mArrayList.get(position);
        if (spanCount >= 2) {
            CircleImageView im = holder.typeImage.get(1);
            TextView name = holder.typeName.get(1);
            im.setImageResource(place.getPic());
            name.setText(place.getTitle());
            holder.layoutRec.setVisibility(View.GONE);
            holder.layoutSqu.setVisibility(View.VISIBLE);
        } else {
            CircleImageView im = holder.typeImage.get(0);
            TextView name = holder.typeName.get(0);
            im.setImageResource(place.getPic());
            name.setText(place.getTitle());
            holder.layoutSqu.setVisibility(View.GONE);
            holder.layoutRec.setVisibility(View.VISIBLE);
        }
        holder.v.setTag(position);
        holder.v.setOnClickListener(this);
    }

    public void swapData(Collection<HomeType> data) {
        this.mArrayList = new ArrayList<>();
        this.mArrayList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onClick(View v) {
        String type = mArrayList.get((int) v.getTag()).getType();
        String title = mArrayList.get((int) v.getTag()).getTitle();
        homeMvpView.openResultsActivity(title,type);
    }

    public class VH extends RecyclerView.ViewHolder {
        @BindViews({R.id.type_image_rec, R.id.type_image_squ})
        List<CircleImageView> typeImage;
        @BindViews({R.id.type_name_rec, R.id.type_name_squ})
        List<TextView> typeName;
        @BindView(R.id.layout_rec)
        CardView layoutRec;
        @BindView(R.id.layout_squ)
        CardView layoutSqu;
        View v;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            v = itemView;

        }
    }

    public void setLayoutManager(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;

    }
}
