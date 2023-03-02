package com.example.getnet;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {
    private Context context;
    private int resourceid;
    private List<City> cityList;
    private LayoutInflater inflater;
    private int id;
    private int currentPos;

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public CityAdapter(Context context, int resource, List<City> cityList) {
        super(context,resource,cityList);
        resourceid = resource;
        currentPos=-1;
    }
    public void deldata(int postion){
        cityList.remove(postion);
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceid,parent,false);//parent：父类容器，在这里当作根结点

        TextView textViewid=(TextView) view.findViewById(R.id.textid);
        TextView textViewname=(TextView) view.findViewById(R.id.textName);
        TextView textViewnum=(TextView) view.findViewById(R.id.textNum);
        TextView textViewX=(TextView) view.findViewById(R.id.textX);
        TextView textViewY=(TextView) view.findViewById(R.id.textY);
        textViewid.setText(city.getId()+"");
        textViewname.setText(city.getName());
        textViewnum.setText(city.getNum());
        textViewX.setText(city.getX()+"");
        textViewY.setText(city.getY()+"");
        view.setBackgroundColor(Color.TRANSPARENT);
        if(currentPos==position){
            view.setBackgroundColor(Color.YELLOW);
        }
        return view;
    }
}
