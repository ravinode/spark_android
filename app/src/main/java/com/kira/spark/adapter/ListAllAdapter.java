package com.kira.spark.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kira.spark.R;
import com.kira.spark.bean.Menu;


import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ListAllAdapter extends RecyclerView.Adapter<ListAllAdapter.MyViewHolder> {

    private Context mContext;
    private List<Menu> menuList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public ListAllAdapter(Context mContext, List<Menu> menuList) {
        this.mContext = mContext;
        this.menuList = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_all_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        holder.title.setText(menu.getName());
        Glide.with(mContext).load(menu.getThumbnail()).into(holder.thumbnail);
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */

    @Override
    public int getItemCount() {
        return menuList.size();
    }

}
