package com.example.alviss.ungdungquanlyshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alviss on 12/24/2017.
 */

public class MerAllAdapter extends BaseAdapter {
    private List<Mer> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public MerAllAdapter(Context aContext,  List<Mer> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public List<Mer> getListData(){
        return listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Mer getItem(int position) {return listData.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_mer_list_all, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.mitextView1);
            holder.sellView = (TextView) convertView.findViewById(R.id.mitextView2);
            holder.sumView = (TextView) convertView.findViewById(R.id.mitextView3);
            holder.priceView = (TextView) convertView.findViewById(R.id.mitextView4);
            holder.typeView = (TextView) convertView.findViewById(R.id.mitextView5);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        Mer mer = this.listData.get(position);
        holder.nameView.setText(mer.getName());
        if (mer.getSell()==1)
            holder.sellView.setText("Đang bán");
        else
            holder.sellView.setText("Chưa bắt đầu bán");
        holder.sumView.setText(String.valueOf(mer.getSum() + " " + mer.getCount()));
        //String.format("%,d", Long.parseLong(view.toString()));
        String d = String.valueOf(mer.getPrice());
        holder.priceView.setText(String.format("%,d", Long.parseLong(d)));
        holder.typeView.setText(String.valueOf(mer.getType()));
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView sellView;
        TextView sumView;
        TextView priceView;
        TextView typeView;
    }
}
