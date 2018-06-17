package com.example.alviss.ungdungquanlyshop;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alviss on 12/22/2017.
 */

public class fragment_main extends Fragment implements SomeInterface {
    protected View myView;
    protected TabHost tab;//tab chinh cua lap hoa don
    protected DBManager db;//co so du lieu
    protected TextView maintext;//title hoa don
    protected List<Mer> listmer = new ArrayList<Mer>();//danh sach san pham thuoc hoa don
    protected ListView listView;//list view cac san pham
    protected MerInBillAdapter adapter;//adapter cua listview
    protected int main_bill_code;//ma hoa don
    protected int main_bill_pcode;//ma khach hang
    protected int main_bill_offpr=0;//giam gia luong tien
    protected int main_bill_offpe=0;//giam gia %
    protected int main_bill_price=0;//tong tien hoa don
    protected String main_bill_time;//thoi gian hoa don
    protected TextView billprice;//gia tien hoa don
    protected EditText billtime;//thoi gian lap hoa don

    protected LinearLayout billmore;
    protected Button btnbilloffpr;
    protected TextView offpr;
    protected Button btnbilloffpe;
    protected TextView offpe;
    protected Button btnbillless;
    protected Button btnbillmore;

    protected TextView pername;
    protected TextView perphone;
    protected TextView peradd;
    protected TextView permail;
    protected TextView pertype;
    protected TextView pernote;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_frag_main, container,false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DBManager(getActivity());//goi lai database tu main activity
        getActivity().setTitle("Lập hóa đơn");//set title
        main_bill_code = db.maxBillid()+1;//lay so hoa don moi nhat
        //khoi tao text view title hoa don
        maintext = (TextView) view.findViewById(R.id.text_bill_name);

        //khoi tao cac text view ve thong tin khach hang
        pername =(TextView) view.findViewById(R.id.per_name);
        perphone =(TextView) view.findViewById(R.id.per_phone);
        peradd =(TextView) view.findViewById(R.id.per_add);
        peradd.setMovementMethod(new ScrollingMovementMethod());//set scroll bar neu du lieu dai hon view
        permail =(TextView) view.findViewById(R.id.per_mail);
        pertype =(TextView) view.findViewById(R.id.per_type);
        pernote =(TextView) view.findViewById(R.id.per_note);
        pernote.setMovementMethod(new ScrollingMovementMethod());//tuong tu nhu tren
        perinbillreset();

        listView = (ListView) view.findViewById(R.id.mer_list);//list item thuoc hoa don

        billmoreless();//xu ly khoi tao va thao tac voi nut bam chi tiet hoa don

        tabsetup(view);//set up tab

        suggestmer(view);//xu ly suggest san pham va add san pham vao danh sach san pham

        suggestper(view);//xu ly suggest khach hang va cap nhat thong tin khach hang

        Button btnclean = (Button) view.findViewById(R.id.btn_bill_clear);
        btnclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dellBDTcodeb(0);
                listmer.clear();
                List<BillItem> billItemList = db.getBDTitem(0);
                for (BillItem item :billItemList){
                    Mer tada = db.getMerbyId(item.codem);
                    tada.amount = item.amountb;
                    listmer.add(tada);
                }
                adapter.notifyDataSetChanged();
                main_bill_code = db.maxBillid() + 1;
                main_bill_pcode = 0;
                main_bill_offpr = 0;
                main_bill_offpe = 0;
                main_bill_time = "";
                priceofbill();
                maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
                pername.setText("");
                perphone.setText("");
                peradd.setText("");
                permail.setText("");
                pertype.setText("");
                pernote.setText("");
                offpe.setText("");
                offpr.setText("");
                SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
                Date currentTime = Calendar.getInstance().getTime();
                billtime.setText(format.format(currentTime));
            }
        });

        Button btnsave = (Button) view.findViewById(R.id.btn_bill_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_bill_pcode==0) {Toast.makeText(myView.getContext(),"Chưa nhập thông tin khách hàng",Toast.LENGTH_SHORT).show();}
                else if (listmer.size()==0) {Toast.makeText(myView.getContext(),"Hóa đơn chưa có sản phẩm",Toast.LENGTH_SHORT).show();}
                else {
                    main_bill_time = billtime.getText().toString();
                    main_bill_code = db.maxBillid() + 1;
                    String pname = db.getPerbyId(main_bill_pcode).getName();
                    db.addBill(main_bill_pcode,pname,main_bill_offpe,main_bill_offpr,main_bill_price,main_bill_time);
                    List<BillItem> listitem = db.getBDTitem(0);
                    for (BillItem item : listitem) {
                        db.addBD(main_bill_code, item.codem, item.amountb);
                        Mer mer = db.getMerbyId(item.codem);
                        db.updMerSum(item.codem,mer.getSum()-item.amountb);
                        Toast.makeText(myView.getContext(), "Lưu hóa đơn thành công", Toast.LENGTH_SHORT).show();
                    }
                    dellBDTcodeb(0);
                    listmer.clear();
                    adapter.notifyDataSetChanged();
                    main_bill_code = db.maxBillid() + 1;
                    main_bill_pcode = 0;
                    main_bill_offpr = 0;
                    main_bill_offpe = 0;
                    main_bill_time = "";
                    maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
                    priceofbill();
                    pername.setText("");
                    perphone.setText("");
                    peradd.setText("");
                    permail.setText("");
                    pertype.setText("");
                    pernote.setText("");
                    offpe.setText("");
                    offpr.setText("");
                    SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
                    Date currentTime = Calendar.getInstance().getTime();
                    billtime.setText(format.format(currentTime));
                }
            }
        });
    }

    //thao tac khoi tao va xy lu nut bam ben canh gia tien
    protected void billmoreless(){
        billprice = (TextView) myView.findViewById(R.id.text_bill_price);//<hien thi> gia tien hoa don

        billtime = (EditText) myView.findViewById(R.id.bill_time);//thoi gian cua hoa don

        billmore = (LinearLayout) myView.findViewById(R.id.bill_detail_container);
        billmore.setVisibility(billmore.GONE);//khoi tao va set ẩn layout chi tiết phụ

        btnbillless = (Button) myView.findViewById(R.id.btn_bill_detail_less);
        btnbillless.setVisibility(btnbillless.GONE);//khoi tao va set ẩn nút ẩn chi tiết phụ

        offpr = (TextView) myView.findViewById(R.id.bill_offpr);
        offpe = (TextView) myView.findViewById(R.id.bill_offpercent);
        SimpleDateFormat format= new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        billtime.setText(format.format(currentTime));

        btnbillmore = (Button) myView.findViewById(R.id.btn_bill_detail_more);
        btnbillmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnbillmore.setVisibility(btnbillmore.GONE);
                btnbillless.setVisibility(btnbillless.VISIBLE);
                billmore.setVisibility(billmore.VISIBLE);
            }
        });

        btnbillless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnbillless.setVisibility(btnbillless.GONE);
                btnbillmore.setVisibility(btnbillmore.VISIBLE);
                billmore.setVisibility(billmore.GONE);
                //tat keyboard sau khi nhap
                InputMethodManager inputManager = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        btnbilloffpr = (Button) myView.findViewById(R.id.btn_offpr);
        btnbilloffpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = 0;
                if (!offpr.getText().toString().equals(""))
                    t = Integer.valueOf(offpr.getText().toString());
                main_bill_offpr=t;
                priceofbill();
            }
        });

        btnbilloffpe = (Button) myView.findViewById(R.id.btn_offpe);
        btnbilloffpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int t = 0;
                if (!offpe.getText().toString().equals(""))
                    t=Integer.valueOf(offpe.getText().toString());
                main_bill_offpe=t;
                priceofbill();
            }
        });
    }

    //khoi tao va set up tab
    protected void tabsetup(View view){
        //khoi tao tab qua viewid
        tab = (TabHost) view.findViewById(R.id.tabhost);
        tab.setup();

        //them cac tab con vao tab host
        TabHost.TabSpec spec = tab.newTabSpec("Sản Phẩm");
        spec.setContent(R.id.tab);
        spec.setIndicator("Sản Phẩm");
        tab.addTab(spec);

        spec = tab.newTabSpec("Khách Hàng");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Khách Hàng");
        tab.addTab(spec);
        //set chieu cao cua tab dieu khien
        tab.getTabWidget().getChildAt(0).getLayoutParams().height = 80;
        tab.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#d1d1d1"));
        tab.getTabWidget().getChildAt(1).getLayoutParams().height = 80;

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                //tat keyboard sau khi nhap
                InputMethodManager inputManager = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
                    tab.getTabWidget().getChildAt(i).setBackgroundColor(Color.TRANSPARENT); // unselected
                }

                tab.getTabWidget().getChildAt(tab.getCurrentTab()).setBackgroundColor(Color.parseColor("#d1d1d1")); // selected
            }
        });
    }

    //xu ly cap nhat them san pham
    protected void suggestmer(View view){

        listmer = new ArrayList<Mer>(){};
        List<BillItem> billItemList = db.getBDTitem(0);
        for (BillItem item :billItemList){
            Mer tada = db.getMerbyId(item.codem);
            tada.amount = item.amountb;
            listmer.add(tada);
        }
        priceofbill();

        adapter = new MerInBillAdapter(view.getContext(),listmer,this);
        listView.setAdapter(adapter);



        //xu ly text suggest vao cap nhat danh sach san pham cua hoa don
        final List<String> mername2 = new ArrayList<String>(){};
        final List<Mer> listtemp = db.getMerselling();
        for (Mer item : listtemp){
            mername2.add(item.getName().toString());
        }
        final AutoCompleteTextView text = (AutoCompleteTextView) view.findViewById(R.id.mer_auto);
        text.setAdapter(new ArrayAdapter(view.getContext(),R.layout.item_auto_complete,mername2));
//tat keyboard sau khi nhap
        InputMethodManager inputManager = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);



        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int check =0;
                int id =-1;
                String d = adapterView.getItemAtPosition(i).toString();
                for (Mer item : listmer){
                    if (item.getName().equals(d)) {check=1; id=listmer.indexOf(item); break;}
                }
                if (check ==0) {
                    Mer temp = db.getMerbyName(d);
                    temp.amount = 1;
                    listmer.add(temp);
                    adapter.notifyDataSetChanged();
                    text.setText("");
                    db.addBDT(0,temp.getId(),temp.amount);
                    priceofbill();
                }
                else {
                    if (listmer.get(id).getSum() > 0) {
                        listmer.get(id).amount++;
                        adapter.notifyDataSetChanged();
                        text.setText("");
                        updtoBDT(listmer.get(id).getId(),listmer.get(id).amount);
                        priceofbill();
                    }
                }
                //tat keyboard sau khi nhap
                InputMethodManager inputManager = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    //xu ly cap nhat khach hang
    protected void suggestper(View view){
        //xu ly text suggest vao cap nhat danh sach san pham cua hoa don
        final List<String> perlist = new ArrayList<String>(){};
        List<Person> list = db.getAllPer();
        for (Person item : list){
            perlist.add(item.getName().toString());
        }
        final AutoCompleteTextView text = (AutoCompleteTextView) view.findViewById(R.id.per_auto);
        text.setAdapter(new ArrayAdapter(view.getContext(),R.layout.item_auto_complete,perlist));

        text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ten = adapterView.getItemAtPosition(i).toString();
                Person person = db.getPerbyName(ten.toString());
                pername.setText(person.getName());
                perphone.setText(person.getPhone());
                peradd.setText(person.getAdd());
                permail.setText(person.getMail());
                pertype.setText(person.getType());
                pernote.setText(person.getNote());
                text.setText("");
                maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - " +person.getName());
                main_bill_pcode = person.getId();
                //tat keyboard sau khi nhap
                InputMethodManager inputManager = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button btngetphone = (Button) view.findViewById(R.id.btn_bill_get_phone);
        btngetphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+perphone.getText().toString()));
                startActivity(callIntent);
            }
        });
    }

    //reset cac truong cua thong tin khach hang thuoc hoa don
    public void perinbillreset(){
        maintext.setText("Hóa đơn số " + String.valueOf(main_bill_code) + " - ");
        main_bill_pcode = 0;
        pername.setText("");
        perphone.setText("");
        peradd.setText("");
        permail.setText("");
        pertype.setText("");
        pernote.setText("");
    }

    //cap nhat tong tien cua bill
    protected void priceofbill(){
        int temp =0;
        for (Mer item : listmer){
            temp=temp+item.amount*item.getPrice();
        }

        double ttt = (main_bill_offpe/100.00000)*temp;
        int dto = (int) ttt;
        temp=temp-dto;

        temp=temp-main_bill_offpr;

        main_bill_price=temp;
        String dd = String.valueOf(temp);

        billprice.setText(String.format("%,d", Long.parseLong(dd))+" đ");
        if (temp==0)
            billprice.setText("0 đ");
    }

    //cap nhat amount cua san pham thuoc bill tam thoi
    @Override
    public void updtoBDT(int codemer,int bamount){
        DBManager ac = new DBManager(myView.getContext());
        ac.updBDTamount(codemer,bamount);
        ac.close();
        priceofbill();
    }

    //phuong thuc xoa ma bill khoi table bill tam thoi, thuong la 0, sau nay se update nhieu bill 1 luc
    @Override
    public void dellBDTcodeb(int codebill){
        DBManager ac = new DBManager(myView.getContext());
        ac.delBDTcodeb(codebill);
        ac.close();
        priceofbill();
    }

    //phuong thuc xoa ma sp khoi table bill tam thoi
    @Override
    public void dellBDTcodem(int codem){
        DBManager ac = new DBManager(myView.getContext());
        ac.delBDTcodem(codem);
        ac.close();
        priceofbill();
    }
}

