package com.kira.spark.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kira.spark.R;
import com.kira.spark.bean.Item;

import java.util.List;

public class KitchenOrderAdapter extends RecyclerView.Adapter<KitchenOrderAdapter.MyViewHolder> {

    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, id, qty;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.cost);
        }
    }


    public KitchenOrderAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kitchen_order_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.getName()+" "+"( x "+item.getQty()+" )");
        holder.price.setText(item.getPrice().toString());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
    }

}
