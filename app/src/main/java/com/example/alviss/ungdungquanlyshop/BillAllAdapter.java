package com.example.alviss.ungdungquanlyshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alviss.ungdungquanlyshop.models.HoaDon;

import java.util.ArrayList;
import java.util.List;



public class BillAllAdapter extends BaseAdapter {
    //private List<Bill> listData;
    private ArrayList<HoaDon> arrHoaDon;
    private LayoutInflater layoutInflater;
    private Context context;

//    public BillAllAdapter(Context aContext, List<Bill> listData) {
//        this.context = aContext;
//        this.listData = listData;
//        layoutInflater = LayoutInflater.from(aContext);
//    }
public BillAllAdapter(Context aContext, ArrayList<HoaDon> listData) {
    this.context = aContext;
    this.arrHoaDon = listData;
    layoutInflater = LayoutInflater.from(aContext);

}

    public ArrayList<HoaDon> getArrHoaDon() {
        return arrHoaDon;
    }

    public void setArrHoaDon(ArrayList<HoaDon> arrHoaDon) {
        this.arrHoaDon = arrHoaDon;
    }

    public List<HoaDon> getListData(){
        return arrHoaDon;
    }

    @Override
    public int getCount() {
        return arrHoaDon.size();
    }

    @Override
    public HoaDon getItem(int position) {return arrHoaDon.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_bill_item, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.item_bill_name);
            holder.pnameView = (TextView) convertView.findViewById(R.id.item_bill_pername);
            holder.priceView = (TextView) convertView.findViewById(R.id.item_bill_price);
            holder.timeView = (TextView) convertView.findViewById(R.id.item_bill_time);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        //Bill bill = this.listData.get(position);
        HoaDon hoaDon = this.arrHoaDon.get(position);
        holder.nameView.setText("Hóa đơn số " +String.valueOf(hoaDon.getMaHD()));
        //holder.pnameView.setText(bill.getPname());
        holder.pnameView.setText(hoaDon.getMaKhachHang());
        //String.format("%,d", Long.parseLong(view.toString()));
//        String d = String.valueOf(bill.getBillpr());
        String d = String.valueOf(hoaDon.getTongTien());
        holder.priceView.setText(d);
        holder.timeView.setText(hoaDon.getNgayLap());
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView pnameView;
        TextView priceView;
        TextView timeView;
    }
}
