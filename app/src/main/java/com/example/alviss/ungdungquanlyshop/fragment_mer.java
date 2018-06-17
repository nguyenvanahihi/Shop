package com.example.alviss.ungdungquanlyshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alviss on 12/21/2017.
 */

public class fragment_mer extends Fragment{
    protected View myView;
    protected DBManager db;
    protected ListView listView;
    protected List<Mer> show = new ArrayList<Mer>();
    protected MerAllAdapter adapter;
    protected Spinner spinnertype;
    protected List<String> listtype = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_frag_mer_all, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myView = view;

        getActivity().setTitle("Danh sách Sản phẩm");
        db = new DBManager(getActivity());

        listView = (ListView) view.findViewById(R.id.list_all_mer);//danh sach san pham
        spinnertype = (Spinner) view.findViewById(R.id.spinner_mer_all_type);//loai san pham de filter
        final EditText searchname = (EditText) view.findViewById(R.id.mer_name_search);//muc tiem kiem ten sp

        updatelistitem(db.getAllMer());//cap nhat danh sach san pham

        updatespin();//cap nhat spinner loai

        //auto update danh sach san pham khi thay doi text
        searchname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = searchname.getText().toString();
                if (temp.equals("")){
                    updatelistitem(db.getAllMer());
                }
                else{
                    updatelistitem(db.getMerbyPartName(temp));
                }
            }
        });

        //filter san pham theo loai
        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchname.setText("");
                if (spinnertype.getSelectedItem().toString().equals("Tất cả")){
                    updatelistitem(db.getAllMer());
                } else {
                    updatelistitem(db.getMerbyType(spinnertype.getSelectedItem().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //cap nhat thong tin tung san pham khi click vao
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(myView.getContext(),mer_edit.class);
                intent.putExtra("ID",adapter.getItem(i).getId());
                startActivityForResult(intent,20);
            }
        });

        //nut them san pham
        Button btnadd = (Button) view.findViewById(R.id.btn_add_mer);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchname.setText("");
                Intent intent = new Intent(myView.getContext(), mer_add.class);
                startActivityForResult(intent,10);
            }
        });

        //nut xu ly voi loai san pham
        Button typeedit = (Button) view.findViewById(R.id.btn_mer_type_edit);
        typeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myView.getContext(),mer_type.class);
                startActivityForResult(intent,30);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {//them san pham
            if (resultCode == 100){
                updatelistitem(db.getAllMer());
                Toast.makeText(myView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == 0)
                Toast.makeText(myView.getContext(),"Đã hủy thêm",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 20)//cap nhat thong tin 1 san pham
        {
            if (resultCode == 100){
                updatelistitem(db.getAllMer());
                Toast.makeText(myView.getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == 200){
                updatelistitem(db.getAllMer());
                Toast.makeText(myView.getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == 0)
                Toast.makeText(myView.getContext(),"Đã hủy cập nhật sản phẩm",Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 30)//cap nhat loai san pham
        {
            updatelistitem(db.getAllMer());
            updatespin();
        }
    }

    protected void updatelistitem(List<Mer> listtemp)
    {
        show.clear();
        show=listtemp;
        adapter = new MerAllAdapter(myView.getContext(),show);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void updatespin(){
        listtype.clear();
        listtype.add("Tất cả");
        listtype.addAll(db.getMerType());
        spinnertype.setAdapter(new ArrayAdapter(myView.getContext(),R.layout.item_spinner,listtype));
    }

}
