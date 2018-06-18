package com.example.alviss.ungdungquanlyshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alviss.ungdungquanlyshop.models.KhachHang;

import java.util.ArrayList;
import java.util.List;



public class PerAllAdapter extends BaseAdapter {
    //private List<Person> listData;
    private ArrayList<KhachHang> arrKhachHang;
    private LayoutInflater layoutInflater;
    private Context context;

    public PerAllAdapter(Context aContext, ArrayList<KhachHang> listData) {
        this.context = aContext;
        this.arrKhachHang = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public ArrayList<KhachHang> getListData(){
        return arrKhachHang;
    }

    @Override
    public int getCount() {
        return arrKhachHang.size();
    }

    @Override
    public KhachHang getItem(int position) {return arrKhachHang.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_per_list_all, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.pitextView1);
            holder.phoneView = (TextView) convertView.findViewById(R.id.pitextView2);
            holder.typeView = (TextView) convertView.findViewById(R.id.pitextView3);
            holder.addView = (TextView) convertView.findViewById(R.id.pitextView4);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        KhachHang person = this.arrKhachHang.get(position);
        holder.nameView.setText(person.getTenKhachHang());
        holder.phoneView.setText(person.getSoDienThoai());
        holder.typeView.setText(person.get_id());
        holder.addView.setText(person.getDiaChi());
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView phoneView;
        TextView typeView;
        TextView addView;
    }
}
