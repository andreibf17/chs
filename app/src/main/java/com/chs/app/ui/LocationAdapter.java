package com.chs.app.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chs.app.R;
import com.chs.app.entities.Location;

import java.util.List;

/**
 * Created by Bogdan Cristian Vlad on 08-Dec-17.
 *
 * Class used for displaying the (customized) location item in the list of locations in LocationActivity.
 */

public class LocationAdapter extends ArrayAdapter<Location> {

    private Context context;
    private List<Location> locations;
    private int layoutResID;

    public LocationAdapter(Context context, int layoutResourceID, List<Location> locations) {
        super(context, layoutResourceID, locations);
        this.context = context;
        this.locations = locations;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.ilTitle = view.findViewById(R.id.ilTitle);
            itemHolder.ilMode = view.findViewById(R.id.ilMode);
            itemHolder.ilIcon = view.findViewById(R.id.ilIcon);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final Location hItem = locations.get(position);

        itemHolder.ilTitle.setText(hItem.getName());
        itemHolder.ilMode.setText(hItem.getMode().getName());
        itemHolder.ilIcon.setImageResource(hItem.getImage());

        return view;
    }

    private static class ItemHolder {
        TextView ilTitle;
        TextView ilMode;
        ImageView ilIcon;
    }
}
