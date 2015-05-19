package com.example.jcaal.sharingjob_v01.gui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jcaal.sharingjob_v01.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by jCaal on 30/03/2015.
 */
public class MyHolder extends TreeNode.BaseNodeViewHolder<MyHolder.IconTreeItem> {
    public MyHolder(Context context) {
        super(context);
    }
    //...
    @Override
    public View createNodeView ( TreeNode  node , IconTreeItem  value ) {
        final LayoutInflater inflater =  LayoutInflater.from(context);
        final  View view = inflater . inflate(R.layout.layout_node, null , false );

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.identar);
        ViewGroup.LayoutParams params = ll.getLayoutParams();
        params.width = value.ident;
        ll.setLayoutParams(params);

        TextView tvValue = ( TextView ) view.findViewById (R.id.node_value);
        switch(value.nivel){
            case 0:
                tvValue.setTextColor(Color.rgb(31,193,101));
                break;
            case 1:
                tvValue.setTextColor(Color.rgb(37,103,148));
                break;
            case 2:
                tvValue.setTextColor(Color.rgb(164,119,35));
                break;
        }
        tvValue.setText (value.text);

        ImageView iv = ( ImageView ) view.findViewById(R.id.node_icono);
        if (value.padre){
            iv.setImageResource(R.mipmap.folder_info);
        }else{
            iv.setImageResource(R.mipmap.file_info);
        }
        return view;
    }
    //...
    public  static  class  IconTreeItem {
        public IconTreeItem(int _icono, String _text, int _ident, int _id, int _nivel, boolean _padre){
            icono = _icono;
            text = _text;
            ident = _ident;
            id = String.valueOf(_id);
            nivel = _nivel;
            padre = _padre;
        }
        public int icono;
        public String text;
        public int ident;
        public String id;
        public int nivel;
        public boolean padre;
    }
}