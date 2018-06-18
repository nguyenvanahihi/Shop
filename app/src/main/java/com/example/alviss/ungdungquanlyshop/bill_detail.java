package com.example.alviss.ungdungquanlyshop;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class bill_detail extends Activity {
    protected DBManager db;
    protected int idt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_edit);
        db = DBManager.getInstance(getBaseContext());
        List<BillItem> show = new ArrayList<BillItem>();

        TextView pername = (TextView) findViewById(R.id.text_bill_item_pername);
        TextView offpr = (TextView) findViewById(R.id.text_bill_item_offpr);
        TextView offpe = (TextView) findViewById(R.id.text_bill_item_offpe);
        TextView price = (TextView) findViewById(R.id.text_bill_item_price);
        TextView btime = (TextView) findViewById(R.id.text_bill_item_time);

        ListView listView = (ListView) findViewById(R.id.list_bill_item);


        if (getIntent().getExtras() != null) {
            idt = getIntent().getIntExtra("ID", 0);
            show=db.getBDitem(idt);
            BillDetailAdapter adapter = new BillDetailAdapter(this,show);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            Bill bill = db.getBillbyId(idt);
            pername.setText("Khách hàng: " +bill.getPname());
            offpr.setText("Chiết khấu: " + bill.getOffpr());
            offpe.setText("Chiết khấu(%): " + bill.getOffpe());
            String d = String.valueOf(bill.getBillpr());
            price.setText("Tổng hóa đơn: " + String.format("%,d", Long.parseLong(d))+ " đ");
            btime.setText("Thời gian: "+bill.getBilltime());

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bill);
            toolbar.setTitle("Chi tiết hóa đơn số "+ String.valueOf(idt));
            toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        }

        Button back = (Button) findViewById(R.id.btn_back_billdt);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bill_detail.super.onBackPressed();
            }
        });

        Button delete = (Button) findViewById(R.id.btn_dell_bill);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.dellBill(idt);
                setResult(100);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
