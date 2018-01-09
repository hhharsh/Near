package com.example.hhharsh.near;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hhharsh on 4/1/18.
 */

public class CustomAdapter extends ArrayAdapter<Item> {

    public CustomAdapter(Activity context, ArrayList<Item> item){
        super(context,0,item);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Item currentPlace = getItem(position);
        String add=currentPlace.getAddress();
        String loc_n=currentPlace.getLoc_name();
        String uu=currentPlace.getUrl();
        Double rr=currentPlace.getRating();
        Log.v("rating_check",String.valueOf(rr));
        TextView loc_tv=(TextView)listItemView.findViewById(R.id.location_id);
        loc_tv.setText(loc_n);

        TextView add_tv=(TextView)listItemView.findViewById(R.id.address_id);
        add_tv.setText(add);

        TextView rat_tv=(TextView)listItemView.findViewById(R.id.rating_id);
        rat_tv.setText(String.valueOf(rr));

       return listItemView;
    }
}
