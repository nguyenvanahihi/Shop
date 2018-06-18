package com.example.alviss.ungdungquanlyshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MerInBillAdapter extends BaseAdapter {
    private List<Mer> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private SomeInterface responder;

    public MerInBillAdapter(Context aContext, List<Mer> listData,SomeInterface test) {
        this.context = aContext;
        this.listData = listData;
        this.responder = test;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_mer_in_bill, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.bi_mer_name);
            holder.sumView = (TextView) convertView.findViewById(R.id.bi_mer_sum);
            holder.priceView = (TextView) convertView.findViewById(R.id.bi_mer_price);
            holder.amountView = (TextView) convertView.findViewById(R.id.bi_mer_amount);
            holder.outputView = (TextView) convertView.findViewById(R.id.bi_mer_output);
            holder.btndel = (Button) convertView.findViewById(R.id.btn_dell_mer_in_bill);
            holder.upam = (Button) convertView.findViewById(R.id.btn_up_amount);
            holder.downam = (Button) convertView.findViewById(R.id.btn_down_amount);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        final Mer mer = this.listData.get(position);

        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(position);
                notifyDataSetChanged();
                responder.dellBDTcodem(mer.getId());
            }
        });


        holder.nameView.setText(mer.getName());
        holder.amountView.setText(String.valueOf(mer.amount));
        holder.sumView.setText(String.valueOf((mer.getSum()-mer.amount) + " " + mer.getCount()));
        //String.format("%,d", Long.parseLong(view.toString()));
        String d = String.valueOf(mer.getPrice());
        holder.priceView.setText(String.format("%,d", Long.parseLong(d)));

        d = String.valueOf(mer.getPrice()*mer.amount);
        holder.outputView.setText(String.format("%,d", Long.parseLong(d))+" đ");

        holder.downam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mer.amount>1) {
                    mer.amount--;
                    holder.amountView.setText(String.valueOf(mer.amount));
                    holder.sumView.setText(String.valueOf((mer.getSum()-mer.amount) + " " + mer.getCount()));
                    String dd = String.valueOf(mer.getPrice()*mer.amount);
                    holder.outputView.setText(String.format("%,d", Long.parseLong(dd))+" đ");
                    responder.updtoBDT(mer.getId(),mer.amount);
                }
            }
        });

        holder.upam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mer.amount<mer.getSum()) {
                    mer.amount++;
                    holder.amountView.setText(String.valueOf(mer.amount));
                    holder.sumView.setText(String.valueOf((mer.getSum() - mer.amount) + " " + mer.getCount()));
                    String dd = String.valueOf(mer.getPrice() * mer.amount);
                    holder.outputView.setText(String.format("%,d", Long.parseLong(dd))+" đ");
                    responder.updtoBDT(mer.getId(),mer.amount);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView sumView;
        TextView priceView;
        TextView amountView;
        TextView outputView;
        Button btndel;
        Button upam;
        Button downam;
    }
}
