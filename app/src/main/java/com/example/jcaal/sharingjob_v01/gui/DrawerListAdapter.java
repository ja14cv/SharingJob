package com.example.jcaal.sharingjob_v01.gui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jcaal.sharingjob_v01.R;

import java.util.List;

/**
 * Created by Jesus on 31/03/2015.
 */

public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {

    public int selected;

    public DrawerListAdapter(Context context, List<DrawerItem> objects) {
        super(context, android.R.layout.simple_list_item_activated_1, android.R.id.text1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }

        if(position == selected){
            convertView.setBackgroundColor(Color.argb(180,204,58,70));
        }else{
            convertView.setBackgroundColor(Color.argb(0,204,58,70));
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.drawer_icon);
        TextView name = (TextView) convertView.findViewById(R.id.drawer_name);

        DrawerItem item = getItem(position);
        icon.setImageResource(item.getIconId());
        name.setText(item.getName());

        return convertView;
    }
}

