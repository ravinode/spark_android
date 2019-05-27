package com.kira.spark.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.kira.spark.R;
import com.kira.spark.bean.Menu;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends ArrayAdapter<Menu> {

    Context context;
    int resource, textViewResourceId;
    List<Menu> items, tempItems, suggestions;

    public SearchAdapter(Context context, int resource, int textViewResourceId, List<Menu> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Menu>(items); // this makes the difference.
        suggestions = new ArrayList<Menu>();
        Toast.makeText(context," is selected!"+items.size(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_search_menu, parent, false);
        }
        Menu menu = items.get(position);


        if (menu != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(menu.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Menu) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Menu menu : tempItems) {
                    if (menu.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(menu);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Menu> filterList = (ArrayList<Menu>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Menu menu : filterList) {
                    add(menu);
                    notifyDataSetChanged();
                }
            }
        }
    };
}